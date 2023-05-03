package com.example.w.activity


import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.BannerPagerAdapter
import com.example.w.adapter.StoryAdapter
import com.example.w.database.Data
import com.example.w.database.PastData
import com.example.w.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Call
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
    private lateinit var adapter: StoryAdapter
    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    // 定义更新时间和日期的 Runnable 对象
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



        icon()
        network()

    }


    // 初始化头像 ImageView 并添加点击事件
    private fun icon() {
        mIvIcon = findViewById(R.id.custom_iv)
        mIvIcon?.setOnClickListener {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    // 声明销毁 Activity 时移除消息队列中的更新时间、日期的操作，缩短程序的运行时间和节省系统资源
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


    // 根据当前时间获取相应的问候信息
    private fun getGreetingMessage(hourOfDay: Int): String {
        return when (hourOfDay) {
            in 0..5 -> "早点休息~"
            in 6..10 -> "早上好!"
            in 11..13 -> "知乎日报"
            in 14..18 -> "下午好!"
            else -> "晚上好!"
        }
    }

    private fun network() {


        val retrofit = Retrofit.Builder().baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val appService = retrofit.create(AppService::class.java)

        appService.getLatestNews()?.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    // 解析得到的数据
                    val data = response.body()
                    if (data != null) {
                        // 设置轮播图的适配器
                        val topStories = data.top_stories
                        val bannerItems = mutableListOf<Data.TopStory>()
                        for (topStory in topStories) {
                            val bannerItem = Data.TopStory(
                                image = topStory.image,
                                title = topStory.title,
                                hint = topStory.hint,
                                url = topStory.url,
                                id = topStory.id
                            )
                            bannerItems.add(bannerItem)
                        }


                        bannerAdapter = BannerPagerAdapter(bannerItems)
                        mBinding.viewPagerBanner.adapter = bannerAdapter

                        // 设置新闻列表的适配器
                        val layoutManager =
                            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

                        mBinding.recyclerView.layoutManager = layoutManager


                        val stories = data.stories
                        val listItems = mutableListOf<Data.Story>()
                        for (story in stories) {
                            val listItem = Data.Story(
                                images = story.images,
                                title = story.title,
                                hint = story.hint,
                                url = story.url,
                                id = story.id
                            )
                            listItems.add(listItem)
                        }


                        adapter = StoryAdapter(listItems)
                        mBinding.recyclerView.adapter = adapter



                        // 监听滑动事件，实现上拉加载
                        mBinding.recyclerView.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                super.onScrolled(recyclerView, dx, dy)

                                if (dy > 0) {
                                    more()
                                }
                            }
                        })

                        // 添加下拉刷新监听器
                        mBinding.swipeRefreshLayout.setOnRefreshListener {
                            GlobalScope.launch(Dispatchers.IO) {
                                network()
                                withContext(Dispatchers.Main) {
                                    adapter.notifyDataSetChanged()
                                }

                                // 等待一秒钟，以确保用户能够看到下拉刷新的动画效果
                                delay(1000)

                                // 关闭下拉刷新动画
                                withContext(Dispatchers.Main) {
                                    mBinding.swipeRefreshLayout.isRefreshing = false
                                }
                            }

                        }

                    }
                }

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
            }
        })


    }

    fun more() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val year = calendar.get(Calendar.YEAR).toString().takeLast(2)
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')

        val formattedDate = "$year$month$day"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val appService = retrofit.create(AppService::class.java)

        appService.getPastNews(formattedDate)?.enqueue(object : Callback<PastData> {
            override fun onResponse(call: Call<PastData>, response: Response<PastData>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {
                        adapter.addData(data.stories)
                    }
                }
            }

            override fun onFailure(call: Call<PastData>, t: Throwable) {}
        })
    }
}

