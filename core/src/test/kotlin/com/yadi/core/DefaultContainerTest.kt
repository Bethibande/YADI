package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.search.instance
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DefaultContainerTest {

    companion object {
        private val BINDING_1 = DependencyBinding(String::class.java, null) { "a" }
        private val BINDING_2 = DependencyBinding(String::class.java, "b") { "b" }
        private val BINDING_3 = DependencyBinding(String::class.java, null) { "c" }
        private val BINDING_4 = DependencyBinding(String::class.java, "d") { "d" }

        private val DEPENDENCIES_1 = DependencyList().bind(BINDING_1, BINDING_2)
        private val DEPENDENCIES_2 = DependencyList().bind(BINDING_3, BINDING_4)

        private val CONTAINER = DefaultContainer().inject(DEPENDENCIES_1, DEPENDENCIES_2)
    }

    @Test
    fun test1() {
        assertEquals("a", CONTAINER.instance<String>())
        assertEquals("b", CONTAINER.instance<String>("b"))
        assertEquals("d", CONTAINER.instance<String>("d"))
        assertThrows<NoSuchElementException> { CONTAINER.instance<String>("yxfgsedf") }
    }

}