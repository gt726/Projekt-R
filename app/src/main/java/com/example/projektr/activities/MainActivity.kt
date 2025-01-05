package com.example.projektr.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projektr.R
import com.example.projektr.fragments.main.ExercisesFragment
import com.example.projektr.fragments.main.HistoryFragment
import com.example.projektr.fragments.main.WorkoutFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // dohvati traku za navigaciju
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // postavi listener za navigaciju
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // odabran je Exercises
                R.id.nav_exercises -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ExercisesFragment())
                        .commit()
                    true
                }

                // odabran je Workout
                R.id.nav_workout -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WorkoutFragment()).commit()
                    true
                }

                // odabran je History
                R.id.nav_history -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HistoryFragment()).commit()
                    true
                }

                else -> false
            }
        }

        // postavi default fragment na Workout
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_workout
        }
    }
}