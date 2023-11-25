package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.inject.Bindable
import com.yadi.core.inject.Injectable
import com.yadi.core.search.InstanceProvider
import com.yadi.core.search.Searchable
import java.util.concurrent.CopyOnWriteArrayList

class Module: Container {

    private val base = DependencyList()
    private val views = CopyOnWriteArrayList<Searchable>(arrayOf(base))

    override fun inject(searchable: Searchable) = apply {
        views.add(searchable)
    }

    override fun inject(searchable: Collection<Searchable>): Container = apply {
        views.addAll(searchable)
    }

    override fun inject(vararg searchable: Searchable): Container = apply {
        views.addAll(searchable)
    }

    override fun bind(binding: DependencyBinding<*>) = base.bind(binding)

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? {
        return views.firstNotNullOfOrNull { view -> view.find(type, tag) }
    }

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> {
        return views.map { view -> view.findAll(type) }.flatten()
    }
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