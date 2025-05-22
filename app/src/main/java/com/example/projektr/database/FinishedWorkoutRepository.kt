package com.example.projektr.database

import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkout
import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkoutExercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

open class FinishedWorkoutRepository(
    protected open val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    companion object {
        var instance: FinishedWorkoutRepository? = null
    }

    protected open val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()


    // dohvati ID trenutnog korisnika
    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw Exception("User not authenticated")
    }


    // dohvati sve zavrsene treninge za trenutnog korisnika
    suspend fun getFinishedWorkouts(): List<FinishedWorkout> {
        val userId = getCurrentUserId()

        return db.collection("users")
            .document(userId)
            .collection("finished_workouts")
            .get()
            .await()
            .documents
            .map { doc ->
                doc.toObject(FinishedWorkout::class.java)!!
            }
    }


    // dohvati sve vjezbe za zadani finished workout
    suspend fun getExercisesForFinishedWorkout(finishedWorkoutId: String): List<FinishedWorkoutExercise> {
        val userId = getCurrentUserId()

        return db.collection("users")
            .document(userId)
            .collection("finished_workouts")
            .document(finishedWorkoutId)
            .collection("exercises")
            .get()
            .await()
            .documents
            .map { it.toObject(FinishedWorkoutExercise::class.java)!! }
    }


    // dodaj novi finished workout u bazu
    suspend fun insertFinishedWorkout(finishedWorkout: FinishedWorkout): String {
        val userId = getCurrentUserId()

        val docRef = db.collection("users")
            .document(userId)
            .collection("finished_workouts")
            .document() // automatski generira novi ID

        val generatedId = docRef.id

        // postavljamo ID u template prije spremanja
        val finishedWorkoutWithId = finishedWorkout.copy(
            id = generatedId,
            userId = userId
        )

        docRef.set(finishedWorkoutWithId).await()

        return generatedId
    }


    // dodaj vjezbe u finished workout
    suspend fun insertExercises(
        finishedWorkoutId: String,
        exercises: List<FinishedWorkoutExercise>
    ) {
        val batch = db.batch()
        val userId = getCurrentUserId()

        exercises.forEach { exercise ->
            val exerciseRef = db.collection("users")
                .document(userId)
                .collection("finished_workouts")
                .document(finishedWorkoutId)
                .collection("exercises")
                .document() // automatski generira novi ID

            val generatedId = exerciseRef.id

            batch.set(
                exerciseRef, exercise.copy(
                    id = generatedId,
                    workoutId = finishedWorkoutId,
                )
            )
        }

        batch.commit().await()
    }


    // izbrisi finished workout
    suspend fun deleteFinishedWorkout(finishedWorkoutId: String) {
        val userId = getCurrentUserId()

        val workoutRef = db.collection("users")
            .document(userId)
            .collection("finished_workouts")
            .document(finishedWorkoutId)

        // dohvati sve vježbe za workout
        val exercises = workoutRef
            .collection("exercises")
            .get()
            .await()

        val batch = db.batch()

        // izbrisi sve vjezbe
        exercises.documents.forEach { doc ->
            batch.delete(doc.reference)
        }

        // izbrisi template
        batch.delete(workoutRef)

        // izvrsi batch operacije
        batch.commit().await()
    }


    // dohvati sve setove za odredenu vjezbu
    suspend fun getAllEntriesForExercise(exerciseName: String): List<FinishedWorkoutExercise> {
        val userId = getCurrentUserId()

        val finishedWorkoutsRef = db.collection("users")
            .document(userId)
            .collection("finished_workouts")

        val result = mutableListOf<FinishedWorkoutExercise>()

        val finishedWorkouts = finishedWorkoutsRef.get().await()

        for (workout in finishedWorkouts.documents) {
            val exercises = workout.reference
                .collection("exercises")
                .whereEqualTo("exerciseName", exerciseName)
                .get()
                .await()

            exercises.documents.mapNotNullTo(result) { it.toObject(FinishedWorkoutExercise::class.java) }
        }

        return result
    }
}