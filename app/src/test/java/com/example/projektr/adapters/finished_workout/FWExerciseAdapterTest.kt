package com.example.projektr.adapters.finished_workout

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class FWExerciseAdapterTest {

    // test parsianja i formatiranja tezina i broja ponavljanja vjezbe
    @Test
    fun `parseWeightAndReps parses correctly and removes blanks`() {
        val weights = "20,30, ,50,"
        val reps = "10,12,,15,"

        val (parsedWeights, parsedReps) = parseWeightAndReps(weights, reps)

        assertEquals(listOf("20", "30", "50"), parsedWeights)
        assertEquals(listOf("10", "12", "15"), parsedReps)
    }
}