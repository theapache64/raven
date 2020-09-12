package com.theapache64.raven.utils

import com.theapache64.expekt.should
import junit.framework.TestCase
import org.junit.Test

/**
 * Created by theapache64 : Sep 12 Sat,2020 @ 12:06
 */
class CycleListTest : TestCase() {

    @Test
    fun testNext() {
        val list = listOf("A", "B", "C").toCycleList()
        list.next().should.equal("A")
        list.next().should.equal("B")
        list.next().should.equal("C")
        list.next().should.equal("A")
    }

    @Test
    fun testPrev() {
        val list = listOf("A", "B", "C").toCycleList()
        list.prev().should.equal("C")
        list.prev().should.equal("B")
        list.prev().should.equal("A")
        list.prev().should.equal("C")
    }

    @Test
    fun testPrevNext() {
        val list = listOf("A", "B", "C").toCycleList()
        list.prev().should.equal("C")
        list.next().should.equal("A")
        list.next().should.equal("B")
        list.prev().should.equal("A")
        list.prev().should.equal("C")
        list.prev().should.equal("B")
        list.prev().should.equal("A")
        list.prev().should.equal("C")
        list.next().should.equal("A")
    }

    @Test
    fun testPrevNextSingle() {
        val list = listOf("A").toCycleList()
        list.prev().should.equal("A")
        list.next().should.equal("A")
        list.next().should.equal("A")
        list.prev().should.equal("A")
        list.prev().should.equal("A")
        list.prev().should.equal("A")
        list.prev().should.equal("A")
        list.prev().should.equal("A")
        list.next().should.equal("A")
    }
}