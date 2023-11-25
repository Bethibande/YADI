package com.yadi.core

import com.yadi.core.inject.Injectable
import com.yadi.core.search.InstanceProvider
import com.yadi.core.search.Searchable

interface Container: Injectable, InstanceProvider {

    fun inject(searchable: Searchable): Container
    fun inject(searchable: Collection<Searchable>): Container
    fun inject(vararg searchable: Searchable): Container

}