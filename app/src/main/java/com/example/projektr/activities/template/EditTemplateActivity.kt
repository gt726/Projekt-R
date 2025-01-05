package com.example.projektr.activities.template

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.activities.MainActivity
import com.example.projektr.adapters.EditTemplateAdapter
import com.example.projektr.adapters.TemplateAdapter
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.Template
import com.example.projektr.database.TemplateExercise
import kotlinx.coroutines.launch

class EditTemplateActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EditTemplateAdapter

    // popis odabranih vjezbi
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

        // dohvati popis vjezbi iz intenta
        val passedExercises =
            intent.getSerializableExtra("EXERCISES_LIST") as? ArrayList<ExerciseWithSets>
        if (passedExercises != null) {
            //exerciseList.clear()
            exerciseList.addAll(passedExercises)
        }

        // postavi adapter za recycler view
        recyclerView = findViewById(R.id.exercises_recycler_view)
        adapter = EditTemplateAdapter(exerciseList, ::onRemoveClicked)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // pronadi elemente
        val addButton = findViewById<Button>(R.id.add_exercise_button)
        val cancelButton = findViewById<Button>(R.id.cancel_button)
        val finishButton = findViewById<Button>(R.id.finish_button)

        // odustani od kreiranja templatea
        cancelButton.setOnClickListener {
            finish()
        }

        // zavrsi kreiranje templatea i pozovi funkciju za spremanje
        finishButton.setOnClickListener {
            saveTemplate(exerciseList)
        }

        // dodaj novu vjezbu
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
            // pokreni aktivnost za dodavanje nove vjezbe
            startActivity(intent)
            finish()
        }
    }


    // funkcija za uklanjanje vjezbe s popisa
    private fun onRemoveClicked(position: Int) {
        if (position in exerciseList.indices) { // provjeri poziciju
            exerciseList.removeAt(position)
            adapter.notifyItemRemoved(position) // obavijesti adapter da je vjezba uklonjena
            // obavijesti adapter da se promijenio broj vjezbi
            adapter.notifyItemRangeChanged(
                position,
                exerciseList.size
            )
        }
    }

    // funkcija za spremanje templatea
    private fun saveTemplate(exerciseList: List<ExerciseWithSets>) {
        // prikazi prompt za unos imena templatea
        val promptView = layoutInflater.inflate(R.layout.prompt_template_name, null)
        val prompt = AlertDialog.Builder(this)
            .setView(promptView)
            .create()
        //postavi transparentnu pozadinu
        prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // pronadi elemente prompta
        val templateName = promptView.findViewById<EditText>(R.id.name)
        val okButton = promptView.findViewById<Button>(R.id.ok_button)
        val cancelButton = promptView.findViewById<Button>(R.id.prompt_cancel_button)

        // odustani i zatvori prompt
        cancelButton.setOnClickListener() {
            prompt.dismiss()
        }

        // zapocni spremanje templatea
        okButton.setOnClickListener() {
            val name = templateName.text.toString()
            if (name.isNotBlank()) {
                val db = AppDatabase.getDatabase(this)
                lifecycleScope.launch {
                    val templateId = db.templateDao().insertTemplate(Template(name = name))
                    val exercises = exerciseList.map {
                        TemplateExercise(
                            templateId = templateId.toInt(),
                            exerciseName = it.exercise.name,
                            numberOfSets = it.numberOfSets
                        )
                    }
                    db.templateDao().insertExercises(exercises)

                    // javi korisniku da je template spremljen
                    Toast.makeText(
                        this@EditTemplateActivity,
                        "Template created!",
                        Toast.LENGTH_SHORT
                    ).show()

                    prompt.dismiss()
                    // osvjezi glavni ekran i zatvori trenutnu aktivnost
                    val intent = Intent(this@EditTemplateActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Please enter a template name", Toast.LENGTH_SHORT).show()
            }
        }
        prompt.show()
    }

}