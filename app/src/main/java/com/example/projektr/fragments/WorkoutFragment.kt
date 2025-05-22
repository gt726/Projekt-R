package com.example.projektr.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.activities.template.AddExerciseActivity
import com.example.projektr.adapters.TemplateAdapter
import com.example.projektr.database.TemplateRepository
import com.example.projektr.databinding.FragmentWorkoutBinding
import kotlinx.coroutines.launch


class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding

    //    private val templateRepository = TemplateRepository()
    private val templateRepository = TemplateRepository() ?: TemplateRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // postavi binding
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        val templateRepository = TemplateRepository.instance ?: TemplateRepository()


        val createTemplateBtn = binding.createTemplateButton


        lifecycleScope.launch {

            try {
                val templates = templateRepository.getTemplates()
                val templatesWithExercises = templates.map { template ->
                    val exercises = templateRepository.getExercisesForTemplate(template.id)
                    template to exercises
                }.toMutableList()
                Log.d("WorkoutFragment", "Templates with exercises: $templatesWithExercises")

                // inicializiraj adapter
                recyclerView.adapter = TemplateAdapter(
                    templatesWithExercises,
                    viewLifecycleOwner,
                    templateRepository
                )
            } catch (e: Exception) {
                Log.e("WorkoutFragment", "Error loading templates", e)
                recyclerView.visibility = View.GONE
            }
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