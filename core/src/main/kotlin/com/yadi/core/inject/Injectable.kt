package com.yadi.core.inject

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.binding.DependencyProvider
import com.yadi.core.binding.Factory

/**
 * Provides functions for injecting bindings into an object that implements Bindable
 */
interface Injectable: Bindable {

    class TypedBinder<T>(private val type: Class<T>, private val target: Bindable) {

        fun bind(tag: Any?, provider: DependencyProvider<T>) = DependencyBinding(type, tag, provider)
            .apply { target.bind(this) }

        fun singleton(tag: Any?, fn: Factory<T>) = bind(tag, DependencyProvider.singleton(fn))
        fun singleton(fn: Factory<T>) = singleton(null, fn)

        fun factory(tag: Any?, fn: Factory<T>) = bind(tag, DependencyProvider.factory(fn))
        fun factory(fn: Factory<T>) = bind(null, fn)

    }

    fun <T> bind(type: Class<T>, fn: TypedBinder<T>.() -> Unit) = apply {
        val binder = TypedBinder(type, this)
        fn.invoke(binder)
    }

    fun <T> bind(type: Class<T>, tag: Any?, provider: DependencyProvider<T>) = DependencyBinding(type, tag, provider)
        .apply { bind(this) }

    fun <T> bindSingleton(type: Class<T>, tag: Any?, fn: Factory<T>) = bind(type, tag, DependencyProvider.singleton(fn))
    fun <T> bindSingleton(type: Class<T>, fn: Factory<T>) = bindSingleton(type, null, fn)

    fun <T> bindFactory(type: Class<T>, tag: Any?, fn: Factory<T>) = bind(type, tag, DependencyProvider.factory(fn))
    fun <T> bindFactory(type: Class<T>, fn: Factory<T>) = bindFactory(type, null, fn)

}

inline fun <reified T> Injectable.bindSingleton(tag: Any?, noinline fn: Factory<T>) = bindSingleton(T::class.java, tag, fn)
inline fun <reified T> Injectable.bindSingleton(noinline fn: Factory<T>) = bindSingleton(null, fn)

inline fun <reified T> Injectable.bindFactory(tag: Any?, noinline fn: Factory<T>) = bindFactory(T::class.java, tag, fn)
inline fun <reified T> Injectable.bindFactory(noinline fn: Factory<T>) = bindFactory(null, fn)

inline fun <reified T> Injectable.bind(noinline fn: Injectable.TypedBinder<T>.() -> Unit) = apply {
    bind(T::class.java, fn)
}