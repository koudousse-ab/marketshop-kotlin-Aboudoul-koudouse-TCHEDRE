package com.marketshop.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(name: String, email: String, phone: String) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_phone", phone)
            .apply()
    }

    fun getUserName(): String = prefs.getString("user_name", "") ?: ""
    fun getUserEmail(): String = prefs.getString("user_email", "") ?: ""
    fun getUserPhone(): String = prefs.getString("user_phone", "") ?: ""

    fun isDarkMode(): Boolean = prefs.getBoolean("dark_mode", false)
    fun setDarkMode(isDark: Boolean) = prefs.edit().putBoolean("dark_mode", isDark).apply()
    fun clearAllData() = prefs.edit().clear().apply()
}