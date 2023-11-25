package com.yadi.core

import com.yadi.core.build.Injectable
import com.yadi.core.build.InstanceProvider

class DependencyInjector: Injectable, InstanceProvider {

    private val selfView = DependencyView()
    private val container = DependencyContainer()
        .withView(selfView)

    override fun view(): DependencyView = selfView
    override fun container(): Searchable = container

}