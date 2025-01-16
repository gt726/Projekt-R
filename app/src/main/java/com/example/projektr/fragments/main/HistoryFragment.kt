package com.example.projektr.fragments.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projektr.R
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.activities.template.AddExerciseActivity
import com.example.projektr.adapters.HistoryAdapter
import com.example.projektr.adapters.TemplateAdapter
import com.example.projektr.database.AppDatabase
import com.example.projektr.databinding.FragmentHistoryBinding
import com.example.projektr.databinding.FragmentWorkoutBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // postavi binding
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //----------------------------------------------------------------------------------------
        // dohvati bazu podataka
        val db = AppDatabase.getDatabase(requireContext())
        Log.d("HistoryFragment", "Database initialized: $db")
        
        lifecycleScope.launch {
            val workouts = db.finishedWorkoutDao().getWorkouts()
            val workoutsWithExercises = workouts.map { workout ->
                val exercises = db.finishedWorkoutDao().getExercisesForWorkout(workout.id)
                workout to exercises
            }.toMutableList()
            recyclerView.adapter = HistoryAdapter(workoutsWithExercises, viewLifecycleOwner, db)
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