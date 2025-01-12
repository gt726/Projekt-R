package com.example.projektr.adapters

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.databinding.ListAwSetBinding

class SetsAdapter(
    private var numberOfSets: Int
) : RecyclerView.Adapter<SetsAdapter.SetViewHolder>() {

    fun addSet() {
        // povecaj broj setova i obavijesti adapter
        numberOfSets++
        notifyItemInserted(numberOfSets - 1)
    }

    // ukloni set
    fun removeSet() {
        numberOfSets--
        notifyItemRemoved(numberOfSets)
        //notifyItemRangeChanged(0, numberOfSets)
    }

    class SetViewHolder(
        private val binding: ListAwSetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(setNumber: Int) {
            binding.setNumber.text = "${setNumber + 1}"

            binding.weight.setText("")
            binding.reps.setText("")

            // dodaj OnClickListener za checkButton
            binding.checkButton.setOnClickListener() {

                val currentColor =
                    (binding.root.background as? android.graphics.drawable.ColorDrawable)?.color


                // provjeri jesu li uneseni podaci
                if (binding.weight.text.toString().isNotEmpty() && binding.reps.text.toString()
                        .isNotEmpty()
                ) {
                    // postavi background na zelenu boju
                    binding.root.setBackgroundColor(Color.parseColor("#4312D512"))
                }

                if (currentColor != Color.parseColor("#242424")) {
                    // postavi background na default
                    binding.root.setBackgroundColor(Color.parseColor("#242424"))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListAwSetBinding.inflate(inflater, parent, false)
        return SetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(position)


    }

    override fun getItemCount(): Int = numberOfSets
}
