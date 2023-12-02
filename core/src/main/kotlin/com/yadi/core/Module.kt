package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.inject.Bindable
import com.yadi.core.inject.Injectable
import com.yadi.core.search.InstanceProvider
import com.yadi.core.search.Searchable

class Module(): Container, Injectable {

    private val bindable = DependencyList()
    private val container = DefaultContainer().inject(bindable)

    constructor(bindings: Searchable): this() {
        bindable.bind(bindings.findAll())
    }

    override fun inject(searchable: Searchable) = apply {
        checkForLoop(searchable)
        container.inject(searchable)
    }

    override fun inject(searchable: Collection<Searchable>) = apply {
        checkForLoop(*searchable.toTypedArray())
        container.inject(searchable)
    }

    override fun inject(vararg searchable: Searchable) = apply {
        checkForLoop(*searchable)
        container.inject(*searchable)
    }

    override fun injected(): Collection<Searchable> = container.injected()

    override fun remove(searchable: Searchable) = apply { container.remove(searchable) }

    override fun remove(searchable: Collection<Searchable>) = apply { container.remove(searchable) }

    override fun remove(vararg searchable: Searchable) = apply { container.remove(*searchable) }

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = container.find(type, tag)

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = container.findAll(type)

    override fun findAll(): List<DependencyBinding<*>> = container.findAll()

    override fun bind(binding: DependencyBinding<*>): Bindable = bindable.bind(binding)
}

class ModuleBuilder(vararg imports: Searchable): Injectable, InstanceProvider {

    private val moduleBindings = DependencyList(true)
    private val importModule = Module().inject(*imports, moduleBindings)

    override fun bind(binding: DependencyBinding<*>): Bindable = moduleBindings.bind(binding)

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = importModule.find(type, tag)

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = importModule.findAll(type)

    override fun findAll(): List<DependencyBinding<*>> = importModule.findAll()

    fun moduleBindings() = moduleBindings

    infix fun DependencyBinding<*>.tagged(tag: Any?) {
        this.tag = tag
    }

}

fun module(vararg imports: Searchable, fn: ModuleBuilder.() -> Unit): Module {
    val builder = ModuleBuilder(*imports)
    fn.invoke(builder)

    return Module(builder.moduleBindings())
}