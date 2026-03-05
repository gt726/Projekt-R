package com.example.projektr.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.activities.ExerciseDetailActivity
import com.example.projektr.data.Exercise
import com.example.projektr.databinding.ListItemExerciseBinding

// za fragment

class ExercisesAdapter(
    private val context: Context,
    private val exercises: List<Exercise>
) :
    RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding =
            ListItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount() = exercises.size

    class ExerciseViewHolder(
        private val binding: ListItemExerciseBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.exerciseName.text = exercise.name

            binding.root.setOnClickListener {
                val intent = Intent(context, ExerciseDetailActivity::class.java).apply {
                    putExtra("exercise_name", exercise.name)
                }
                context.startActivity(intent)
            }
        }
    }
}