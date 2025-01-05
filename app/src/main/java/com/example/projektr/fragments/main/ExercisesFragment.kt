package com.example.projektr.fragments.main

import android.content.Intent
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
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.adapters.ExercisesAdapter
import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseList
import com.example.projektr.databinding.FragmentExercisesBinding
import com.example.projektr.databinding.ListItemExerciseBinding

class ExercisesFragment : Fragment() {

    private lateinit var exercisesList: List<Exercise>  // popis vjezbi
    private lateinit var recyclerView: RecyclerView // prikaz vjezbi
    private lateinit var adapter: ExercisesAdapter  // povezivanje podataka s prikazom

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentExercisesBinding.inflate(inflater, container, false)

        // dohvati popis vjezbi i sortiraj ih po imenu
        exercisesList = ExerciseList.list.sortedBy { it.name }

        recyclerView = binding.recyclerView // povezi xml
        adapter =
            ExercisesAdapter(exercisesList) //stvori novu instancu adaptera i predaj mu popis vjezbi kao argument
        recyclerView.layoutManager = LinearLayoutManager(context) // postavi layout manager
        recyclerView.adapter = adapter // postavi adapter za recyclerView

        // doddaj divider
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider)
        drawable?.let { divider.setDrawable(it) }
        recyclerView.addItemDecoration(divider)

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
