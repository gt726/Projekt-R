package com.example.projektr.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.activities.ActiveWorkoutActivity
import com.example.projektr.activities.template.EditTemplateActivity
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.Template
import com.example.projektr.database.TemplateExercise
import com.example.projektr.fragments.main.WorkoutFragment
import kotlinx.coroutines.launch

class TemplateAdapter(
    private val templatesWithExercises: MutableList<Pair<Template, List<TemplateExercise>>>,
    private val lifecycleOwner: LifecycleOwner,
    private val db: AppDatabase
) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val templateName: TextView = view.findViewById(R.id.template_name)
        val exerciseList: LinearLayout = view.findViewById(R.id.exercise_list)
        val overflowMenu: ImageView = view.findViewById(R.id.overflow_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_template, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (template, exercises) = templatesWithExercises[position]
        holder.templateName.text = template.name
        holder.exerciseList.removeAllViews()
        exercises.forEach { exercise ->
            val textView = TextView(holder.itemView.context).apply {
                text = "${exercise.exerciseName} - ${exercise.numberOfSets} sets"
                setPadding(8, 4, 8, 4)
                setTextColor(Color.parseColor("#B1B1B1"))
            }
            holder.exerciseList.addView(textView)
        }

        // OnClickListener za svaki template
        holder.itemView.setOnClickListener {
            // stvori novi prompt za potvrdu pokretanja templatea
            val inflater = LayoutInflater.from(it.context)
            val promptView = inflater.inflate(R.layout.prompt_start_workout, null)
            val prompt = AlertDialog.Builder(it.context)
                .setView(promptView)
                .create()
            prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // dohvati elemente iz prompta
            val templateName = promptView.findViewById<TextView>(R.id.template_name)
            val noButton = promptView.findViewById<Button>(R.id.no_btn)
            val yesButton = promptView.findViewById<Button>(R.id.yes_btn)

            // postavi ime templatea
            templateName.text = "\" ${template.name} ?\""

            // odustani
            noButton.setOnClickListener {
                prompt.dismiss()
            }

            // pokreni workout
            yesButton.setOnClickListener {

                val intent = Intent(it.context, ActiveWorkoutActivity::class.java)
                intent.putExtra("TEMPLATE_ID", template.id)
                it.context.startActivity(intent)

                Toast.makeText(it.context, "Started template: ${template.name}", Toast.LENGTH_SHORT)
                    .show()
                prompt.dismiss()
            }

            prompt.show()
        }

        // postavi onClickListener za overflowMenu
        holder.overflowMenu.setOnClickListener { view ->
            // stvori novi popupMenu
            val popupMenu = PopupMenu(holder.itemView.context, view)
            popupMenu.inflate(R.menu.template_list_menu)

            // postavi onClickListener za svaku opciju u meniu
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    // uredi template
                    R.id.menu_edit -> {
                        val context = holder.itemView.context
                        // posalji template id i javi da se ureduje postojeci template
                        val intent = Intent(context, EditTemplateActivity::class.java).apply {
                            putExtra("TEMPLATE_ID", template.id)
                            putExtra("START_MODE", "EDIT_EXISTING")
                            putExtra("LOAD_FROM_DB", true)
                        }
                        // pokreni EditTemplateActivity
                        context.startActivity(intent)
                        true
                    }

                    // preimenuj template
                    R.id.menu_rename -> {
                        val inflater = LayoutInflater.from(view.context)
                        // stvori novi prompt za unos imena templatea
                        val promptView = inflater.inflate(R.layout.prompt_template_name, null)
                        val prompt = AlertDialog.Builder(view.context)
                            .setView(promptView)
                            .create()
                        prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)

                        // dohvati elemente iz prompta
                        val templateName = promptView.findViewById<EditText>(R.id.name)
                        val okButton = promptView.findViewById<Button>(R.id.ok_button)
                        val cancelButton =
                            promptView.findViewById<Button>(R.id.prompt_cancel_button)

                        templateName.requestFocus()

                        // odustani od preimenovanja
                        cancelButton.setOnClickListener() {
                            prompt.dismiss()
                        }

                        // potvrdi preimenovanje
                        okButton.setOnClickListener() {
                            val name = templateName.text.toString()
                            if (name.isNotBlank()) {

                                lifecycleOwner.lifecycleScope.launch {
                                    // azuriraj ime templatea u bazi
                                    db.templateDao().renameTemplate(template.id, name)
                                }
                                // azuriraj ime templatea na popisu
                                holder.templateName.text = name
                                prompt.dismiss()
                            } else {
                                Toast.makeText(
                                    view.context,
                                    "Please enter a template name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        prompt.show()
                        true
                    }

                    // izbrisi template
                    R.id.menu_delete -> {
                        lifecycleOwner.lifecycleScope.launch {
                            // izbrisi template iz baze
                            db.templateDao().deleteTemplateAndExercises(template.id)

                            if (position in templatesWithExercises.indices) { // provjeri poziciju
                                templatesWithExercises.removeAt(position)
                                notifyItemRemoved(position) // obavijesti adapter da je item izbrisan
                                notifyItemRangeChanged(
                                    position,
                                    templatesWithExercises.size
                                ) // obavijesti adapter da su se promijenile pozicije itema
                            }
                        }
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    override fun getItemCount() = templatesWithExercises.size
}
