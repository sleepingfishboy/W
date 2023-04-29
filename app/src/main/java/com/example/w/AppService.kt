package com.example.w

import com.google.gson.internal.bind.DateTypeAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import java.util.*

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
interface AppService {
    @GET("news/latest")
fun getData(): Call<List<App>>

}