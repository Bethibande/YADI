package com.yadi.core.build

import com.yadi.core.DependencyView
import com.yadi.core.bind.DependencyBinding
import com.yadi.core.bind.DependencyProvider
import com.yadi.core.bind.Factory

infix fun DependencyBinding<*>.tagged(tag: Any) {
    this.tag = tag
}

/**
 * Provides functions for injecting dependencies
 */
interface Injectable {

    class TypeBindingBuilder<T>(private val type: Class<T>, private val builder: Injectable) {

        fun bind(provider: DependencyProvider<T>, tag: Any?) = DependencyBinding(type, tag, provider)
            .apply { builder.bind(this) }

        fun singleton(fn: Factory<T>) = singleton(fn, null)

        fun singleton(fn: Factory<T>, tag: Any?) = bind(DependencyProvider.singleton(fn), tag)

        fun factory(fn: Factory<T>) = factory(fn, null)

        fun factory(fn: Factory<T>, tag: Any?) = bind(DependencyProvider.factory(fn), tag)

    }

    /**
     * The view used by this builder class.
     */
    fun view(): DependencyView

    private fun withView(fn: DependencyView.() -> Unit) {
        fn.invoke(view())
    }

    fun bind(binding: DependencyBinding<*>) = withView {
        bind(binding)
    }

    fun <T> bind(type: Class<T>, tag: Any?, provider: DependencyProvider<T>) = DependencyBinding(type, tag, provider)
        .apply { bind(this) }

    fun <T> factory(type: Class<T>, tag: Any?, fn: Factory<T>) = bind(type, tag, DependencyProvider.factory(fn))
    fun <T> factory(type: Class<T>, fn: Factory<T>) = factory(type, null, fn)
    fun <T> singleton(type: Class<T>, tag: Any?, fn: Factory<T>) = bind(type, tag, DependencyProvider.singleton(fn))
    fun <T> singleton(type: Class<T>, fn: Factory<T>) = singleton(type, null, fn)

}

inline fun <reified T> Injectable.factory(tag: Any?, noinline fn: Factory<T>) = factory(T::class.java, tag, fn)
inline fun <reified T> Injectable.factory(noinline fn: Factory<T>) = factory(null, fn)
inline fun <reified T> Injectable.singleton(tag: Any?, noinline fn: Factory<T>) = singleton(T::class.java, tag, fn)
inline fun <reified T> Injectable.singleton(noinline fn: Factory<T>) = singleton(null, fn)

inline fun <reified T> Injectable.bind(fn: Injectable.TypeBindingBuilder<T>.() -> Unit) {
    fn.invoke(Injectable.TypeBindingBuilder(T::class.java, this))
}