package com.yadi.core.inject

import com.yadi.core.binding.DependencyBinding

/**
 * Objects implementing this interface support binding objects to itself
 */
interface Bindable {

    fun bind(binding: DependencyBinding<*>): Bindable

    fun bind(bindings: List<DependencyBinding<*>>): Bindable = apply {
        bindings.forEach { binding -> bind(binding) }
    }
    fun bind(vararg bindings: DependencyBinding<*>): Bindable = apply {
        bindings.forEach { binding -> bind(binding) }
    }

}