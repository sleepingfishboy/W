package com.example.w.activity


import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity


import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.RecyclerView
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.BannerPagerAdapter
import com.example.w.databinding.ActivityMainBinding

import kotlinx.coroutines.*
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
            in 0..5 -> "现在是凌晨"
            in 6..10 -> "早上好"
            in 11..13 -> "知乎日报"
            in 14..18 -> "下午好"
            else -> "晚上好"
        }
    }


    fun network() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val appService = retrofit.create(AppService::class.java)

        try {
            // 尝试使用同步请求 .execute() 替换 .enqueue() 来获取异常信息
            val list = appService.getData().execute().body()
            if (list != null) {
                for (data in list) {
                    Log.d("ggg", "(:)-->> id is ${data.top_stories}")
                }
            } else {
                Log.e("ggg", "请求失败: 数据为空")
            }
        } catch (e: Exception) {
            Log.e("ggg", "请求失败: ${e.message}")
        }

//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://news-at.zhihu.com/api/4/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            val appService = retrofit.create(AppService::class.java)
//
//            appService.getData().enqueue(object : Callback<List<Data>> {
//                override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
//                    if (response.isSuccessful) {
//                        val list = response.body()
//                        if (list != null) {
//                            for (data in list) {
//                                Log.d("ggg", "(:)-->> id is ${data.top_stories}")
//                            }
//                        }
//                    } else {
//                        Log.e("ggg", "请求失败: ${response.code()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Data>>, t: Throwable) {
//                    Log.e("ggg", "网络错误", t)
//                }
//            })

    }



    private fun photograph() {
        Log.d("ggg", "(:)-->> 图片方法被使用了")

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

