package com.example.projektr.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.adapters.finished_workout.FWExerciseAdapter
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.database.FirestoreFinishedWorkout.FinishedWorkoutExercise
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FinishedWorkoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FWExerciseAdapter
    private val exerciseList = mutableListOf<FinishedWorkoutExercise>()
    private val workoutRepository = FinishedWorkoutRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finished_workout)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // postavi recycler view
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FWExerciseAdapter(exerciseList)
        recyclerView.adapter = adapter

        // dohvati workoutId iz intenta
        val workoutId = intent.getStringExtra("WORKOUT_ID") ?: ""

        // dohvati elemente
        val title = findViewById<TextView>(R.id.title)
        val date = findViewById<TextView>(R.id.date)

        // dohvati vjezbe za taj workout iz baze
        lifecycleScope.launch {

            val workout = workoutRepository.getFinishedWorkouts().find { it.id == workoutId }
            workout?.let {
                title.text = it.workoutName
                date.text = formatDate(it.date)
                val exercises = workoutRepository.getExercisesForFinishedWorkout(it.id)
                exerciseList.clear()
                exerciseList.addAll(exercises)
                adapter.notifyDataSetChanged()
            }

        }
    }
}

fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(timestamp)
}
