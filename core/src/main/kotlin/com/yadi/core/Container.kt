package com.yadi.core

import com.yadi.core.search.InstanceProvider
import com.yadi.core.search.Searchable
import java.util.Stack

/**
 * A container allows you to combine multiple [Searchable] objects,
 * it also implements the [InstanceProvider] for convenience.
 */
interface Container: InstanceProvider {

    fun inject(searchable: Searchable): Container
    fun inject(searchable: Collection<Searchable>): Container
    fun inject(vararg searchable: Searchable): Container

    fun injected(): Collection<Searchable>

    /**
     * Prevents adding a searchable object to the current tree if it already exists.
     * Injecting duplicate objects could cause infinite loops when resolving object instances
     *
     * @param searchable the object to prevent from being added
     * @throws IllegalStateException if the given object already exists in the current tree
     * @see contains
     */
    fun checkForLoop(searchable: Searchable) {
        if (contains(searchable)) {
            throw IllegalStateException("Cannot inject '$searchable', the given object already exists in the current tree.")
        }
    }

    /**
     * Prevents adding any searchable object to the current tree if it already exists.
     * Injecting duplicate objects could cause infinite loops when resolving object instances
     *
     * @param searchable the object to prevent from being added
     * @throws IllegalStateException if the given object already exists in the current tree
     * @see contains
     * @see checkForLoop
     */
    fun checkForLoop(vararg searchable: Searchable) {
        if (contains(*searchable)) {
            throw IllegalStateException("Cannot inject '$searchable', the given object already exists in the current tree.")
        }
    }

    /**
     * Checks if an object has already been injected into the tree,
     * uses depth-first-search to check all children as well
     */
    private fun contains(searchable: Searchable): Boolean {
        val stack = Stack<Searchable>()
        stack.addAll(injected())

        while(stack.isNotEmpty()) {
            val target = stack.pop()

            if (target == searchable) return true

            if (target is Container) {
                target.injected().forEach { child -> stack.push(child) }
            }
        }

        return false
    }

    /**
     * Checks if any of the given objects have been injected into the tree,
     * uses depth-first-search to check all children as well.
     * @see contains
     */
    private fun contains(vararg searchable: Searchable): Boolean {
        if (searchable.isEmpty()) return false

        val stack = Stack<Searchable>()
        stack.addAll(injected())

        while(stack.isNotEmpty()) {
            val target = stack.pop()

            if (searchable.contains(target)) return true

            if (target is Container) {
                target.injected().forEach { child -> stack.push(child) }
            }
        }

        return false
    }

}