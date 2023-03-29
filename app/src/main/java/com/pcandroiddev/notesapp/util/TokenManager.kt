package com.pcandroiddev.notesapp.util

import android.content.Context
import android.content.SharedPreferences
import com.pcandroiddev.notesapp.util.Constants.PREFS_TOKEN_FILE
import com.pcandroiddev.notesapp.util.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun saveToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun deleteToken() {
        editor.remove(USER_TOKEN)
        editor.apply()
    }


}

