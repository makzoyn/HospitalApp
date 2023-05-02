package com.example.hospitalapp.database

import android.content.Context
import android.content.SharedPreferences
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.ui.DoctorFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.UUID


class DatabaseHelper(context: Context) {
    private var repositoryDatabase = context.getSharedPreferences("repositoryDatabase", Context.MODE_PRIVATE)
    private var gson = Gson()

    fun saveRemind(hospitals: ArrayList<Hospital?>?) {
        val editor = repositoryDatabase!!.edit()
        editor.putString("hospitals", gson.toJson(hospitals))
        editor.apply()
    }

    fun getHospital(): ArrayList<Hospital?> {
        val hospitalsString = repositoryDatabase!!.getString("hospitals", null)
        val hospitalListType: Type = object : TypeToken<ArrayList<Hospital?>?>() {}.type
        val hospitals: ArrayList<Hospital?> = gson.fromJson(hospitalsString, hospitalListType)
        return hospitals ?: ArrayList<Hospital?>()
    }
}