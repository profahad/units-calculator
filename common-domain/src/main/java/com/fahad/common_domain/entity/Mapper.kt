package com.microsysx.common_domain

abstract class Mapper<in From, To> {

    abstract fun mapFrom(from: From): To

    fun mapFromList(from: List<From>): List<To> {
        return from.map { mapFrom(it) }
    }
}
