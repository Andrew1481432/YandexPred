package com.example.teacher.retrofit

import com.example.teacher.retrofit.network.KtorClient
import com.example.teacher.retrofit.network.KtorClient.requestAndCatch
import com.example.teacher.retrofit.network.error.Failure

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testPredFall() = runTest {
        KtorClient.httpClient.requestAndCatch({
            GetRepo().getPred("")
        }, {
            when (failure) {
                is Failure.NetworkError -> {

                }
                is Failure.HttpError -> {
                    assertEquals("Invalid parameter: q", (failure as Failure.HttpError).message)
                }
                else -> throw this
            }
        })
    }


    @Test
    fun testPredSuccess() = runTest {
        KtorClient.httpClient.requestAndCatch({
            val modelResp = GetRepo().getPred("hello")
            assertEquals("kitty", modelResp.text[0])
        }, {
            when (failure) {
                is Failure.NetworkError -> {

                }
                is Failure.HttpError -> {

                }
                else -> throw this
            }
        })
    }

}