package com.example.projektr.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.projektr.database.FirestoreTemplate.Template
import com.example.projektr.database.FirestoreTemplate.TemplateExercise

class TemplateRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    // dohvati ID trenutnog korisnika
    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw Exception("User not authenticated")
    }


    // dohvati sve templateove za trenutnog korisnika
    suspend fun getTemplates(): List<Template> {
        val userId = getCurrentUserId()

        return db.collection("users")
            .document(userId)
            .collection("templates")
            .get()
            .await()
            .documents
            .map { doc ->
                doc.toObject(Template::class.java)!!
            }
    }

    // dohvati sve vjezbe za zadani template
    suspend fun getExercisesForTemplate(templateId: String): List<TemplateExercise> {
        val userId = getCurrentUserId()

        return db.collection("users")
            .document(userId)
            .collection("templates")
            .document(templateId)
            .collection("exercises")
            .get()
            .await()
            .documents
            .map { it.toObject(TemplateExercise::class.java)!! }
    }


    // dodaj novi template u bazu
    suspend fun insertTemplate(template: Template): String {
        val userId = getCurrentUserId()

        val docRef = db.collection("users")
            .document(userId)
            .collection("templates")
            .document() // automatski generira novi ID

        val generatedId = docRef.id

        // postavljamo ID u template prije spremanja
        val templateWithId = template.copy(
            id = generatedId,
            userId = userId
        )

        docRef.set(templateWithId).await()

        return generatedId
    }


    // dodaj vjezbe u template
    suspend fun insertExercises(
        templateId: String,
        exercises: List<TemplateExercise>
    ) {
        val batch = db.batch()
        val userId = getCurrentUserId()

        exercises.forEach { exercise ->
            val exerciseRef = db.collection("users")
                .document(userId)
                .collection("templates")
                .document(templateId)
                .collection("exercises")
                .document() // automatski generira novi ID

            val generatedId = exerciseRef.id

            batch.set(
                exerciseRef, exercise.copy(
                    id = generatedId,
                    templateId = templateId,
                )
            )
        }

        batch.commit().await()
    }


    // preimenuj template
    suspend fun renameTemplate(templateId: String, newName: String) {
        val userId = getCurrentUserId()

        db.collection("users")
            .document(userId)
            .collection("templates")
            .document(templateId)
            .update("name", newName)
            .await()
    }
    

    // izbrisi template
    suspend fun deleteTemplate(templateId: String) {
        val userId = getCurrentUserId()

        val templateRef = db.collection("users")
            .document(userId)
            .collection("templates")
            .document(templateId)

        // dohvati sve vježbe za template
        val exercises = templateRef
            .collection("exercises")
            .get()
            .await()

        val batch = db.batch()

        // izbrisi sve vjezbe
        exercises.documents.forEach { doc ->
            batch.delete(doc.reference)
        }

        // izbrisi template
        batch.delete(templateRef)

        // izvrsi batch operacije
        batch.commit().await()
    }

    // izbrisi sve vjezbe za zadani template
    suspend fun deleteExercisesForTemplate(templateId: String) {
        val userId = getCurrentUserId()

        val exercisesRef = db.collection("users")
            .document(userId)
            .collection("templates")
            .document(templateId)
            .collection("exercises")

        // izbrisi sve vjezbe
        exercisesRef.get().await().documents.forEach { doc ->
            doc.reference.delete()
        }
    }

}