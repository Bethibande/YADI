package com.yadi.core.search

import com.yadi.core.DependencyList
import com.yadi.core.binding.DependencyBinding
import com.yadi.core.inject.Bindable
import com.yadi.core.inject.Injectable
import com.yadi.core.inject.bind
import kotlin.test.assertIs
import org.junit.jupiter.api.Test

class InstanceProviderTest {

    interface Service

    class ServiceDefault: Service
    class ServiceA: Service
    class ServiceB: Service

    private fun view() = DependencyList()
    private fun provider(view: DependencyList) = object: InstanceProvider, Injectable {
        override fun bind(binding: DependencyBinding<*>): Bindable = apply { view.bind(binding) }

        override fun <T> find(type: Class<T>, tag: Any?): DependencyBinding<T>? = view.find(type, tag)
        override fun <T> findAll(type: Class<T>): List<DependencyBinding<T>> = view.findAll(type)
    }

    @Test
    fun test1() {
        val view = view()
        val provider = provider(view)

        provider.bind<Service> {
            singleton { ServiceDefault() }
            singleton { ServiceA() } tagged "A"
            singleton("B") { ServiceB() }
            factory("C") { ServiceB() }
            factory { ServiceB() } tagged "C"
        }

        assertIs<ServiceDefault>(provider.instance<Service>())
        assertIs<ServiceA>(provider.instance<Service>("A"))
        assertIs<ServiceB>(provider.instance<Service>("B"))
    }

}