package com.example.jettrivia.data

data class DataOrException<T, Boolean, E : Exception>(
    var data: T? = null,
    var loadingStatus: Boolean? = null,
    var e: Exception? = null
)