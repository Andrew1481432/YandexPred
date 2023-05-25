package com.example.teacher.retrofit.network.error

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object NetworkError : Failure()
    object ServerError : Failure()
    object UnknownError : Failure()

    abstract class FeatureFailure : Failure()

    class HttpError(val code: Int, val message: String) : FeatureFailure()
}