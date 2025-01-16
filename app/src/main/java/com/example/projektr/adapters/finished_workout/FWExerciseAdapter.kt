package com.example.projektr.adapters.finished_workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.adapters.active_workout.AWExerciseAdapter.ExerciseViewHolder
import com.example.projektr.database.FinishedWorkoutExercise
import com.example.projektr.databinding.ListFwExerciseBinding

class FWExerciseAdapter(
    private val exercises: MutableList<FinishedWorkoutExercise>
) : RecyclerView.Adapter<FWExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListFwExerciseBinding.inflate(inflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size


    class ExerciseViewHolder(
        private val binding: ListFwExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            exercise: FinishedWorkoutExercise
        ) {
            // postavi ime vjezbe
            binding.exerciseTitle.text = exercise.exerciseName

            binding.setRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            // dohvati listu tezina i ponavljanja te ukloni prazne stringove
            val weightsList = exercise.weights.split(",").filter { it.isNotEmpty() }
            val repsList = exercise.reps.split(",").filter { it.isNotEmpty() }

            // postavi adapter za setove
            val setsAdapter = FWSetsAdapter(weightsList, repsList)
            binding.setRecyclerView.adapter = setsAdapter
        }
    }
}