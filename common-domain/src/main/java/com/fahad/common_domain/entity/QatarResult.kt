package com.fahad.common_domain.entity

sealed class QatarResult<out T> {
    data class Success<out T>(val data: T) : QatarResult<T>()
    data class Failure<T>(val error: QatarError) : QatarResult<T>()
}
