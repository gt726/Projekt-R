package com.example.projektr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.data.Exercise
import com.example.projektr.databinding.ListTemplateExerciseBinding

class ExercisesTemplateAdapter(
    private val exercises: List<Exercise>,
    private val onExerciseClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExercisesTemplateAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding =
            ListTemplateExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
        holder.itemView.setOnClickListener {
            onExerciseClick(exercise)
        }
    }

    override fun getItemCount() = exercises.size

    class ExerciseViewHolder(private val binding: ListTemplateExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.exerciseName.text = exercise.name
        }
    }
}
