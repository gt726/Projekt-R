package com.example.projektr.activities

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class FinishedWorkoutActivityTest {
    // test formatiranja datuma
    @Test
    fun `formatDate returns correctly formatted string`() {
        // 1. siječnja 2024.
        val timestamp = 1704067200000L
        val expected = "01-01-2024"
        val actual = formatDate(timestamp)
        assertEquals(expected, actual)
    }
}