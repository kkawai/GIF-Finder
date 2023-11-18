package com.kk.android.fuzzy_waddle.util

import android.util.Base64

object Util {
    fun encodeString(str: String): String {
        return Base64.encodeToString(str.toByteArray(),Base64.URL_SAFE)
    }

    fun decodeString(str: String): String {
        return String(Base64.decode(str, Base64.URL_SAFE))
    }
}