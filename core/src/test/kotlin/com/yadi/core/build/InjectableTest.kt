package com.yadi.core.build

import com.yadi.core.DependencyView
import kotlin.test.assertIs
import org.junit.jupiter.api.Test

class InjectableTest {

    interface Service
    class ClassA: Service
    class ClassB: Service
    class ClassC: Service

    private fun view() = DependencyView()
    private fun injector(view: DependencyView) = object: Injectable {
        override fun view(): DependencyView = view
    }

    @Test
    fun test1() {
        val view = view()
        val injector = injector(view)

        injector.bind<Service> {
            singleton { ClassA() } tagged "a"
            factory { ClassB() } tagged "b"
            factory { ClassC() }
        }

        assertIs<ClassA>(view.find(Service::class.java, "a")?.get())
        assertIs<ClassB>(view.find(Service::class.java, "b")?.get())
        assertIs<ClassC>(view.find(Service::class.java, null)?.get())
    }

}