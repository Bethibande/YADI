package com.yadi.core

import com.yadi.core.bind.DependencyBinding

class DependencyView: Searchable {

    // TODO: make thread-safe
    private var bindings = HashMap<Class<*>, MutableList<DependencyBinding<*>>>()

    fun bind(binding: DependencyBinding<*>) = with {
        val list = bindings.computeIfAbsent(binding.type) { mutableListOf() }
        list.add(binding)
    }

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? {
        val bindings = this.bindings[type] ?: return null
        return bindings.firstOrNull { binding -> binding.tag == tag } as? DependencyBinding<T>
    }

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> {
        return this.bindings[type] as? List<DependencyBinding<T>> ?: emptyList()
    }

}