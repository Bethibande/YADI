package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.inject.Bindable
import com.yadi.core.inject.Injectable
import com.yadi.core.search.InstanceProvider
import com.yadi.core.search.Searchable

class Module: Container, Injectable {

    private val bindable = DependencyList()
    private val container = DefaultContainer().inject(bindable)

    override fun inject(searchable: Searchable): Module = apply { container.inject(searchable) }

    override fun inject(searchable: Collection<Searchable>): Module = apply { container.inject(searchable) }

    override fun inject(vararg searchable: Searchable): Container = apply { container.inject(*searchable) }

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = container.find(type, tag)

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = container.findAll(type)

    override fun bind(binding: DependencyBinding<*>): Bindable = bindable.bind(binding)
}

class ModuleBuilder(vararg imports: Searchable): Injectable, InstanceProvider {

    private val moduleBindings = DependencyList()
    private val importModule = Module().inject(*imports, moduleBindings)

    override fun bind(binding: DependencyBinding<*>): Bindable = moduleBindings.bind(binding)

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = importModule.find(type, tag)

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = importModule.findAll(type)

    fun moduleBindings() = moduleBindings

}

fun module(vararg imports: Searchable, fn: ModuleBuilder.() -> Unit): Module {
    val builder = ModuleBuilder(*imports)
    fn.invoke(builder)

    return Module().inject(builder.moduleBindings())
}