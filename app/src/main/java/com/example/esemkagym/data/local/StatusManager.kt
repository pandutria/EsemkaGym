package com.example.esemkagym.data.local

import android.content.Context

class StatusManager(context: Context) {
    val pref = "prefs"
    val key = "keys"

    val shared = context.getSharedPreferences(pref, Context.MODE_PRIVATE)

    fun save(isCheckIn: Boolean) {
        shared.edit().putBoolean(key, isCheckIn).apply()
    }

    fun get(): Boolean? {
        return shared.getBoolean(key, false)
    }

    fun clear() {
        return shared.edit().clear().apply()
    }
}