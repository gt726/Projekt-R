package com.example.projektr.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TemplateDao {
    @Insert
    suspend fun insertTemplate(template: Template): Long

    @Insert
    suspend fun insertExercises(exercises: List<TemplateExercise>)

    @Query("SELECT * FROM Template")
    suspend fun getTemplates(): List<Template>

    @Query("SELECT * FROM TemplateExercise WHERE templateId = :templateId")
    suspend fun getExercisesForTemplate(templateId: Int): List<TemplateExercise>

    @Query("UPDATE Template SET name = :name WHERE id = :templateId")
    suspend fun renameTemplate(templateId: Int, name: String)
}
