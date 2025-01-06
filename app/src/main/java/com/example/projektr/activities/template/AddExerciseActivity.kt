package com.example.projektr.activities.template

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.activities.MainActivity
import com.example.projektr.adapters.AddExerciseAdapter
import com.example.projektr.data.Exercise
import com.example.projektr.data.ExerciseList
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.databinding.ActivityAddExerciseBinding

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var exerciseList: List<Exercise>  // popis svih vjezbi u sustavu
    private lateinit var recyclerView: RecyclerView // prikaz vjezbi
    private lateinit var adapter: AddExerciseAdapter  // povezivanje podataka s prikazom
    private val selectedExercises = mutableListOf<ExerciseWithSets>() // popis odabranih vjezbi

//    private val selectedExercises = mutableListOf<ExerciseWithSets>(
//        ExerciseWithSets(Exercise("Push-up"), 1),
//        ExerciseWithSets(Exercise("Squat"), 2),
//        ExerciseWithSets(Exercise("Plank"), 3),
//        ExerciseWithSets(Exercise("Lunges"), 4),
//        ExerciseWithSets(Exercise("Jumping Jacks"), 5),
//        ExerciseWithSets(Exercise("Burpees"), 6)
//    ) // testni popis

    override fun onCreate(savedInstanceState: Bundle?) {

        val binding = ActivityAddExerciseBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // provjeri je li lista vjezbi poslana iz EditTemplateActivity
        val passedExercises =
            intent.getSerializableExtra("EXERCISES_LIST") as? ArrayList<ExerciseWithSets>
        if (passedExercises != null) {
            //selectedExercises.clear()
            selectedExercises.addAll(passedExercises)
        }

        // dohvati podatke iz intenta
        val startMode = intent.getStringExtra("START_MODE") ?: ""
        Log.d("AddExerciseActivity", "startMode: $startMode")

        val existingTemplateId = intent.getIntExtra("TEMPLATE_ID", -1)

        // sortiraj vjezbe abecedno
        exerciseList = ExerciseList.list.sortedBy { it.name }

        // pronadi recyclerView
        recyclerView = binding.exercisesRecyclerView
        //stvori novu instancu adaptera i predaj mu popis vjezbi kao argument
        adapter =
            AddExerciseAdapter(exerciseList) { exercise ->
                addSetsPrompt(
                    exercise,
                    startMode,
                    existingTemplateId
                )
            }
        recyclerView.layoutManager = LinearLayoutManager(this) // postavi layout manager
        recyclerView.adapter = adapter // postavi adapter za recyclerView

        // onClickListener za cancel button
        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            // ako je popis prazan, vrati se na MainActivity, inace se vrati na EditTemplateActivity
            val intent: Intent
            if (selectedExercises.isNotEmpty()) {
                intent = Intent(this, EditTemplateActivity::class.java)
                intent.putExtra(
                    "EXERCISES_LIST",
                    ArrayList(selectedExercises.map {
                        ExerciseWithSets(
                            it.exercise,
                            it.numberOfSets
                        )
                    })
                )
                // posalji potrebne detalje
                intent.putExtra("START_MODE", startMode)
                intent.putExtra("LOAD_FROM_DB", false)
                intent.putExtra("TEMPLATE_ID", existingTemplateId)
            } else {
                intent = Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

    }

    // funkcija za pop up window za dodavanje broja setova
    private fun addSetsPrompt(exercise: Exercise, startMode: String, existingTemplateId: Int) {
        val promptView = layoutInflater.inflate(R.layout.prompt_add_sets, null)
        val prompt = AlertDialog.Builder(this).setView(promptView).create()
        // postavi transparentnu pozadinu
        prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // pronadi elemente u popup-u
        val exerciseName = promptView.findViewById<TextView>(R.id.exercise_name)
        val numberOfSets = promptView.findViewById<EditText>(R.id.sets)
        val okButton = promptView.findViewById<Button>(R.id.ok_button)
        val cancelButton = promptView.findViewById<Button>(R.id.prompt_cancel_button)

        // postavi ime vjezbe u popup
        exerciseName.text = exercise.name

        numberOfSets.requestFocus()

        // zatvori popup
        cancelButton.setOnClickListener() {
            prompt.dismiss()
        }

        // dodaj vjezbu u listu odabranih vjezbi
        okButton.setOnClickListener() {
            val sets = numberOfSets.text.toString().toIntOrNull()

            if (sets != null && sets > 0) {
                selectedExercises.add(ExerciseWithSets(exercise, sets))
                prompt.dismiss()

                // zapocni edit template activity i posalji mu popis vjezbi
                val intent = Intent(this, EditTemplateActivity::class.java)
                intent.putExtra(
                    "EXERCISES_LIST",
                    ArrayList(selectedExercises.map {
                        ExerciseWithSets(
                            it.exercise,
                            it.numberOfSets
                        )
                    })
                )
                // posalji potrebne detalje
                intent.putExtra("START_MODE", startMode)
                intent.putExtra("LOAD_FROM_DB", false)
                intent.putExtra("TEMPLATE_ID", existingTemplateId)
                // pokreni novi activity i zatvori trenutni
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a valid number of sets", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        prompt.show()
    }
}

