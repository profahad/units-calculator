package com.fahad.common_data.utils

import android.app.Activity

interface CurrentActivityProvider {
    fun get(): Activity
}
