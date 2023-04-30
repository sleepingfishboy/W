package com.example.w.activity


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyCallback.DataActivationStateListener
import android.transition.Transition
import android.util.Log
import android.view.Gravity


import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.BannerPagerAdapter
import com.example.w.database.Data
import com.example.w.databinding.ActivityMainBinding
import com.google.gson.internal.bind.DateTypeAdapter

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.*


class MainActivity : AppCompatActivity() {


    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val updateDateTimeRunnable = object : Runnable {
        override fun run() {
            val calendar = Calendar.getInstance()
            val monthStr = SimpleDateFormat("MMM", Locale.CHINA).format(calendar.time)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val greeting = getGreetingMessage(calendar.get(Calendar.HOUR_OF_DAY))
            mBinding.toolbar.subtitle = "$monthStr\n$dayOfMonth | $greeting"
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

        // Set up circular image on the right side of Toolbar to launch SecondActivity
        val imageView = ImageView(this).apply {
            setImageResource(R.drawable.icon) // Replace with your image resource ID
            setOnClickListener {
                val intent = Intent(this@MainActivity, SignInActivity::class.java)

                startActivity(intent)

            }
        }
        val layoutParams = Toolbar.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.toolbar_image_size),
            resources.getDimensionPixelSize(R.dimen.toolbar_image_size)
        )
        layoutParams.gravity =
            Gravity.END or Gravity.CENTER_VERTICAL // Move the image to the right side of Toolbar
        layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.toolbar_image_margin)
        imageView.layoutParams = layoutParams
        mBinding.toolbar.addView(imageView, layoutParams)

        network()
        photograph()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun getGreetingMessage(hourOfDay: Int): String {
        return when (hourOfDay) {
            in 0..5 -> "现在是凌晨!"
            in 6..10 -> "早上好!"
            in 11..13 -> "知乎日报"
            in 14..18 -> "下午好!"
            else -> "晚上好"
        }
    }


    fun network() {


        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val appService = retrofit.create(AppService::class.java)
//        appService.getLatestNews()?.enqueue(object : Callback<Data?> {
//            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
//                val data = response.body()
//                if (data!=null){
//
//                }
//            }
//        })
        appService.getLatestNews()?.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Log.d("ggg", "(:)-->> ${data.top_stories}")
                    }
                }

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                // 网络请求失败的情况
            }
        })

    }


    private fun photograph() {


        val bannerList = mutableListOf(
            R.drawable.banner_1,
            R.drawable.banner_2,

            R.drawable.banner_3,
            R.drawable.banner_4,
            R.drawable.banner_5
        )

        // 实例化 ViewPager 和 Adapter
        mBinding.viewPagerBanner
        val bannerAdapter = BannerPagerAdapter(bannerList)

        // 绑定 Adapter
        mBinding.viewPagerBanner.adapter = bannerAdapter

        val mRecyclerView = mBinding.viewPagerBanner.getChildAt(0) as RecyclerView
        mRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER // 关闭 over-scroll 效果
        mBinding.viewPagerBanner.offscreenPageLimit = 1 // 设置预加载为1，这里需要根据实际情况来调整


        // 循环滚动
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(3000)
                val currentPosition = mBinding.viewPagerBanner.currentItem
                mBinding.viewPagerBanner.setCurrentItem(currentPosition + 1, true)

                // 用于无限循环，如果到了最后一页就重新开始
                if (currentPosition == bannerList.size - 1) {
                    delay(500) // 等待页面切换完毕再跳转
                    mBinding.viewPagerBanner.setCurrentItem(0, false)
                }
            }
        }

    }

}

