package com.kevinduran.myshop.config.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("com.kevinduran.borroweds.PREFERENCES_FILE", Context.MODE_PRIVATE)

    fun saveString(key:String, value:String) {
        sharedPreferences.edit {
            putString(key, value)
            apply()
        }
    }

    fun saveInt(key:String, value:Int) {
        sharedPreferences.edit {
            putInt(key, value)
            apply()
        }
    }

    fun saveLong(key:String, value:Long) {
        sharedPreferences.edit {
            putLong(key, value)
            apply()
        }
    }

    fun getString(key:String) : String {
        return sharedPreferences.getString(key, null) ?: ""
    }

    fun getInt(key:String) : Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun getLong(key:String) : Long {
        return sharedPreferences.getLong(key, 0L)
    }
}