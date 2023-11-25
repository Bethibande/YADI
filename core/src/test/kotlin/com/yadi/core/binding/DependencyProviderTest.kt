package com.yadi.core.binding

import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class DependencyProviderTest {

    @Test
    fun testFactory() {
        val counter = AtomicInteger()
        val provider = DependencyProvider.factory { counter.getAndIncrement() }

        assertEquals(0, provider.provide())
        assertEquals(1, provider.provide())
        assertEquals(2, provider.provide())
    }

    @Test
    fun testSingleton() {
        val counter = AtomicInteger()
        val provider = DependencyProvider.singleton { counter.getAndIncrement() }

        assertEquals(0, provider.provide())
        assertEquals(0, provider.provide())
        assertEquals(0, provider.provide())
    }

}