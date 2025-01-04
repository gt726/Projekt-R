package com.example.projektr.database

import androidx.room.*

@Entity
data class Template(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

@Entity
data class TemplateExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val templateId: Int,
    val exerciseName: String,
    val numberOfSets: Int
)
