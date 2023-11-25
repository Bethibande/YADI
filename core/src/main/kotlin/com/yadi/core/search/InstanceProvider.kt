package com.yadi.core.search

interface InstanceProvider: Searchable {

    fun <T> instance(type: Class<T>, tag: Any?): T {
        return find(type, tag)?.get() ?: throw NoSuchElementException("No value of type '$type' with tag '$tag' found")
    }

    fun <T> instance(type: Class<T>) = instance(type, null)

}

inline fun <reified T> InstanceProvider.instance(tag: Any?): T = instance(T::class.java, tag)
inline fun <reified T> InstanceProvider.instance(): T = instance<T>(null)