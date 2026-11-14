package com.example.telacadastro

import com.example.myapplication.R
import com.example.myapplication.ConfiguracoesActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CreateWorkoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        // 1. Encontrar o RecyclerView pelo ID definido no XML
        val recyclerExercises = findViewById<RecyclerView>(R.id.recyclerExercises)

        // 2. Definir o LayoutManager (organiza os itens em lista vertical)
        recyclerExercises.layoutManager = LinearLayoutManager(this)

        // 3. Criar uma lista de exemplo com dados falsos para preencher os cartões
        val listaItens = listOf("Item 1", "Item 2", "Item 3", "Item 4")

        // 4. Atribuir o Adapter para desenhar os itens (usando o StudentAdapter que já criamos)
        recyclerExercises.adapter = StudentAdapter(listaItens)

        // Bottom Navigation setup
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, InstructorHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navAlunos)?.setOnClickListener {
            startActivity(Intent(this, StudentsListActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            // Already here
        }
        findViewById<LinearLayout>(R.id.navAgenda)?.setOnClickListener {
            startActivity(Intent(this, InstructorAgendaActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }
    }
}
