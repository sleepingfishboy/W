package com.example.w.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.w.AppService
import com.example.w.R
import com.example.w.adapter.LongCommentsAdapter
import com.example.w.database.LongCommentsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentsActivity : AppCompatActivity() {


    private lateinit var adapter: LongCommentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewLC)

        // 获取传递过来的链接
        val id = intent.getStringExtra("id")
        Log.d("ggg", "(:)-->> $id")


        val retrofit = Retrofit.Builder().baseUrl("https://news-at.zhihu.com/api/4/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val appService = retrofit.create(AppService::class.java)

//        appService.getLongComment(id)?.enqueue(object : Callback<LongCommentsData> {
//            override fun onResponse(
//                call: Call<LongCommentsData>,
//                response: Response<LongCommentsData>
//            ) {
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    if (data != null) {
//
//
//                        val layoutManager =
//                            LinearLayoutManager(this@CommentsActivity, RecyclerView.VERTICAL, false)
//
//                        recyclerView.layoutManager = layoutManager
//
//
//                        val longComments = data.comments
//                        val listItems = mutableListOf<LongCommentsData.Comment>()
//                        for (comments in longComments) {
//                            val listItem = LongCommentsData.Comment(
//                                avatar = comments.avatar,
//                                author = comments.author,
//                                content = comments.content,
//                                )
//                            listItems.add(listItem)
//                        }
//
//
//                        adapter = LongCommentsAdapter(listItems)
//                        recyclerView.adapter = adapter
//
//                    }
//                }
//            }
//            override fun onFailure(call: Call<LongCommentsData>, t: Throwable) {
//            }
//        })
    }





}
































