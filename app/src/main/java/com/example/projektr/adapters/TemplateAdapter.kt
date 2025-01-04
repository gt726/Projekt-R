package com.example.projektr.adapters

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
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.Template
import com.example.projektr.database.TemplateExercise
import kotlinx.coroutines.launch

class TemplateAdapter(
    private val templatesWithExercises: List<Pair<Template, List<TemplateExercise>>>,
    private val lifecycleOwner: LifecycleOwner,
    private val db: AppDatabase
) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val templateName: TextView = view.findViewById(R.id.templateName)
        val exerciseList: LinearLayout = view.findViewById(R.id.exerciseList)
        val overflowMenu: ImageView = view.findViewById(R.id.overflowMenu)
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

        // postavi onClickListener za overflowMenu
        holder.overflowMenu.setOnClickListener { view ->
            // Create and show the popup menu
            val popupMenu = PopupMenu(holder.itemView.context, view)
            // Inflate the menu layout
            popupMenu.inflate(R.menu.template_list_menu)

            // postavi onClickListener za svaku opciju u meniu
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        true
                    }

                    R.id.menu_rename -> {
                        val inflater = LayoutInflater.from(view.context)
                        val promptView = inflater.inflate(R.layout.prompt_template_name, null)
                        val prompt = AlertDialog.Builder(view.context)
                            .setView(promptView)
                            .create()
                        prompt.window?.setBackgroundDrawableResource(android.R.color.transparent)

                        val templateName = promptView.findViewById<EditText>(R.id.name)
                        val okButton = promptView.findViewById<Button>(R.id.ok_button)
                        val cancelButton =
                            promptView.findViewById<Button>(R.id.prompt_cancel_button)

                        cancelButton.setOnClickListener() {
                            prompt.dismiss()
                        }
                        okButton.setOnClickListener() {
                            val name = templateName.text.toString()
                            if (name.isNotBlank()) {

                                lifecycleOwner.lifecycleScope.launch {
                                    // azuriraj ime templatea u bazi
                                    db.templateDao().renameTemplate(template.id, name)
                                }
                                // azuriraj ime templatea u listi
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

                    R.id.menu_delete -> {
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
