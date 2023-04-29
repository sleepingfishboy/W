package com.example.w

import com.example.w.database.Data
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
interface AppService {
    @GET("news/latest")
fun getData(): Call<List<Data>>

}