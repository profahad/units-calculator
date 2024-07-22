package com.fahad.common_domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    @SerialName("data") var data: String? = null,
    @SerialName("message") var message: String? = null,
    @SerialName("Message") var messageV2: String? = null,
    @SerialName("status") var status: Int? = null,
    @SerialName("resultCode") var resultCode: Int? = null,
    @SerialName("resulCodeType") var resulCodeType: String? = null
)