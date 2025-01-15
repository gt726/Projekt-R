package com.example.projektr.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.data.ExerciseWithSets
import com.example.projektr.databinding.ListAwExerciseBinding

class AWExerciseAdapter(
    private val exercises: MutableList<ExerciseWithSets>
) : RecyclerView.Adapter<AWExerciseAdapter.ExerciseViewHolder>() {

    // povezi vjezbe s podacima o setovima
    private val setsDataMap = mutableMapOf<ExerciseWithSets, MutableList<Pair<String, String>>>()

    // inicijaliziraj mapu za svaku vjezbu
    init {
        exercises.forEach { exercise ->
            setsDataMap[exercise] = MutableList(exercise.numberOfSets) { Pair("", "") }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListAwExerciseBinding.inflate(inflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(
            exercises[position],
            setsDataMap,
            setsDataMap[exercises[position]] ?: mutableListOf()
        ) {
            // ukloni vjezbu
            exercises.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, exercises.size)
        }
    }

    override fun getItemCount(): Int = exercises.size

    // dohvati podatke za sve setove
    fun getAllSetsData(): Map<String, List<Pair<String, String>>> {
        return setsDataMap.mapKeys { it.key.exercise.name }
    }

    class ExerciseViewHolder(
        private val binding: ListAwExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            exerciseWithSets: ExerciseWithSets,
            setsDataMap: MutableMap<ExerciseWithSets, MutableList<Pair<String, String>>>,
            setsData: MutableList<Pair<String, String>>,
            onRemoveLastSet: () -> Unit
        ) {
            // postavi ime vjezbe
            binding.exerciseTitle.text = exerciseWithSets.exercise.name

            binding.setRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            // postavi adapter za setove
            val setsAdapter = SetsAdapter(exerciseWithSets.numberOfSets)
            binding.setRecyclerView.adapter = setsAdapter

            // osvjezi podatke za setove prilikom svake interakcije
            binding.setRecyclerView.addOnItemTouchListener(object :
                RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    // azuriraj podatke za setove
                    setsData.clear()
                    setsData.addAll(setsAdapter.getSetsData())
                    Log.d("AWExerciseAdapter", "setsData: $setsData")
                    setsDataMap[exerciseWithSets] = setsData
                    return false
                }
            })


            // dodavanje setova
            binding.addSetButton.setOnClickListener {
                // povecaj broj setova i obavijesti adapter
                exerciseWithSets.numberOfSets++
                setsAdapter.addSet()
                //setsData.add(Pair("", ""))
                setsData.clear()
                setsData.addAll(setsAdapter.getSetsData())
                setsDataMap[exerciseWithSets] = setsData
                //Log.d("AWExerciseAdapter", "setsData: $setsData")
            }

            // uklanjanje setova
            binding.removeSetButton.setOnClickListener {
                setsData.clear()
                setsData.addAll(setsAdapter.getSetsData())
                setsDataMap[exerciseWithSets] = setsData
                //Log.d("AWExerciseAdapter", "setsData: $setsData")
                //Log.d("AWExerciseAdapter", "number of sets: ${exerciseWithSets.numberOfSets}")

                // smanji broj setova i obavijesti adapter
                if (exerciseWithSets.numberOfSets > 1) {
                    exerciseWithSets.numberOfSets--
                    setsAdapter.removeSet()
                    setsData.removeAt(setsData.size - 1)
                    setsDataMap[exerciseWithSets] = setsData
                } else {
                    //ukloni set i vjezbu ako se radi o zadnjem setu
                    onRemoveLastSet()
                }
                //Log.d("AWExerciseAdapter", "setsData: $setsData")
            }
        }
    }
}
