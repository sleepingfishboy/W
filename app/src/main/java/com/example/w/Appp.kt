package com.example.w

import android.app.Application
import android.os.Looper
import android.widget.Toast
import android.os.Handler

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
class Appp :Application(){
    companion object{
        lateinit var mContext:Appp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        Handler(Looper.myLooper()!!).post{
            while (true){
                try {
                    Looper.loop()
                }catch (e:java.lang.Exception){
                    Toast.makeText(mContext,"有异常！！，不过被抓住了",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}