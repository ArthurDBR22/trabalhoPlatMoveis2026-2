package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class CustomWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_workout)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rvExercises = findViewById<RecyclerView>(R.id.rvExercises)
        rvExercises.layoutManager = LinearLayoutManager(this)
        
        val exercises = listOf(
            Exercise(1, "Supino Reto com Barra", "Peito", 3, "10 a 12", 20),
            Exercise(2, "Supino Inclinado Halteres", "Peito", 3, "10", 18),
            Exercise(3, "Crucifixo Máquina", "Peito", 3, "12", 25),
            Exercise(4, "Tríceps Pulley", "Tríceps", 4, "12", 15),
            Exercise(5, "Tríceps Testa", "Tríceps", 3, "10 a 12", 10),
            Exercise(6, "Abdominal Crunch", "Abdômen", 3, "15", 0)
        )
        
        val adapter = ExerciseAdapter(exercises)
        rvExercises.adapter = adapter

        // Setup Tabs
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabName = tab?.text
                Toast.makeText(this@CustomWorkoutActivity, "Aba selecionada: $tabName", Toast.LENGTH_SHORT).show()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        

        findViewById<android.view.View>(R.id.btnReorder).setOnClickListener {
            Toast.makeText(this, "A funcionalidade de reordenar lista está em desenvolvimento.", Toast.LENGTH_SHORT).show()
        }
        findViewById<android.view.View>(R.id.btnEditWorkout).setOnClickListener {
            Toast.makeText(this, "Acesso à edição de treino.", Toast.LENGTH_SHORT).show()
        }
        findViewById<android.view.View>(R.id.btnStartWorkout).setOnClickListener {
            startActivity(Intent(this, WorkoutInProgressActivity::class.java))

        }
    }
}