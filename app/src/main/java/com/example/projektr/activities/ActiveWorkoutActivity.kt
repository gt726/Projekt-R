package com.example.projektr.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.adapters.active_workout.AWExerciseAdapter
import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkout
import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkoutExercise
import com.example.projektr.database.TemplateRepository
import kotlinx.coroutines.launch

class ActiveWorkoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AWExerciseAdapter

    // popis odabranih vjezbi
    private val exerciseList = mutableListOf<ExerciseWithSets>()

    private val templateRepository = TemplateRepository()
    private val workoutRepository = FinishedWorkoutRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_active_workout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // postavi recycler view
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AWExerciseAdapter(exerciseList)
        recyclerView.adapter = adapter

        // dohvati templateId iz intenta
        val templateId = intent.getStringExtra("TEMPLATE_ID") ?: ""
        Log.d("ActiveWorkoutActivity", "templateId: $templateId")

        // dohvati elemente
        val title = findViewById<TextView>(R.id.title)
        val cancelButton = findViewById<Button>(R.id.cancel_button)
        val finishButton = findViewById<Button>(R.id.finish_button)

        // dohvati vjezbe za taj template iz baze
        lifecycleScope.launch {
            val templates = templateRepository.getTemplates()

            val template = templates.find { it.id == templateId }

            template?.let {
                title.text = template.name
                val exercises = templateRepository.getExercisesForTemplate(templateId)
                exerciseList.clear()
                exerciseList.addAll(exercises.map {
                    ExerciseWithSets(Exercise(it.exerciseName), it.numberOfSets)
                })
            }

            adapter.notifyDataSetChanged()
        }

        // odustani od treninga
        cancelButton.setOnClickListener {
            finish()
        }

        // zavrsi trening
        finishButton.setOnClickListener {
            lifecycleScope.launch {
                val workout = FinishedWorkout(
                    workoutName = title.text.toString(),
                    date = System.currentTimeMillis()
                )
                val workoutId = workoutRepository.insertFinishedWorkout(workout)

                // dohvati sve setove iz adaptera
                val allSetsData = adapter.getAllSetsData()
                var workoutEmpty = true
                val workoutExercises = mutableListOf<FinishedWorkoutExercise>()

                // za svaku vjezbu, stvori FinishedWorkoutExercise
                for ((exerciseName, sets) in allSetsData) {
                    val validSets = sets.filter { it.first.isNotEmpty() && it.second.isNotEmpty() }

                    if (validSets.isNotEmpty()) {
                        workoutEmpty = false
                        var allWeights = ""
                        var allReps = ""

                        validSets.forEach { (weight, reps) ->
                            allWeights += "$weight,"
                            allReps += "$reps,"
                        }

                        val finishedExercise = FinishedWorkoutExercise(
                            workoutId = workoutId,
                            exerciseName = exerciseName,
                            weights = allWeights,
                            reps = allReps,
                            numberOfSets = validSets.size
                        )
                        workoutExercises.add(finishedExercise)
                    }

                    Log.d(
                        "ActiveWorkoutActivity",
                        "Exercise: $exerciseName,Number of sets: ${validSets.size} ,Sets: $validSets"
                    )
                }

                if (!workoutEmpty) {
                    // spremi vjezbe u bazu
                    workoutRepository.insertExercises(workoutId, workoutExercises)
                    Log.d("ActiveWorkoutActivity", "Workout saved successfully with ID: $workoutId")
                } else {
                    // ako nema ispunjenih vjezbi, izbrisi finished workout
                    Log.d("ActiveWorkoutActivity", "Workout not saved. No valid exercises.")
                    workoutRepository.deleteFinishedWorkout(workoutId)
                }
                Toast.makeText(
                    this@ActiveWorkoutActivity,
                    "Workout finished",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}