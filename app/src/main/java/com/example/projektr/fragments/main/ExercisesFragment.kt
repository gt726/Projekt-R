package com.example.projektr.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.data.Exercise
import com.example.projektr.databinding.FragmentExercisesBinding
import com.example.projektr.databinding.ListItemExerciseBinding

class ExercisesFragment : Fragment() {

    private lateinit var exercisesList: List<Exercise>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExercisesBinding.inflate(inflater, container, false)

        // Sample data or fetch from a database
        exercisesList = listOf(
            Exercise("Push-up"),
            Exercise("Squat"),
            Exercise("Plank"),
            Exercise("Lunges"),
            Exercise("Jumping Jacks"),
            Exercise("Burpees"),
            Exercise("Mountain Climbers"),
            Exercise("High Knees"),
            Exercise("Sit-ups"),
            Exercise("Leg Raises"),
            Exercise("Russian Twists"),
            Exercise("Bicycle Crunches"),
            Exercise("Superman"),
            Exercise("Bridge"),
            Exercise("Tricep Dips"),
            Exercise("Shoulder Taps"),
            Exercise("Wall Sit"),
            Exercise("Calf Raises")
        )

        recyclerView = binding.recyclerView
        adapter = ExercisesAdapter(exercisesList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // doddaj divider
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider)
        drawable?.let { divider.setDrawable(it) }
        recyclerView.addItemDecoration(divider)

        return binding.root
    }
}

class ExercisesAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding =
            ListItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount() = exercises.size

    class ExerciseViewHolder(private val binding: ListItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.exerciseName.text = exercise.name
        }
    }
}
