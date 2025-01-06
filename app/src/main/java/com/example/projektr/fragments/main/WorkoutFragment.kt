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
import com.example.projektr.activities.template.AddExerciseActivity
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.adapters.TemplateAdapter
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.Template
import com.example.projektr.database.TemplateExercise
import com.example.projektr.databinding.FragmentWorkoutBinding
import kotlinx.coroutines.launch


class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // postavi binding
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val createTemplateBtn = binding.createTemplateButton


        // dohvati bazu podataka
        val db = AppDatabase.getDatabase(requireContext())
        Log.d("WorkoutFragment", "Database initialized: $db")
        lifecycleScope.launch {
            val templates = db.templateDao().getTemplates()
            val templatesWithExercises = templates.map { template ->
                val exercises = db.templateDao().getExercisesForTemplate(template.id)
                template to exercises
            }.toMutableList()
            recyclerView.adapter = TemplateAdapter(templatesWithExercises, viewLifecycleOwner, db)
        }

        // postavi onClickListener za gumb za kreiranje novog templatea
        createTemplateBtn.setOnClickListener {
            val intent = Intent(activity, AddExerciseActivity::class.java)
            intent.putExtra("START_MODE", "CREATE_NEW")
            startActivity(intent)
        }

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