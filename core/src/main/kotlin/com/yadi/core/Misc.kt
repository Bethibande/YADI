package com.yadi.core

inline fun <T> T.with(fn: T.() -> Unit): T {
    fn.invoke(this)
    return this
}