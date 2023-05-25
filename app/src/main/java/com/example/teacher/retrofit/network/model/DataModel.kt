package com.example.teacher.retrofit.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DataModel(
    @SerialName("endOfWord")
    val isEndOfWord: Boolean,
    var pos: Int,
    var text: List<String>
)