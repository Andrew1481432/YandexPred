package com.example.teacher.retrofit

import com.example.teacher.retrofit.network.KtorClient
import com.example.teacher.retrofit.network.model.DataModel

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class GetRepo {

    companion object {
        const val PREDICATOR_URL = "https://predictor.yandex.net/"
        const val PREDICATOR_KEY = "сюда свой токен"
    }

    suspend fun getPred(text: String): DataModel =
        KtorClient.httpClient.get {
            url(PREDICATOR_URL+"api/v1/predict.json/complete")
            parameter("key", PREDICATOR_KEY)
            parameter("q", text)
            parameter("lang", "ru")
        }
}