package com.example.projektr.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.R
import com.example.projektr.activities.FinishedWorkoutActivity
import com.example.projektr.database.AppDatabase
import com.example.projektr.database.FinishedWorkout
import com.example.projektr.database.FinishedWorkoutExercise
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val workoutsWithExercises: MutableList<Pair<FinishedWorkout, List<FinishedWorkoutExercise>>>,
    private val lifecycleOwner: LifecycleOwner,
    private val db: AppDatabase
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // sortiraj od najnovijeg prema najstarijem
    init {
        workoutsWithExercises.sortByDescending { it.first.date }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // dohvati elemente
        val date: TextView = view.findViewById(R.id.date)
        val workoutName: TextView = view.findViewById(R.id.workout_name)
        val exerciseList: LinearLayout = view.findViewById(R.id.exercise_list)
        val overflowMenu: ImageView = view.findViewById(R.id.overflow_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_finished_workout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (workout, exercises) = workoutsWithExercises[position]

        // dohvati datum i konvertiraj ga u string
        val date = Date(workout.date)
        val formatter =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


        // postavi podatke
        holder.date.text = formatter.format(date)
        holder.workoutName.text = workout.workoutName
        holder.exerciseList.removeAllViews()
        exercises.forEach { exercise ->
            val textView = TextView(holder.itemView.context).apply {
                text = "${exercise.exerciseName} - ${exercise.numberOfSets} sets"
                setPadding(8, 4, 8, 4)
                setTextColor(Color.parseColor("#B1B1B1"))
            }
            holder.exerciseList.addView(textView)
        }

        // postavi onClickListener za svaki item
        holder.itemView.setOnClickListener {
            // pokreni FinishedWorkoutActivity
            val intent = Intent(it.context, FinishedWorkoutActivity::class.java)
            intent.putExtra("WORKOUT_ID", workout.id)
            it.context.startActivity(intent)
        }

        // postavi onClickListener za overflowMenu
        holder.overflowMenu.setOnClickListener { view ->
            // stvori novi popupMenu
            val popupMenu = PopupMenu(holder.itemView.context, view)
            popupMenu.inflate(R.menu.history_list_menu)

            // postavi onClickListener za svaku opciju u meniu
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    // izbrisi workout iz historya
                    R.id.menu_delete -> {
                        lifecycleOwner.lifecycleScope.launch {
                            // izbrisi workout iz baze
                            db.finishedWorkoutDao().deleteWorkoutAndExercises(workout.id)

                            if (position in workoutsWithExercises.indices) { // provjeri poziciju
                                workoutsWithExercises.removeAt(position)
                                notifyItemRemoved(position) // obavijesti adapter da je item izbrisan
                                notifyItemRangeChanged(
                                    position,
                                    workoutsWithExercises.size
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

    override fun getItemCount() = workoutsWithExercises.size
}