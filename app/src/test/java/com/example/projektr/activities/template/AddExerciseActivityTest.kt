package com.example.projektr.activities.template

import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseWithSets
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class AddExerciseActivityTest {
    // test abecednog sortiranja vjezbi
    @Test
    fun `sortExercises should sort by name`() {
        val exercises = listOf(
            Exercise("Biceps Curl"),
            Exercise("Arnold Press"),
            Exercise("Deadlift")
        )
        val sorted = sortExercises(exercises)
        assertEquals("Arnold Press", sorted[0].name)
        assertEquals("Biceps Curl", sorted[1].name)
        assertEquals("Deadlift", sorted[2].name)
    }

    // test pretvorbe u serializable listu
    @Test
    fun `mapToSerializableList should convert list correctly`() {
        val input = listOf(
            ExerciseWithSets(Exercise("Push Up"), 3),
            ExerciseWithSets(Exercise("Squat"), 5)
        )

        val output = mapToSerializableList(input)
        assertEquals(2, output.size)
        assertEquals("Push Up", output[0].exercise.name)
        assertEquals(3, output[0].numberOfSets)
    }
}