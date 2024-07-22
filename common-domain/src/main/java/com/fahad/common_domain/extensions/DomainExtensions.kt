package com.fahad.common_domain.extensions

import com.fahad.common_domain.entity.QatarError
import com.fahad.common_domain.entity.QatarResult


fun <T> T.toResult(): QatarResult<T> {
    return QatarResult.Success(this)
}

fun <T> T?.toResultOrError(): QatarResult<T> {
    return if (this != null) QatarResult.Success(this)
    else QatarResult.Failure(QatarError.Unknown(Exception("data is null")))
}

fun <T> QatarError.toResult(): QatarResult<T> {
    return QatarResult.Failure(this)
}

fun <T> QatarResult<T>.isSuccess(): Boolean {
    return this is QatarResult.Success
}

fun <T> QatarResult<T>.isFailure(): Boolean {
    return this is QatarResult.Failure
}

@Deprecated("Unsafe", ReplaceWith("fold, getOrNull, getOrElse, onSuccess"))
fun <T> QatarResult<T>.getData(): T {
    return (this as QatarResult.Success).data
}

fun <T> QatarResult<T>.getOrNull(): T? {
    return (this as? QatarResult.Success)?.data
}

fun <T> QatarResult<T>.getError(): QatarError {
    return (this as QatarResult.Failure).error
}

fun <T> QatarResult<T>.getOrElse(onFailure: (error: QatarError) -> T) = when (this) {
    is QatarResult.Success -> data
    is QatarResult.Failure -> onFailure(error)
}

fun QatarResult<String>.getOrEmpty(): String = when (this) {
    is QatarResult.Success -> data
    is QatarResult.Failure -> ""
}

suspend fun <T> QatarResult<*>.getToken(onSuccess: suspend (value: String) -> QatarResult<T>): QatarResult<T> = when (this) {
    is QatarResult.Success -> onSuccess(data as String)
    is QatarResult.Failure -> this as QatarResult.Failure<T>
}

inline fun <R, T> QatarResult<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (error: QatarError) -> R
): R = when (this) {
    is QatarResult.Success -> onSuccess(data)
    is QatarResult.Failure -> onFailure(error)
}

inline fun <R, T> QatarResult<T>.map(
    transform: (T) -> R
): QatarResult<R> = when (this) {
    is QatarResult.Success -> QatarResult.Success(transform(data))
    is QatarResult.Failure -> QatarResult.Failure(error)
}

inline fun <R, T> QatarResult<T>.flatMap(
    transform: (T) -> QatarResult<R>
): QatarResult<R> = when (this) {
    is QatarResult.Success -> transform(data)
    is QatarResult.Failure -> QatarResult.Failure(error)
}

inline fun <T> QatarResult<T>.onSuccess(
    onSuccess: (value: T) -> Unit
): QatarResult<T> {
    if (this is QatarResult.Success) onSuccess(data)
    return this
}

inline fun <T> QatarResult<T>.onFailure(
    onFailure: (error: QatarError) -> Unit
): QatarResult<T> {
    if (this is QatarResult.Failure) onFailure(error)
    return this
}
