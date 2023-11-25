package com.yadi.core.inject

import com.yadi.core.binding.DependencyBinding

/**
 * Objects implementing this interface support binding objects to itself
 */
interface Bindable {

    fun bind(binding: DependencyBinding<*>): Bindable

}