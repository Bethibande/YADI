package com.yadi.core

import com.yadi.core.search.InstanceProvider
import com.yadi.core.search.Searchable

/**
 * A container allows you to combine multiple [Searchable] objects,
 * it also implements the [InstanceProvider] for convenience.
 */
interface Container: InstanceProvider {

    fun inject(searchable: Searchable): Container
    fun inject(searchable: Collection<Searchable>): Container
    fun inject(vararg searchable: Searchable): Container

}