package com.xkf.libnetwork

class ApiResponse<T>(
    var success: Boolean = false,
    var status: Int = 0,
    var message: String = "",
    var body: T? = null
)