package com.example.projektr.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.databinding.ListTemplateExerciseBinding

class EditTemplateAdapter(
    private val exercises: MutableList<ExerciseWithSets>,
    private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<EditTemplateAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding =
            ListTemplateExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
        holder.binding.removeButton.setOnClickListener {
            onRemoveClick(holder.bindingAdapterPosition)
        }

        // azuriraj broj setova u slucaju da ga korisnik promijeni
        holder.binding.sets.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newSets = s.toString().toIntOrNull()
                if (newSets != null && newSets > 0) {
                    exercises[holder.adapterPosition].numberOfSets = newSets
                }
            }
        })
    }

    override fun getItemCount() = exercises.size

    class ExerciseViewHolder(
        val binding: ListTemplateExerciseBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseWithSets) {
            binding.exerciseName.text = exercise.exercise.name
            binding.sets.setText(exercise.numberOfSets.toString())
        }
    }
}