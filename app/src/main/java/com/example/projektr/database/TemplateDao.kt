package com.example.projektr.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TemplateDao {
    // stvori novi template
    @Insert
    suspend fun insertTemplate(template: Template): Long

    // stvori nove vjezbe za template
    @Insert
    suspend fun insertExercises(exercises: List<TemplateExercise>)

    // dohvati sve templateove
    @Query("SELECT * FROM Template")
    suspend fun getTemplates(): List<Template>

    // dohvati sve vjezbe za template
    @Query("SELECT * FROM TemplateExercise WHERE templateId = :templateId")
    suspend fun getExercisesForTemplate(templateId: Int): List<TemplateExercise>

    // preimenuj template
    @Query("UPDATE Template SET name = :name WHERE id = :templateId")
    suspend fun renameTemplate(templateId: Int, name: String)

    // izbrisi template
    @Query("DELETE FROM Template WHERE id = :templateId")
    suspend fun deleteTemplate(templateId: Int)

    // izbrisi sve vjezbe za zadani template
    @Query("DELETE FROM TemplateExercise WHERE templateId = :templateId")
    suspend fun deleteExercisesForTemplate(templateId: Int)

    // izbrisi template i sve vjezbe za taj template
    @androidx.room.Transaction
    suspend fun deleteTemplateAndExercises(templateId: Int) {
        deleteExercisesForTemplate(templateId)
        deleteTemplate(templateId)
    }
}
