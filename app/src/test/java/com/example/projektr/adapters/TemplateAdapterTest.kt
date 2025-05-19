package com.example.projektr.adapters

import com.example.projektr.database.FirestoreTemplate
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class TemplateAdapterTest {

    // test formatiranja teksta za obavljenu vjezbu
    @Test
    fun `formatExerciseText returns correct format`() {
        val exercise = FirestoreTemplate.TemplateExercise("1", "template1", "Push Up", 3)
        val result = formatExerciseText(exercise)
        assertEquals("Push Up - 3 sets", result)
    }

    // test formatiranja teksta za obavljenu vjezbu s praznim imenom
    @Test
    fun `formatExerciseText returns correct format with empty name`() {
        val exercise = FirestoreTemplate.TemplateExercise("1", "template1", "", 3)
        val result = formatExerciseText(exercise)
        assertEquals(" - 3 sets", result)
    }

    // test formatiranja teksta za obavljenu vjezbu s praznim brojem setova
    @Test
    fun `formatExerciseText returns correct format with zero sets`() {
        val exercise = FirestoreTemplate.TemplateExercise("1", "template1", "Push Up", 0)
        val result = formatExerciseText(exercise)
        assertEquals("Push Up - 0 sets", result)
    }
}