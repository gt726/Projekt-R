package com.example.projektr.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.projektr.R
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.FinishedWorkoutExercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise_detail)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // dohvati podatke iz intenta
        val exerciseName = intent.getStringExtra("exercise_name") ?: ""

        // dohvati elemente
        val naslov = findViewById<TextView>(R.id.exercise_name)
        val maxWeight = findViewById<TextView>(R.id.max_weight_value)
        val maxSetVolume = findViewById<TextView>(R.id.max_set_value)
        val maxSessionVolume = findViewById<TextView>(R.id.max_session_value)

        // postavi naslov
        naslov.text = exerciseName

        // inicijaliziraj bazu
        db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val entries = db.finishedWorkoutDao().getAllEntriesForExercise(exerciseName)
            val maxWeightValue: String
            val maxSetValue: String
            val maxSessionValue: String

            if (entries.isNotEmpty()) {
                maxWeightValue = getMaxWeight(entries).toString()
                maxSetValue = getBestSet(entries)
                maxSessionValue = getBestSession(entries)


            } else {
                maxWeightValue = "---"
                maxSetValue = "--- kg x --- reps"
                maxSessionValue = "--- kg"
            }
            maxWeight.text = "$maxWeightValue kg"
            maxSetVolume.text = "$maxSetValue"
            maxSessionVolume.text = "$maxSessionValue"
        }

        // max weight,max set volume, max session volume
        // graf za max weight, best set volume, session volume
    }

    private fun getMaxWeight(entries: List<FinishedWorkoutExercise>): Float {
        return entries.maxOfOrNull {
            it.weights.split(",").filter { w -> w.isNotEmpty() }.maxOfOrNull { w -> w.toFloat() }
                ?: 0f
        } ?: 0f
    }

    private fun getBestSet(entries: List<FinishedWorkoutExercise>): String {
        var bestSet = ""
        var bestWeight = 0f
        for (entry in entries) {
            val weights = entry.weights.split(",").mapNotNull { it.toFloatOrNull() }
            val reps = entry.reps.split(",").mapNotNull { it.toIntOrNull() }
            for (i in weights.indices) {
                if (i < reps.size) {
                    val score = weights[i] * reps[i]
                    if (score > bestWeight) {
                        bestWeight = score
                        bestSet = "${weights[i]} kg x ${reps[i]} reps"
                    }
                }
            }
        }
        return bestSet
    }

    private fun getBestSession(entries: List<FinishedWorkoutExercise>): String {
        // grupiraj setove po workout ID-u
        val sessionVolumes = mutableMapOf<Int, Float>()

        for (entry in entries) {
            // razdvoji tezine i broj ponavljanja te dohvati workout ID
            val weights = entry.weights.split(",").mapNotNull { it.toFloatOrNull() }
            val reps = entry.reps.split(",").mapNotNull { it.toIntOrNull() }
            val workoutId = entry.workoutId

            var sessionVolume = 0f
            for (i in weights.indices) {
                if (i < reps.size) {
                    sessionVolume += weights[i] * reps[i]
                }
            }

            // dodaj ukupnom zbroju za taj workout ID
            sessionVolumes[workoutId] = sessionVolumes.getOrDefault(workoutId, 0f) + sessionVolume
        }

        // dohvati workout ID s najvecim volumenom
        val bestWorkoutId = sessionVolumes.maxByOrNull { it.value }?.key
        val bestVolume = sessionVolumes[bestWorkoutId] ?: 0f

        return if (bestWorkoutId != null) {
            "$bestVolume kg"
        } else {
            "---"
        }
    }

}