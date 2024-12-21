package com.example.projektr.fragments.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.projektr.R
import com.example.projektr.activities.NewTemplateActivity
import com.example.projektr.activities.SettingsActivity
import com.example.projektr.databinding.FragmentWorkoutBinding


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
        val createTemplateBtn =
            binding.createTemplateButton //view.findViewById<Button>(R.id.createTemplateButton)

        createTemplateBtn.setOnClickListener {
            val intent = Intent(activity, NewTemplateActivity::class.java)
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