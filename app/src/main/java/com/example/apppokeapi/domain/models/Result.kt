package com.example.apppokeapi.domain.models

import java.io.IOException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: AppException) : Result<Nothing>()
}

sealed class RetrofitException(message: String?, cause: Throwable?) : AppException(message, cause) {
    class NetworkError(message: String?, cause: Exception) : RetrofitException(message, cause)
    class HttpError(message: String?, val code: Int, val errorBody: String?) : RetrofitException(message, null)
    class UnexpectedError(message: String?, cause: Throwable?) : RetrofitException(message, cause)
}

open class AppException(
    message: String?,
    cause: Throwable?,
) : Exception()