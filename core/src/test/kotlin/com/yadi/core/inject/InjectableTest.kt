package com.yadi.core.inject

import com.yadi.core.DependencyList
import com.yadi.core.binding.DependencyBinding
import kotlin.test.assertIs
import org.junit.jupiter.api.Test

class InjectableTest {

    interface Service
    class ServiceA: Service
    class ServiceB: Service
    class ServiceC: Service

    private fun injectable(): Pair<DependencyList, Injectable> {
        val view = DependencyList()
        val injectable = object: Injectable {
            override fun bind(binding: DependencyBinding<*>): Bindable = apply { view.bind(binding) }
        }

        return view to injectable
    }

    @Test
    fun test1() {
        val (view, injectable) = injectable()

        injectable.bind<Service> {
            singleton { ServiceA() }
            singleton("b") { ServiceB() }
            singleton { ServiceC() } tagged "c"
        }

        assertIs<ServiceA>(view.find(Service::class.java, null)?.get())
        assertIs<ServiceB>(view.find(Service::class.java, "b")?.get())
        assertIs<ServiceC>(view.find(Service::class.java, "c")?.get())
    }

    @Test
    fun test2() {
        val (view, injectable) = injectable()

        injectable.bind<Service> {
            factory { ServiceA() }
            factory("b") { ServiceB() }
            factory { ServiceC() } tagged "c"
        }

        assertIs<ServiceA>(view.find(Service::class.java, null)?.get())
        assertIs<ServiceB>(view.find(Service::class.java, "b")?.get())
        assertIs<ServiceC>(view.find(Service::class.java, "c")?.get())
    }

    @Test
    fun test3() {
        val (view, injectable) = injectable()

        injectable.bind<Service> {
            singleton { ServiceA() }
            singleton("b") { ServiceB() }
            singleton { ServiceC() } tagged "c"
            singleton("d") { ServiceB() }
        }

        assertIs<ServiceA>(view.find(Service::class.java, null)?.get())
        assertIs<ServiceB>(view.find(Service::class.java, "b")?.get())
        assertIs<ServiceC>(view.find(Service::class.java, "c")?.get())
    }

    @Test
    fun test4() {
        val (view, injectable) = injectable()

        injectable.bind<Service> {
            factory { ServiceA() }
            factory("b") { ServiceB() }
            factory { ServiceC() } tagged "c"
            factory("d") { ServiceB() }
        }

        assertIs<ServiceA>(view.find(Service::class.java, null)?.get())
        assertIs<ServiceB>(view.find(Service::class.java, "b")?.get())
        assertIs<ServiceC>(view.find(Service::class.java, "c")?.get())
    }

    @Test
    fun test5() {
        val (view, injectable) = injectable()

        injectable.bindSingleton { ServiceA() }
        injectable.bindSingleton { ServiceB() }
        injectable.bindSingleton { ServiceC() }

        assertIs<ServiceA>(view.find(ServiceA::class.java, null)?.get())
        assertIs<ServiceB>(view.find(ServiceB::class.java, null)?.get())
        assertIs<ServiceC>(view.find(ServiceC::class.java, null)?.get())
    }

    @Test
    fun test6() {
        val (view, injectable) = injectable()

        injectable.bindFactory { ServiceA() }
        injectable.bindFactory { ServiceB() }
        injectable.bindFactory { ServiceC() }

        assertIs<ServiceA>(view.find(ServiceA::class.java, null)?.get())
        assertIs<ServiceB>(view.find(ServiceB::class.java, null)?.get())
        assertIs<ServiceC>(view.find(ServiceC::class.java, null)?.get())
    }

    @Test
    fun test7() {
        val (view, injectable) = injectable()

        injectable.bindSingleton<Service> { ServiceA() }
        injectable.bindSingleton<Service>("b") { ServiceB() }

        assertIs<ServiceA>(view.find(Service::class.java, null)?.get())
        assertIs<ServiceB>(view.find(Service::class.java, "b")?.get())
    }

    @Test
    fun test8() {
        val (view, injectable) = injectable()

        injectable.bindFactory<Service> { ServiceA() }
        injectable.bindFactory<Service>("b") { ServiceB() }

        assertIs<ServiceA>(view.find(Service::class.java, null)?.get())
        assertIs<ServiceB>(view.find(Service::class.java, "b")?.get())
    }

}