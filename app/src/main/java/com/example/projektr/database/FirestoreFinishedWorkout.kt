package com.example.projektr.database

class FirestoreFinishedWorkout {

    data class FinishedWorkout(
        var id: String = "",
        val workoutName: String = "",
        val date: Long = 0L,
        val userId: String = ""
    )


    data class FinishedWorkoutExercise(
        var id: String = "",
        val workoutId: String = "",
        val exerciseName: String = "",
        val numberOfSets: Int = 0,
        val weights: String = "",
        val reps: String = ""
    )
}