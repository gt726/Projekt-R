package com.example.projektr.data

import java.io.Serializable

data class ExerciseWithSets(
    val exercise: Exercise,
    var numberOfSets: Int
) : Serializable