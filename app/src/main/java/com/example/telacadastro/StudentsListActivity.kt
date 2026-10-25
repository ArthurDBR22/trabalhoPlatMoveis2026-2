package com.example.telacadastro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_students)

        val recyclerStudents = findViewById<RecyclerView>(R.id.recyclerStudents)
        recyclerStudents.layoutManager = LinearLayoutManager(this)

        // Lista de exemplo para preencher o RecyclerView e testar a tela
        val listaAlunos = listOf("Aluno 1", "Aluno 2", "Aluno 3", "Aluno 4")

        // Atribuindo um adaptador simples baseado no seu item_student.xml
        recyclerStudents.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = layoutInflater.inflate(R.layout.item_student, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
            override fun getItemCount(): Int = listaAlunos.size
        }
    }
}