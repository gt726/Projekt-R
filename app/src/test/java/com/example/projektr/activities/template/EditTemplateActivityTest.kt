package com.example.projektr.activities.template

import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.database.TemplateRepository
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class EditTemplateActivityTest {

    private lateinit var repository: TemplateRepository

    @Before
    fun setUp() {
        //repository = mock()
    }

    // test pretvorbe u serializable listu
    @Test
    fun `mapToList should convert list correctly`() {
        val input = listOf(
            ExerciseWithSets(Exercise("Push Up"), 3),
            ExerciseWithSets(Exercise("Squat"), 5)
        )

        val output = mapToList(input)
        assertEquals(2, output.size)
        assertEquals("Push Up", output[0].exercise.name)
        assertEquals(3, output[0].numberOfSets)
    }
}