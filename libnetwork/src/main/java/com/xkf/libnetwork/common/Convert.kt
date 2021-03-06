package com.xkf.libnetwork.common

import java.lang.reflect.Type


interface Convert {
    fun convert(content: String, type: Type): Any?
    
    fun convert(content: String, clazz: Class<Any>):Any?
}