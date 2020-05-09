package com.example.mynotes

import android.app.Application
import com.jaredrummler.cyanea.Cyanea

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Cyanea.init(this, resources)
    }

}