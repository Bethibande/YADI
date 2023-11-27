package com.yadi.core

import com.yadi.core.binding.DependencyBinding
import com.yadi.core.binding.DependencyProvider
import com.yadi.core.inject.bind
import com.yadi.core.inject.bindFactory
import com.yadi.core.inject.bindSingleton
import com.yadi.core.search.instance
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.TimeUnit
import kotlin.test.assertContains

class ModuleTest {

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

        private val VIEW_1 = DependencyList()
            .bind(BINDING_1)
            .bind(BINDING_2)
            .bind(BINDING_3)
            .bind(BINDING_4)
        private val VIEW_2 = DependencyList()
            .bind(BINDING_5)
            .bind(BINDING_6)
            .bind(BINDING_7)

        private val MODULE = Module()
            .inject(VIEW_1)
            .inject(VIEW_2)
    }

    @Test
    fun test1() {
        assertEquals(BINDING_1, MODULE.find(String::class.java, null))
        assertEquals(BINDING_2, MODULE.find(Int::class.java, "abc"))
        assertEquals(BINDING_3, MODULE.find(Boolean::class.java, 1))
        assertEquals(BINDING_4, MODULE.find(Boolean::class.java, 2))
        assertEquals(BINDING_5, MODULE.find(String::class.java, "World"))
        assertEquals(BINDING_6, MODULE.find(Int::class.java, 1))
        assertEquals(BINDING_7, MODULE.find(Int::class.java, 2))
    }

    @Test
    fun test2() {
        val bindings = MODULE.findAll(String::class.java)
        assertEquals(2, bindings.size)
        assertEquals(BINDING_1, bindings[0])
        assertEquals(BINDING_5, bindings[1])
    }

    @Test
    fun test3() {
        val bindings = MODULE.findAll(Int::class.java)
        assertEquals(3, bindings.size)
        assertEquals(BINDING_2, bindings[0])
        assertEquals(BINDING_6, bindings[1])
        assertEquals(BINDING_7, bindings[2])
    }

    @Test
    fun test4() {
        val bindings = MODULE.findAll(Boolean::class.java)
        assertEquals(2, bindings.size)
        assertEquals(BINDING_3, bindings[0])
        assertEquals(BINDING_4, bindings[1])
    }

    @Test
    fun test5() {
        val core = Module().bind<String> {
            singleton { "a" }
            singleton { "World" } tagged "name"
        } as Module

        val module = module(core) {
            val name = instance<String>("name")
            bindSingleton("message") { "Hello $name!" }
        }

        assertEquals("Hello World!", module.instance("message"))
    }

    @Test
    fun test6() {
        val core = Module().bind<String> {
            singleton { "a" }
            singleton { "World" } tagged "name"
        } as Module

        val module = module(core) {
            bindSingleton("message") { "Hello ${instance<String>("name")}!" }
        }

        core.inject(module)
        assertEquals("a", core.instance())
        assertEquals("World", core.instance("name"))
        assertEquals("Hello World!", core.instance("message"))
    }

    @Test
    fun test7() {
        val module = module {
            bindSingleton { "abc" }
            bindSingleton { "def" } tagged "a"
        }

        assertEquals("abc", module.instance<String>())
        assertEquals("def", module.instance<String>("a"))
    }

    @Test
    fun test8() {
        val module = module {
            bindFactory { "abc" }
            bindFactory { "def" } tagged "a"
        }

        assertEquals("abc", module.instance<String>())
        assertEquals("def", module.instance<String>("a"))
    }

    @Test
    fun test9() {
        val module = module {
            bindSingleton { "abc" }
        }

        assertEquals("abc", module.instance<String>())
        assertThrows<NoSuchElementException> { module.instance<String>("a") }
    }

    @Test
    fun test10() {
        val module = DefaultContainer()
        assertThrows<IllegalArgumentException> { module.inject(module) }
    }

    @Test
    fun test11() {
        val module1 = DefaultContainer()
        val module2 = DefaultContainer()
        assertThrows<IllegalArgumentException> { module1.inject(module1, module2) }
    }

    @Test
    fun test12() {
        val module1 = DefaultContainer()

        module1.inject(MODULE)

        assertEquals(1, module1.injected().size)
        assertContains(module1.injected(), MODULE)

        assertThrows<IllegalStateException> { MODULE.inject(module1) }
    }

    @Test
    fun test13() {
        val module1 = DefaultContainer()
        val module2 = DefaultContainer()

        module1.inject(MODULE, module2)

        assertEquals(2, module1.injected().size)
        assertContains(module1.injected(), MODULE)
        assertContains(module1.injected(), module2)

        assertThrows<IllegalStateException> { MODULE.inject(module1) }
    }

}