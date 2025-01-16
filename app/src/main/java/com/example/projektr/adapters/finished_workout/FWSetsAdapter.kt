package com.example.projektr.adapters.finished_workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projektr.databinding.ListAwSetBinding
import com.example.projektr.databinding.ListFwSetBinding

class FWSetsAdapter(
    private val weights: List<String>,
    private val reps: List<String>
) : RecyclerView.Adapter<FWSetsAdapter.SetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListFwSetBinding.inflate(inflater, parent, false)
        return SetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(position + 1, weights[position], reps[position])
    }

    override fun getItemCount(): Int = minOf(weights.size, reps.size)

    class SetViewHolder(
        private val binding: ListFwSetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(setNumber: Int, weight: String, reps: String) {
            // postavi redni broj seta i podatke
            binding.setNumber.text = setNumber.toString()
            binding.weight.text = weight
            binding.reps.text = reps
        }
    }
}
