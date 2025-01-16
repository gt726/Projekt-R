package com.example.projektr.adapters.active_workout

import android.graphics.Color
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.databinding.ListAwSetBinding

class SetsAdapter(
    private var numberOfSets: Int
) : RecyclerView.Adapter<SetsAdapter.SetViewHolder>() {

    // Pair<weight, reps>
    private val setsData = mutableListOf<Pair<String, String>>()

    init {
        // inicjaliziraj listu setova
        for (i in 0 until numberOfSets) {
            setsData.add(Pair("", ""))
        }
    }


    // povecaj broj setova i obavijesti adapter
    fun addSet() {
        numberOfSets++
        setsData.add(Pair("", ""))
        notifyItemInserted(numberOfSets - 1)
    }

    // ukloni set
    fun removeSet() {
        numberOfSets--
        setsData.removeAt(numberOfSets)
        notifyItemRemoved(numberOfSets)
        //notifyItemRangeChanged(0, numberOfSets)
    }

    // dohvati podatke o setovima
    fun getSetsData(): MutableList<Pair<String, String>> {
        return setsData
    }

    class SetViewHolder(
        private val binding: ListAwSetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // TextWatcheri za weight i reps
        private var weightWatcher: TextWatcher? = null
        private var repsWatcher: TextWatcher? = null

        // za svaki set
        fun bind(setNumber: Int, setsData: MutableList<Pair<String, String>>) {
            // postavi redni broj seta
            binding.setNumber.text = "${setNumber + 1}"

            // ukloni stare watchere
            weightWatcher?.let { binding.weight.removeTextChangedListener(it) }
            repsWatcher?.let { binding.reps.removeTextChangedListener(it) }

            // azuriraj podatke u slucaju promjene
            weightWatcher = binding.weight.addTextChangedListener {
                setsData[setNumber] = setsData[setNumber].copy(first = it.toString())
            }

            // azuriraj podatke u slucaju promjene
            repsWatcher = binding.reps.addTextChangedListener {
                setsData[setNumber] = setsData[setNumber].copy(second = it.toString())
            }


            // dodaj OnClickListener za checkButton
            binding.checkButton.setOnClickListener() {

                // dohvati trenutnu pozadinu
                val currentColor =
                    (binding.root.background as? android.graphics.drawable.ColorDrawable)?.color


                // provjeri jesu li uneseni podaci
                if (binding.weight.text.toString().isNotEmpty() && binding.reps.text.toString()
                        .isNotEmpty()
                ) {
                    // postavi background na zelenu boju
                    binding.root.setBackgroundColor(Color.parseColor("#4312D512"))
                    binding.weight.setBackgroundColor(Color.parseColor("#00000000"))
                    binding.reps.setBackgroundColor(Color.parseColor("#00000000"))
                }

                // ako je set checkan, uncheckaj ga
                if (currentColor != Color.parseColor("#242424")) {
                    // postavi background na default
                    binding.root.setBackgroundColor(Color.parseColor("#242424"))
                    binding.weight.setBackgroundColor(Color.parseColor("#AD6A6A6A"))
                    binding.reps.setBackgroundColor(Color.parseColor("#AD6A6A6A"))
                }
            }
        }

        // spremi podatke
        fun saveData(setNumber: Int, setsData: MutableList<Pair<String, String>>) {
            setsData[setNumber] = setsData[setNumber].copy(
                first = binding.weight.text.toString(),
                second = binding.reps.text.toString()
            )
            Log.d("SetsAdapter", "Saved data for set $setNumber: ${setsData[setNumber]}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListAwSetBinding.inflate(inflater, parent, false)
        return SetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(position, setsData)
    }

    override fun onViewRecycled(holder: SetViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            holder.saveData(position, setsData)
        }
    }

    override fun getItemCount(): Int = numberOfSets
}
