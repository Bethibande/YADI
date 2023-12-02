package com.yadi.core.binding

import kotlin.reflect.KProperty

data class DependencyBinding<T>(
    val type: Class<T>,
    var tag: Any?,
    val provider: DependencyProvider<T>
) {

    fun get() = provider.provide()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

}