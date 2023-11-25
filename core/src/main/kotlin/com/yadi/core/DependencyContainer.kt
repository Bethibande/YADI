package com.yadi.core

import com.yadi.core.bind.DependencyBinding

class DependencyContainer: Searchable {

    // TODO: make edit thread-safe
    private val views = mutableListOf<DependencyView>()

    fun addView(view: DependencyView) {
        views.add(view)
    }

    fun withView(view: DependencyView) = with { addView(view) }

    fun removeView(view: DependencyView) {
        views.remove(view)
    }

    override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = views.map { view -> view.findAll(type) }
        .flatten()

    override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = views.firstNotNullOfOrNull { view ->
        view.find(type, tag)
    }

}