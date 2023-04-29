package com.example.w


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import com.example.w.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(mBinding.root)


        mBinding.button.setOnClickListener {
            val retrofit = Retrofit
                .Builder()
                .baseUrl("https://news-at.zhihu.com/api/4")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
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
        class BannerPagerAdapter(private val bannerList: List<Int>) :
            RecyclerView.Adapter<BannerPagerAdapter.ViewHolder>() {

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val imageView: ImageView = itemView.findViewById(R.id.iv_banner_item_image)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_banner, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                Glide.with(holder.itemView)
                    .load(bannerList[position])
                    .into(holder.imageView)
            }

            override fun getItemCount(): Int {
                return bannerList.size
            }

        }
    }
}

