package com.example.projektr.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.adapters.HistoryAdapter
import com.example.projektr.database.FinishedWorkoutRepository
import com.example.projektr.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private val workoutRepository = FinishedWorkoutRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // postavi binding
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //----------------------------------------------------------------------------------------

        lifecycleScope.launch {
            val workouts = workoutRepository.getFinishedWorkouts()
            val workoutsWithExercises = workouts.map { workout ->
                val exercises = workoutRepository.getExercisesForFinishedWorkout(workout.id)
                workout to exercises
            }.toMutableList()
            recyclerView.adapter =
                HistoryAdapter(workoutsWithExercises, viewLifecycleOwner, workoutRepository)
        }
        //----------------------------------------------------------------------------------------


        // postavi onClickListener za ikonu postavki
        val settingsIcon = binding.settingsIcon
        settingsIcon.setOnClickListener {
            // pokreni SettingsActivity
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}