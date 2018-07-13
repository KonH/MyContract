package com.konh.mycontract

import com.konh.mycontract.utils.resetToDayStart
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class CalendarUnitTests {
    @Test
    fun resetToDayStart_isCorrectOnBaseCases() {
        val date1 = GregorianCalendar(2018, 1, 1, 8, 10)
        val date2 = GregorianCalendar(2018, 1, 1, 9, 20)
        val start1 = resetToDayStart(date1)
        val start2 = resetToDayStart(date2)
        assertTrue(start1.time.time == start2.time.time)
    }

    @Test
    fun resetToDayStart_isCorrectOnDifferentTimeSegments() {
        val date1 = GregorianCalendar(2018, 1, 1, 8, 10)
        val date2 = GregorianCalendar(2018, 1, 1, 13, 20)
        val start1 = resetToDayStart(date1)
        val start2 = resetToDayStart(date2)
        assertTrue(start1.time.time == start2.time.time)
    }
}
