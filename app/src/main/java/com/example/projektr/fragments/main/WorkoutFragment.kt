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


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val createTemplateBtn =
            binding.createTemplateButton


        val dummyData = listOf(
            Template(1, "Template 1") to listOf(
                TemplateExercise(1, 1, "Exercise A", 3),
                TemplateExercise(2, 1, "Exercise B", 4)
            ),
            Template(2, "Template 2") to listOf(
                TemplateExercise(3, 2, "Exercise C", 5)
            )
        )


        val db = AppDatabase.getDatabase(requireContext())
        Log.d("WorkoutFragment", "Database initialized: $db")
        lifecycleScope.launch {
            val templates = db.templateDao().getTemplates()
            val templatesWithExercises = templates.map { template ->
                val exercises = db.templateDao().getExercisesForTemplate(template.id)
                template to exercises
            }
            recyclerView.adapter = TemplateAdapter(templatesWithExercises, viewLifecycleOwner, db)
        }

        createTemplateBtn.setOnClickListener {
            val intent = Intent(activity, AddExerciseActivity::class.java)
            startActivity(intent)
        }

        val settingsIcon = binding.settingsIcon
        settingsIcon.setOnClickListener {
            // pokreni SettingsActivity
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return binding.root//inflater.inflate(R.layout.fragment_workout, container, false)
    }


}