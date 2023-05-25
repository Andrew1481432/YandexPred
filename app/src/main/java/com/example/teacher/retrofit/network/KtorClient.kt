package com.example.teacher.retrofit.network

import android.util.Log
import com.example.teacher.retrofit.network.error.Failure
import com.example.teacher.retrofit.network.model.ErrorResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.net.UnknownHostException
import java.nio.charset.Charset

object KtorClient {

    private const val TIME_OUT = 30_000

    val json = Json {
        prettyPrint = true
        isLenient = true

        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    val httpClient = HttpClient(Android) {
        install(Logging) {
            logger = object: Logger{
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
        }

        HttpResponseValidator {
            handleResponseException { cause ->
                val error = when (cause) {
                    is ResponseException -> {
                        try {
                            val resp = getErrorResponseModel(cause.response)
                            Failure.HttpError(resp.code, resp.message)
                        } catch (e: IllegalArgumentException) {
                            Failure.ServerError
                        }
                    }
                    is UnknownHostException -> Failure.NetworkError
                    else -> Failure.UnknownError
                }
                throw NetworkException(error)
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(json)

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    private suspend fun getErrorResponseModel(responseContent: HttpResponse): ErrorResponse {
        val content = responseContent.readText(Charset.defaultCharset())
        return json.decodeFromString(ErrorResponse.serializer(), content)
    }

    suspend fun <T> HttpClient.requestAndCatch(
        block: suspend HttpClient.() -> T,
        errorHandler: suspend NetworkException.() -> T
    ): T = runCatching { block() }
        .getOrElse {
            when (it) {
                is NetworkException -> it.errorHandler()
                else -> throw it
            }
        }

}