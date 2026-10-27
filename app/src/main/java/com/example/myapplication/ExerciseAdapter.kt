package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrder: TextView = view.findViewById(R.id.tvOrder)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvMuscle: TextView = view.findViewById(R.id.tvMuscle)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.tvOrder.text = exercise.order.toString()
        holder.tvName.text = exercise.name
        holder.tvMuscle.text = exercise.muscleGroup
        holder.tvDetails.text = "${exercise.sets} séries | ${exercise.reps} rep | ${exercise.weightKg} kg"
    }

    override fun getItemCount() = exercises.size
}