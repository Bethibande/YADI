package com.yadi.core.build

import com.yadi.core.Searchable

interface InstanceProvider {

    /**
     * The searchable base used to get object instances
     */
    fun container(): Searchable

    fun <T> instance(type: Class<T>, tag: Any?): T {
        return container().find(type, tag)?.get() ?: throw NoSuchElementException("Value of type '$type' with tag '$tag'")
    }

    fun <T> instance(type: Class<T>): T = instance(type, null)

}

inline fun <reified T> InstanceProvider.instance(tag: Any?): T = instance(T::class.java, tag)
inline fun <reified T> InstanceProvider.instance(): T = instance<T>(null)