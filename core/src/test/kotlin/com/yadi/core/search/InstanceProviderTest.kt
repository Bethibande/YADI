package com.yadi.core.search

import com.yadi.core.DependencyList
import com.yadi.core.binding.DependencyBinding
import com.yadi.core.binding.DependencyProvider
import com.yadi.core.inject.Bindable
import com.yadi.core.inject.Injectable
import com.yadi.core.inject.bind
import kotlin.test.assertIs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InstanceProviderTest {

    interface Service
    
    class ServiceA: Service
    class ServiceB: Service
    
    companion object {
        private val BINDING_1  = DependencyBinding(ServiceA::class.java, null, DependencyProvider.singleton { ServiceA() })
        private val BINDING_2  = DependencyBinding(ServiceA::class.java, "a", DependencyProvider.singleton { ServiceA() })
        private val BINDING_3  = DependencyBinding(ServiceA::class.java, "b", DependencyProvider.singleton { ServiceA() })
        private val BINDING_4  = DependencyBinding(ServiceB::class.java, null, DependencyProvider.factory { ServiceB() })
        private val BINDING_5  = DependencyBinding(ServiceB::class.java, "a", DependencyProvider.factory { ServiceB() })
        private val BINDING_6  = DependencyBinding(ServiceB::class.java, "b", DependencyProvider.factory { ServiceB() })
        private val BINDING_7  = DependencyBinding(Service::class.java, "a", DependencyProvider.singleton { ServiceA() })
        private val BINDING_8  = DependencyBinding(Service::class.java, "b", DependencyProvider.factory { ServiceA() })
        private val BINDING_9  = DependencyBinding(Service::class.java, "c", DependencyProvider.singleton { ServiceB() })
        private val BINDING_10 = DependencyBinding(Service::class.java, "d", DependencyProvider.factory { ServiceB() })

        private val VIEW = DependencyList().bind(BINDING_1, BINDING_2, BINDING_3, BINDING_4, BINDING_5, BINDING_6)
            .bind(BINDING_7, BINDING_8, BINDING_9, BINDING_10)

        private val PROVIDER = object: InstanceProvider {
            override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = VIEW.find(type, tag)
            override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = VIEW.findAll(type)
            override fun findAll(): List<DependencyBinding<*>> = VIEW.findAll()
        }
    }

    @Test
    fun test1() {
        assertIs<ServiceA>(PROVIDER.instance<ServiceA>())
        assertIs<ServiceA>(PROVIDER.instance<ServiceA>("a"))
        assertIs<ServiceA>(PROVIDER.instance<ServiceA>("b"))
        assertIs<ServiceB>(PROVIDER.instance<ServiceB>())
        assertIs<ServiceB>(PROVIDER.instance<ServiceB>("a"))
        assertIs<ServiceB>(PROVIDER.instance<ServiceB>("b"))
    }

    @Test
    fun test2() {
        assertThrows<NoSuchElementException> { PROVIDER.instance<ServiceA>("c") }
        assertThrows<NoSuchElementException> { PROVIDER.instance<ServiceB>("c") }
        assertThrows<NoSuchElementException> { PROVIDER.instance<Service>("e") }
    }

    @Test
    fun test3() {
        assertIs<ServiceA>(PROVIDER.instance<Service>("a"))
        assertIs<ServiceA>(PROVIDER.instance<Service>("b"))
        assertIs<ServiceB>(PROVIDER.instance<Service>("c"))
        assertIs<ServiceB>(PROVIDER.instance<Service>("d"))
    }

}