package com.yadi.core.search

import com.yadi.core.binding.DependencyBinding

interface Searchable {

    fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>?
    fun <T> findAll(type: Class<T>): List<DependencyBinding<T>>
    fun findAll(): List<DependencyBinding<*>>

}