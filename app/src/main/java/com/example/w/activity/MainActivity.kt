package com.example.w.activity


import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.BannerPagerAdapter
import com.example.w.database.Data
import com.example.w.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mTvDate: TextView? = null
    private var mTvMon: TextView? = null
    private var mTvGreet: TextView? = null
    private var mIvIcon: ImageView? = null
    private lateinit var bannerAdapter: BannerPagerAdapter
    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val updateDateTimeRunnable = object : Runnable {
        override fun run() {
            val calendar = Calendar.getInstance()
            val monthStr = SimpleDateFormat("MMM", Locale.CHINA).format(calendar.time)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val greeting = getGreetingMessage(calendar.get(Calendar.HOUR_OF_DAY))
            mTvDate = findViewById(R.id.custom_tv_date)
            mTvMon = findViewById(R.id.custom_tv_mon)
            mTvGreet = findViewById(R.id.custom_tv_greet)
            mTvMon?.text = monthStr   // 将月份显示在对应的 TextView 中

            // 显示日期和问候消息
            mTvDate?.text = dayOfMonth.toString()
            mTvGreet?.text = greeting

            handler.postDelayed(this, 1000)
        }
    }


    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        handler.post(updateDateTimeRunnable)



        bar()
        network()

    }

    private fun bar() {
        Log.d("ggg", "(:)-->> bar")
        mIvIcon = findViewById(R.id.custom_iv)
        mIvIcon?.setOnClickListener {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun getGreetingMessage(hourOfDay: Int): String {
        return when (hourOfDay) {
            in 0..5 -> "早点休息~"
            in 6..10 -> "早上好!"
            in 11..13 -> "知乎日报"
            in 14..18 -> "下午好!"
            else -> "晚上好"
        }
    }

    private fun network() {


        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val appService = retrofit.create(AppService::class.java)

        appService.getLatestNews()?.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {

                        val topStories = data.top_stories
                        val bannerItems = mutableListOf<Data.TopStory>()
                        for (topStory in topStories) {
                            val bannerItem = Data.TopStory(
                                image = topStory.image,
                                title = topStory.title,
                                hint = topStory.hint
                            )
                            bannerItems.add(bannerItem)
                        }

                        // 初始化 ViewPager2 组件


                        // 设置适配器
                        val bannerAdapter = BannerPagerAdapter(bannerItems)
                        mBinding.viewPagerBanner.adapter = bannerAdapter

                    }
                }

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
            }
        })

    }



}

