package com.example.w

import android.app.Application
import android.os.Looper
import android.widget.Toast
import java.util.logging.Handler

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
class App :Application(){
    companion object{
        lateinit var mContext:App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        android.os.Handler(Looper.myLooper()!!).post{
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