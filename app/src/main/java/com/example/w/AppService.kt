package com.example.w

import android.icu.util.Calendar
import com.example.w.database.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
interface AppService {

    @GET("news/before/{date}")
    fun getPastNews(@Path("date") date: String?): Call<Data>?


    @GET("news/latest")
    fun getLatestNews(): Call<Data>?

    @GET("story/{id}/long-comments")
    fun getLongComment(@Path("id") id: String?): Call<Data>?


}