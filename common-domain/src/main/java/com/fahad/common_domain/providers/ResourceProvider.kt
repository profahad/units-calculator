package com.fahad.common_domain.providers

import androidx.annotation.ColorRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes id: Int, vararg args: Any): String
    fun getStringByKey(key: String): String
    fun getStringByKeyEng(key: String): String
    fun getColor(@ColorRes id: Int): Int
    fun getQuantityString(@PluralsRes id: Int, quantity: Int): String
}
