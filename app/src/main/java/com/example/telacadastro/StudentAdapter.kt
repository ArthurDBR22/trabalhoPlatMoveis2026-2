package com.example.telacadastro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.User

class StudentAdapter(
    private var studentList: List<User>,
    private val onStudentClick: (User) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStudentName: TextView = itemView.findViewById(R.id.txtStudentName)
        val txtStudentStatus: TextView = itemView.findViewById(R.id.txtStudentStatus)
        val txtStudentWorkout: TextView = itemView.findViewById(R.id.txtStudentWorkout)
        val txtStudentLastDate: TextView = itemView.findViewById(R.id.txtStudentLastDate)
    }

    fun updateList(newList: List<User>) {
        studentList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.txtStudentName.text = student.name.ifBlank { student.email }
        holder.txtStudentStatus.text = "Ativo"
        holder.txtStudentWorkout.text = if (student.currentWeightKg > 0) {
            "🏋️ Peso: ${student.currentWeightKg} kg • ${student.email}"
        } else {
            "🏋️ ${student.email}"
        }
        holder.txtStudentLastDate.text = "📋 Toque para gerenciar / montar treino"
        holder.itemView.setOnClickListener { onStudentClick(student) }
    }

    override fun getItemCount(): Int = studentList.size
}
