package com.yadi.core.build

import com.yadi.core.DependencyView
import com.yadi.core.Searchable
import kotlin.test.assertIs
import org.junit.jupiter.api.Test

class InstanceProviderTest {

    interface Service

    class ServiceDefault: Service
    class ServiceA: Service
    class ServiceB: Service

    private fun view() = DependencyView()
    private fun provider(view: DependencyView) = object: InstanceProvider, Injectable {
        override fun container(): Searchable = view
        override fun view(): DependencyView = view
    }

    @Test
    fun test1() {
        val view = view()
        val provider = provider(view)

        provider.bind<Service> {
            singleton { ServiceDefault() }
            singleton { ServiceA() } tagged "A"
            singleton { ServiceB() } tagged "B"
        }

        assertIs<ServiceDefault>(provider.instance<Service>())
        assertIs<ServiceA>(provider.instance<Service>("A"))
        assertIs<ServiceB>(provider.instance<Service>("B"))
    }

}