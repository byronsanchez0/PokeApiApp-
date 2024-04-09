//package com.example.apppokeapi.domain.domainutils
//
//import kotlin.Result
//
//sealed class Result<out T>{
//    data class Success<out T>(val data: T) : Result<T>()
//    data class Error(val exception: Exception) : Result<Nothing>()
//
//
//}