package com.example.projektr.activities

import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkoutExercise
import org.junit.jupiter.api.Assertions.*
import org.junit.Test

class ExerciseDetailActivityTest {

    // testiranje pronalska najvece koristene tezine za neku vjezbu
    @Test
    fun `getMaxWeight returns highest weight`() {
        val entries = listOf(
            FinishedWorkoutExercise(
                weights = "40,50,60",
                reps = "10,10,10",
                workoutId = "w1"
            ),
            FinishedWorkoutExercise(weights = "55,80", reps = "5,5", workoutId = "w2")
        )
        val result = getMaxWeight(entries)
        assertEquals(80f, result)
    }

    // testiranje pronalska najbolje serije (najveci volumen) za neku vjezbu
    @Test
    fun `getBestSet returns correct set with highest volume`() {
        val entries = listOf(
            FinishedWorkoutExercise(weights = "50,60", reps = "10,5", workoutId = "w1"), // 500, 300
            FinishedWorkoutExercise(weights = "30,40", reps = "12,15", workoutId = "w2") // 360, 600
        )
        val result = getBestSet(entries)
        assertEquals("40.0 kg x 15 reps", result)
    }

    // testiranje pronalaska sesije s najvecim ukupnim volumenom
    @Test
    fun `getBestSession returns session with highest volume`() {
        val entries = listOf(
            FinishedWorkoutExercise(weights = "50,60", reps = "10,5", workoutId = "w1"), // 800
            FinishedWorkoutExercise(weights = "30,40", reps = "12,15", workoutId = "w2"), // 960
            FinishedWorkoutExercise(weights = "20", reps = "5", workoutId = "w2") // +100 = 1060
        )
        val result = getBestSession(entries)
        assertEquals("1060.0 kg", result)
    }

    // testiranje navedenih funkcija s praznim unosom
    @Test
    fun `functions return defaults on empty input`() {
        val entries = emptyList<FinishedWorkoutExercise>()
        assertEquals(0f, getMaxWeight(entries))
        assertEquals("", getBestSet(entries))
        assertEquals("---", getBestSession(entries))
    }
}