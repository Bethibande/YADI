package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.inject.Bindable
import com.yadi.core.search.Searchable
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A mutable list of bindings,
 * read and write operations are synchronized using a [ReentrantReadWriteLock]
 */
class DependencyList(private val allowDuplicates: Boolean): Bindable, Searchable {

    private val bindings = HashMap<Class<*>, MutableList<DependencyBinding<*>>>()
    private val lock = ReentrantReadWriteLock()

    constructor(): this(false)

    override fun bind(binding: DependencyBinding<*>) = apply {
        lock.write {
            val bindings = this.bindings.computeIfAbsent(binding.type) { mutableListOf() }
            if (!allowDuplicates) bindings.filter { it.tag == binding.tag }.let { duplicates -> bindings.removeAll(duplicates) }
            bindings.add(binding)
        }
    }

    override fun bind(bindings: List<DependencyBinding<*>>) = super.bind(bindings) as DependencyList

    override fun bind(vararg bindings: DependencyBinding<*>) = super.bind(*bindings) as DependencyList

    @Suppress("UNCHECKED_CAST")
    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = lock.read {
        val bindings = this.bindings[type] ?: return@read null
        return bindings.firstOrNull { binding -> binding.tag == tag } as? DependencyBinding<T>
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = lock.read {
        bindings[type] as? List<DependencyBinding<T>> ?: emptyList()
    }

    override fun findAll(): List<DependencyBinding<*>> = bindings.values.flatten()

}