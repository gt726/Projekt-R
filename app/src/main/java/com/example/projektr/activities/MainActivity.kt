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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_exercises -> {
                    // Navigate to Exercises Fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ExercisesFragment())
                        .commit()
                    true
                }

                R.id.nav_workout -> {
                    // Navigate to Workout Fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WorkoutFragment()).commit()
                    true
                }

                R.id.nav_history -> {
                    // Navigate to History Fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HistoryFragment()).commit()
                    true
                }

                else -> false
            }
        }

        // Default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_workout
        }
    }
}