package com.yadi.core

import com.yadi.core.bind.DependencyBinding
import com.yadi.core.bind.DependencyProvider
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class DependencyContainerTest {

    companion object {
        private val PROVIDER_1 = DependencyProvider { "test" }
        private val PROVIDER_2 = DependencyProvider { 123 }
        private val PROVIDER_3 = DependencyProvider { true }

        private val BINDING_1 = DependencyBinding(String::class.java, null, PROVIDER_1)
        private val BINDING_2 = DependencyBinding(Int::class.java, "abc", PROVIDER_2)
        private val BINDING_3 = DependencyBinding(Boolean::class.java, 1, PROVIDER_3)
        private val BINDING_4 = DependencyBinding(Boolean::class.java, 2, PROVIDER_3)
        private val BINDING_5 = DependencyBinding(String::class.java, "World", PROVIDER_1)
        private val BINDING_6 = DependencyBinding(Int::class.java, 1, PROVIDER_2)
        private val BINDING_7 = DependencyBinding(Int::class.java, 2, PROVIDER_2)

        private val VIEW_1 = DependencyView()
            .bind(BINDING_1)
            .bind(BINDING_2)
            .bind(BINDING_3)
            .bind(BINDING_4)
        private val VIEW_2 = DependencyView()
            .bind(BINDING_5)
            .bind(BINDING_6)
            .bind(BINDING_7)

        private val CONTAINER = DependencyContainer()
            .withView(VIEW_1)
            .withView(VIEW_2)
    }

    @Test
    fun test1() {
        assertEquals(BINDING_1, CONTAINER.find(String::class.java, null))
        assertEquals(BINDING_2, CONTAINER.find(Int::class.java, "abc"))
        assertEquals(BINDING_3, CONTAINER.find(Boolean::class.java, 1))
        assertEquals(BINDING_4, CONTAINER.find(Boolean::class.java, 2))
        assertEquals(BINDING_5, CONTAINER.find(String::class.java, "World"))
        assertEquals(BINDING_6, CONTAINER.find(Int::class.java, 1))
        assertEquals(BINDING_7, CONTAINER.find(Int::class.java, 2))
    }

    @Test
    fun test2() {
        val bindings = CONTAINER.findAll(String::class.java)
        assertEquals(2, bindings.size)
        assertEquals(BINDING_1, bindings[0])
        assertEquals(BINDING_5, bindings[1])
    }

    @Test
    fun test3() {
        val bindings = CONTAINER.findAll(Int::class.java)
        assertEquals(3, bindings.size)
        assertEquals(BINDING_2, bindings[0])
        assertEquals(BINDING_6, bindings[1])
        assertEquals(BINDING_7, bindings[2])
    }

    @Test
    fun test4() {
        val bindings = CONTAINER.findAll(Boolean::class.java)
        assertEquals(2, bindings.size)
        assertEquals(BINDING_3, bindings[0])
        assertEquals(BINDING_4, bindings[1])
    }

}