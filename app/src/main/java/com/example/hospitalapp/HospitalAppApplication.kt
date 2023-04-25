package com.example.hospitalapp

import android.app.Application
import android.content.Context
import com.example.hospitalapp.repository.HospitalRepository

class HospitalAppApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        HospitalRepository.newInstance()
    }

    init{
        instance = this
    }

    companion object {
        private var instance : HospitalAppApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}