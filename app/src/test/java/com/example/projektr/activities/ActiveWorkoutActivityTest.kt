package com.example.projektr.activities

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ActiveWorkoutActivityTest {

    //testiranje parsiranja praznog unosa
    @Test
    fun `returns empty list when all sets are empty`() {
        val input = mapOf(
            "Push Ups" to listOf(Pair("", ""), Pair("", ""))
        )
        val result = formatExercises(input, "workout123")

        assertTrue(result.isEmpty())
    }

    // funkcija treba ignorirati sve nepotpune setove
    @Test
    fun `ignores sets with empty weight or reps`() {
        val input = mapOf(
            "Squats" to listOf(
                Pair("60", "10"),
                Pair("", "8"),
                Pair("65", "")
            )
        )
        val result = formatExercises(input, "workout123")

        assertEquals(1, result.size)
        val exercise = result[0]
        assertEquals("Squats", exercise.exerciseName)
        assertEquals("60,", exercise.weights)
        assertEquals("10,", exercise.reps)
        assertEquals(1, exercise.numberOfSets)
    }

    // testiranje ispravnog stvaranja FinishedWorkoutExercise objekata
    @Test
    fun `creates correct FinishedWorkoutExercise objects`() {
        val input = mapOf(
            "Bench Press" to listOf(
                Pair("80", "8"),
                Pair("85", "6")
            ),
            "Deadlift" to listOf(
                Pair("100", "5")
            )
        )

        val result = formatExercises(input, "w123")

        assertEquals(2, result.size)

        val bench = result.find { it.exerciseName == "Bench Press" }
        assertNotNull(bench)
        assertEquals("80,85,", bench!!.weights)
        assertEquals("8,6,", bench.reps)
        assertEquals(2, bench.numberOfSets)
        assertEquals("w123", bench.workoutId)

        val deadlift = result.find { it.exerciseName == "Deadlift" }
        assertNotNull(deadlift)
        assertEquals("100,", deadlift!!.weights)
        assertEquals("5,", deadlift.reps)
        assertEquals(1, deadlift.numberOfSets)
    }
}