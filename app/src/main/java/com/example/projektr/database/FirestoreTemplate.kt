package com.example.projektr.database

class FirestoreTemplate {

    data class Template(
        var id: String = "",
        val name: String = "",
        val userId: String = "" // kako bi povezali korisnika s templateom
    )

    data class TemplateExercise(
        var id: String = "",
        val templateId: String = "",
        val exerciseName: String = "",
        val numberOfSets: Int = 0
    )
}