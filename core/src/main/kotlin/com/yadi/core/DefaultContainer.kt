package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.search.Searchable
import java.util.concurrent.CopyOnWriteArrayList

class DefaultContainer: Container {

    private val views = CopyOnWriteArrayList<Searchable>()

    override fun inject(searchable: Searchable) = apply {
        checkForLoop(searchable)
        views.add(searchable)
    }

    override fun inject(searchable: Collection<Searchable>) = apply {
        checkForLoop(*searchable.toTypedArray())
        views.addAll(searchable)
    }

    override fun inject(vararg searchable: Searchable) = apply {
        checkForLoop(*searchable)
        views.addAll(searchable)
    }

    override fun remove(searchable: Searchable) = apply { views.remove(searchable) }

    override fun remove(searchable: Collection<Searchable>) = apply { views.removeAll(searchable.toSet()) }

    override fun remove(vararg searchable: Searchable) = apply { views.removeAll(searchable.toSet()) }

    override fun injected(): Collection<Searchable> = views

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? {
        return views.firstNotNullOfOrNull { view -> view.find(type, tag) }
    }

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> {
        return views.map { view -> view.findAll(type) }.flatten()
    }
}