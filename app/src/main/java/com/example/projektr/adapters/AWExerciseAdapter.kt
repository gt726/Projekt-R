package com.example.projektr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.databinding.ListAwExerciseBinding

class AWExerciseAdapter(
    private val exercises: MutableList<ExerciseWithSets>
) : RecyclerView.Adapter<AWExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListAwExerciseBinding.inflate(inflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position]) {
            // ukloni vjezbu
            exercises.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, exercises.size)
        }
    }

    override fun getItemCount(): Int = exercises.size

    class ExerciseViewHolder(
        private val binding: ListAwExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exerciseWithSets: ExerciseWithSets, onRemoveLastSet: () -> Unit) {
            binding.exerciseTitle.text = exerciseWithSets.exercise.name

            binding.setRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            // postavi adapter za setove
            val setsAdapter = SetsAdapter(exerciseWithSets.numberOfSets)
            binding.setRecyclerView.adapter = setsAdapter

            // dodavanje setova
            binding.addSetButton.setOnClickListener {
                // povecaj broj setova i obavijesti adapter
                exerciseWithSets.numberOfSets++
                setsAdapter.addSet()
            }

            // uklanjanje setova
            binding.removeSetButton.setOnClickListener {
                // smanji broj setova i obavijesti adapter
                if (exerciseWithSets.numberOfSets > 1) {
                    exerciseWithSets.numberOfSets--
                    setsAdapter.removeSet()
                } else {
                    //ukloni set i vjezbu ako se radi o zadnjem setu
                    onRemoveLastSet()
                }
            }
        }
    }
}
