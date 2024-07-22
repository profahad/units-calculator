package com.fahad.common_domain.entity

sealed class QatarError(val throwable: Throwable) {
    class Offline(throwable: Throwable) : QatarError(throwable)
    class Timeout(throwable: Throwable) : QatarError(throwable)
    class Unknown(throwable: Throwable, val isFromServer : Boolean = false) : QatarError(throwable)
}
