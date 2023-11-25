package com.yadi.core

import com.yadi.core.bind.DependencyBinding
import com.yadi.core.bind.DependencyProvider
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.jupiter.api.Test

class DependencyViewTest {

    companion object {
        private val PROVIDER_1 = DependencyProvider { "test" }
        private val PROVIDER_2 = DependencyProvider { 123 }
        private val PROVIDER_3 = DependencyProvider { true }

        private val BINDING_1 = DependencyBinding(String::class.java, null, PROVIDER_1)
        private val BINDING_2 = DependencyBinding(Int::class.java, "abc", PROVIDER_2)
        private val BINDING_3 = DependencyBinding(Boolean::class.java, 1, PROVIDER_3)
        private val BINDING_4 = DependencyBinding(Boolean::class.java, 2, PROVIDER_3)

        private val VIEW = DependencyView().bind(BINDING_1)
            .bind(BINDING_2)
            .bind(BINDING_3)
            .bind(BINDING_4)
    }

    @Test
    fun test1() {
        assertEquals(BINDING_1, VIEW.find(String::class.java, null))
        assertEquals(BINDING_2, VIEW.find(Int::class.java, "abc"))
        assertEquals(BINDING_3, VIEW.find(Boolean::class.java, 1))
    }

    @Test
    fun test2() {
        assertNull(VIEW.find(String::class.java, 1))
        assertNull(VIEW.find(Int::class.java, "ab"))
        assertNull(VIEW.find(Boolean::class.java, null))
    }

    @Test
    fun test3() {
        val bindings = VIEW.findAll(Boolean::class.java)

        assertEquals(2, bindings.size)
        assertEquals(BINDING_3, bindings[0])
        assertEquals(BINDING_4, bindings[1])
    }

}