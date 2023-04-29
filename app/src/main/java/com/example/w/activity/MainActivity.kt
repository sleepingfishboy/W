package com.example.w.activity


import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import com.example.w.databinding.ActivityMainBinding
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.BannerPagerAdapter
import com.example.w.database.Data
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {


    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(mBinding.root)


//        mBinding.button.setOnClickListener {
//            val retrofit = Retrofit
//                .Builder()
//                .baseUrl("https://news-at.zhihu.com/api/4")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            val appService = retrofit.create(AppService::class.java)
//            appService.getData().enqueue(object : Callback<List<Data>> {
//                override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
//                    val list = response.body()
//                    if (list != null) {
//                        for (app in list){
//                            Log.d("ggg","(:)-->> id is ${app.date}");
//                        }
//                    }
//                }
//
//
//            })
//        }

        mBinding.button.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://news-at.zhihu.com/api/4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val appService = retrofit.create(AppService::class.java)

            appService.getData().enqueue(object : Callback<List<Data>> {
                override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        if (list != null) {
                            for (data in list) {
                                Log.d("ggg", "(:)-->> id is ${data.date}")
                            }
                        }
                    } else {
                        Log.e("ggg", "请求失败: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                    Log.e("ggg", "网络错误", t)
                }
            })
        }



        data class Data(
            val data: String
        )






        photograph()



    }










    fun photograph() {


        val bannerList = mutableListOf(
            R.drawable.banner_1,
            R.drawable.banner_2,
            R.drawable.banner_3
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