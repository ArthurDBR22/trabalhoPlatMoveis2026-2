package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutHistoryAdapter(private val items: List<WorkoutHistoryItem>) :
    RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvType: TextView = view.findViewById(R.id.tvType)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvCalories: TextView = view.findViewById(R.id.tvCalories)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvType.text = item.type
        holder.tvDetails.text = "${item.exercisesCount} exercícios"
        holder.tvCalories.text = "${item.calories} kcal"
        holder.tvDate.text = item.date
        holder.tvDuration.text = item.duration
    }

    override fun getItemCount() = items.size
}