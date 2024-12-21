package com.example.projektr.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.adapters.ExercisesAdapter
import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseList
import com.example.projektr.databinding.ActivityNewTemplateBinding
import com.example.projektr.databinding.FragmentExercisesBinding

class NewTemplateActivity : AppCompatActivity() {

    private lateinit var exerciseList: List<Exercise>  // popis vjezbi
    private lateinit var recyclerView: RecyclerView // prikaz vjezbi
    private lateinit var adapter: ExercisesAdapter  // povezivanje podataka s prikazom

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityNewTemplateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        exerciseList = ExerciseList.list.sortedBy { it.name } // sortiraj vjezbe po imenu

        recyclerView = binding.exercisesRecyclerView // povezi xml
        adapter =
            ExercisesAdapter(exerciseList) //stvori novu instancu adaptera i predaj mu popis vjezbi kao argument
        recyclerView.layoutManager = LinearLayoutManager(this) // postavi layout manager
        recyclerView.adapter = adapter // postavi adapter za recyclerView

        // cancel button
        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            finish()
        }

        // finish button
        findViewById<Button>(R.id.finish_button).setOnClickListener {
            addSetsPrompt(ExerciseList.list[0])
            saveTemplate()
        }
    }

    private fun addSetsPrompt(exercise: Exercise) {
        val promptView = layoutInflater.inflate(R.layout.add_sets_prompt, null)
        val prompt = AlertDialog.Builder(this)
            .setView(promptView)
            .create()
        prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)


//        val promptCancelButton = findViewById<Button>(R.id.prompt_cancel_button)
//
//        promptCancelButton.setOnClickListener {
//            prompt.dismiss()
//        }

        prompt.show()

    }


    private fun saveTemplate() {
    }
}

