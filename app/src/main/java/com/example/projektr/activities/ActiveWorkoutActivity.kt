package com.example.projektr.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.adapters.AWExerciseAdapter
import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.FinishedWorkout
import com.example.projektr.database.FinishedWorkoutExercise
import kotlinx.coroutines.launch

class ActiveWorkoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AWExerciseAdapter

    // popis odabranih vjezbi
    private val exerciseList = mutableListOf<ExerciseWithSets>()

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
        val templateId = intent.getIntExtra("TEMPLATE_ID", -1)
        Log.d("ActiveWorkoutActivity", "templateId: $templateId")

        // dohvati elemente
        val title = findViewById<TextView>(R.id.title)
        val cancelButton = findViewById<Button>(R.id.cancel_button)
        val finishButton = findViewById<Button>(R.id.finish_button)

        // dohvati vjezbe za taj template iz baze
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val template = db.templateDao().getTemplateById(templateId)
            title.text = template.name
            val exercises = db.templateDao().getExercisesForTemplate(templateId)
            exerciseList.clear()
            exerciseList.addAll(exercises.map {
                ExerciseWithSets(Exercise(it.exerciseName), it.numberOfSets)
            })
            adapter.notifyDataSetChanged()
        }

        // odustani od treninga
        cancelButton.setOnClickListener {
            finish()
        }

        // zavrsi trening
        finishButton.setOnClickListener {
            lifecycleScope.launch {
                val activeWorkoutDao = db.finishedWorkoutDao()

                // stvori novi FinishedWorkout
                val workout = FinishedWorkout(
                    workoutName = title.text.toString(),
                    date = System.currentTimeMillis()
                )
                val workoutId = activeWorkoutDao.insertWorkout(workout)

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
                            workoutId = workoutId.toInt(),
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
                    activeWorkoutDao.insertWorkoutExercises(workoutExercises)
                    Log.d("ActiveWorkoutActivity", "Workout saved successfully with ID: $workoutId")
                } else {
                    // ako nema ispunjenih vjezbi, izbrisi finished workout
                    Log.d("ActiveWorkoutActivity", "Workout not saved. No valid exercises.")
                    activeWorkoutDao.deleteWorkoutById(workoutId.toInt())
                }
                finish()
            }
        }
    }
}