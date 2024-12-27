package com.example.projektr.activities.template

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.adapters.EditTemplateAdapter
import com.example.projektr.data.ExerciseWithSets

class EditTemplateActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EditTemplateAdapter
    private val exerciseList = mutableListOf<ExerciseWithSets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_template)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val passedExercises =
            intent.getSerializableExtra("EXERCISES_LIST") as? ArrayList<ExerciseWithSets>
        if (passedExercises != null) {
            //exerciseList.clear()
            exerciseList.addAll(passedExercises)
        }

        recyclerView = findViewById(R.id.exercisesRecyclerView)
        adapter = EditTemplateAdapter(exerciseList, ::onRemoveClicked)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        val addButton = findViewById<Button>(R.id.addExerciseButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val finishButton = findViewById<Button>(R.id.finishButton)

        cancelButton.setOnClickListener {
            finish()
        }

        finishButton.setOnClickListener {
            //saveTemplate(exerciseList)
        }

        addButton.setOnClickListener {
            // otvori popis vjezbi
            val intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra(
                "EXERCISES_LIST",
                ArrayList(exerciseList.map {
                    ExerciseWithSets(
                        it.exercise,
                        it.numberOfSets
                    )
                })
            )
            startActivity(intent)
            finish()
        }
    }


    private fun onRemoveClicked(position: Int) {
        if (position in exerciseList.indices) { // Validate position
            exerciseList.removeAt(position) // Remove the exercise
            adapter.notifyItemRemoved(position) // Notify adapter of item removal
            adapter.notifyItemRangeChanged(
                position,
                exerciseList.size
            ) // Notify adapter to refresh remaining items
        }
    }

    private fun saveTemplate(exerciseList: List<ExerciseWithSets>) {
        // prikazi prompt za unos imena templatea
//        val promptView = layoutInflater.inflate(R.layout.prompt_add_sets, null)
//        val prompt = AlertDialog.Builder(this)
//            .setView(promptView)
//            .create()
//        prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}