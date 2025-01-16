package com.example.projektr.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FinishedWorkoutDao {

    // stvori novi zavrseni workout
    @Insert
    suspend fun insertWorkout(workout: FinishedWorkout): Long

    // stvori vjezbe za zavrseni workout
    @Insert
    suspend fun insertWorkoutExercises(exercises: List<FinishedWorkoutExercise>)

    // dohvati sve zavrsene workoutove
    @Query("SELECT * FROM FinishedWorkout")
    suspend fun getWorkouts(): List<FinishedWorkout>

    // dohvati zavrseni workout po id-u
    @Query("SELECT * FROM FinishedWorkout WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Int): FinishedWorkout

    // dohvati sve vjezbe za zavrseni workout
    @Query("SELECT * FROM FinishedWorkoutExercise WHERE workoutId = :workoutId")
    suspend fun getExercisesForWorkout(workoutId: Int): List<FinishedWorkoutExercise>

    // izbrisi sve vjezbe za zavrseni workout
    @Query("DELETE FROM FinishedWorkoutExercise WHERE workoutId = :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: Int)

    // izbrisi zavrseni workout po id-u
    @Query("DELETE FROM FinishedWorkout WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Int)

    // izbrisi zavrseni workout i povezane vjezbe
    @Transaction
    suspend fun deleteWorkoutAndExercises(workoutId: Int) {
        deleteExercisesByWorkoutId(workoutId)
        deleteWorkoutById(workoutId)
    }

    // izbrisi sve vjezbe
    @Query("DELETE FROM FinishedWorkoutExercise")
    suspend fun deleteAllExercises()

    // izbrisi sve zavrsene workoutove
    @Query("DELETE FROM FinishedWorkout")
    suspend fun deleteAllWorkouts()

    // ocisti bazu
    @Transaction
    suspend fun clearDatabase() {
        deleteAllExercises()
        deleteAllWorkouts()
    }
}
