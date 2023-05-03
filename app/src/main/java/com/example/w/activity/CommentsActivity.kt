package com.example.w.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.ShortCommentsAdapter
import com.example.w.database.ShortCommentsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentsActivity : AppCompatActivity() {

    private lateinit var adapter: ShortCommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewLC)

        // 获取传递过来的链接
        val id = intent.getIntExtra("id", -1)


        val retrofit = Retrofit.Builder().baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val appService = retrofit.create(AppService::class.java)

        appService.getLongComment(id)?.enqueue(object : Callback<ShortCommentsData> {
            override fun onResponse(
                call: Call<ShortCommentsData>,
                response: Response<ShortCommentsData>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {


                        val layoutManager =
                            LinearLayoutManager(this@CommentsActivity, RecyclerView.VERTICAL, false)

                        recyclerView.layoutManager = layoutManager


                        val longComments = data.comments
                        val listItems = mutableListOf<ShortCommentsData.Comment>()
                        for (comments in longComments) {
                            val listItem = ShortCommentsData.Comment(
                                avatar = comments.avatar,
                                author = comments.author,
                                content = comments.content,
                                )
                            Log.d("ggg","(:)-->> ${comments.content}");
                            listItems.add(listItem)
                        }


                        adapter = ShortCommentsAdapter(listItems)
                        recyclerView.adapter = adapter

                    }else Log.d("ggg","(:)-->> 评论没有请求回来")
                }
            }
            override fun onFailure(call: Call<ShortCommentsData>, t: Throwable) {
            }
        })
    }
}