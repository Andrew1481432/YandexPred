package com.example.teacher.retrofit.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val code: Int, val message: String)