package com.yadi.core

import com.yadi.core.bind.DependencyBinding
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A mutable view of dependency bindings,
 * synchronized using a [ReentrantReadWriteLock]
 */
class DependencyView: Searchable {

    private var bindings = HashMap<Class<*>, MutableList<DependencyBinding<*>>>()
    private val lock = ReentrantReadWriteLock()

    fun bind(binding: DependencyBinding<*>) = with {
        lock.write {
            val list = bindings.computeIfAbsent(binding.type) { mutableListOf() }
            list.add(binding)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? =lock.read {
        val bindings = this.bindings[type] ?: return null
        bindings.firstOrNull { binding -> binding.tag == tag } as? DependencyBinding<T>
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = lock.read {
        this.bindings[type] as? List<DependencyBinding<T>> ?: emptyList()
    }

}