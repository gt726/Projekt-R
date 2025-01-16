package com.example.projektr.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.adapters.active_workout.AWExerciseAdapter
import com.example.projektr.adapters.finished_workout.FWExerciseAdapter
import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.FinishedWorkoutExercise
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FinishedWorkoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FWExerciseAdapter
    private val exerciseList = mutableListOf<FinishedWorkoutExercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finished_workout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // postavi recycler view
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FWExerciseAdapter(exerciseList)
        recyclerView.adapter = adapter

        // dohvati workoutId iz intenta
        val workoutId = intent.getIntExtra("WORKOUT_ID", -1)

        // dohvati elemente
        val title = findViewById<TextView>(R.id.title)
        val date = findViewById<TextView>(R.id.date)

        // dohvati vjezbe za taj workout iz baze
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val workout = db.finishedWorkoutDao().getWorkoutById(workoutId)
            title.text = workout.workoutName
            date.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(workout.date)

            val exercises = db.finishedWorkoutDao().getExercisesForWorkout(workoutId)
            exerciseList.clear()
            exerciseList.addAll(exercises)
            adapter.notifyDataSetChanged()
        }
    }
}