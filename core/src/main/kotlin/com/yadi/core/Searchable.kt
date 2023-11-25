package com.yadi.core

import com.yadi.core.bind.DependencyBinding

/**
 * This interface provides methods to search for dependencies of a certain type.
 */
interface Searchable {

    fun <T> findAll(type: Class<T>): List<DependencyBinding<T>>
    fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>?

}