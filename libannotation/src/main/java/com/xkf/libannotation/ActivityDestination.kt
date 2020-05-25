package com.xkf.libannotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class ActivityDestination(
    val pageUrl: String,
    val needLogin: Boolean = false,
    val asStarter: Boolean = false
)