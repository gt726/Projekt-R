package com.example.projektr.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FinishedWorkout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutName: String,
    val date: Long
)


@Entity
data class FinishedWorkoutExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutId: Int,
    val exerciseName: String,
    val numberOfSets: Int,
    val weights: String,
    val reps: String
)
