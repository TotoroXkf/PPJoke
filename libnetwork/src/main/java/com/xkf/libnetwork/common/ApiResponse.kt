package com.xkf.libnetwork.common

class ApiResponse<T>(
    var success: Boolean = false,
    var status: Int = 0,
    var message: String = "",
    var body: T? = null
)