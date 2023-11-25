package com.yadi.core.bind

typealias Factory<T> = () -> T

fun interface DependencyProvider<T> {

    companion object {
        @JvmStatic
        fun <T> factory(fn: Factory<T>) = DependencyProvider { fn.invoke() }
        @JvmStatic
        fun <T> singleton(fn: Factory<T>) = SingletonDependencyProvider(fn)
    }

    fun provide(): T

}

class SingletonDependencyProvider<T>(fn: Factory<T>): DependencyProvider<T> {

    private val lazy = lazy(fn)

    override fun provide(): T = lazy.value
}