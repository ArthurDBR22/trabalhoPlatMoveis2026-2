package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CustomWorkoutActivity : AppCompatActivity() {

    private val repository = UnifitRepositoryImpl()
    private val auth = FirebaseAuth.getInstance()

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

        val defaultExercises = listOf(
            Exercise(order = 1, name = "Supino Reto", muscleGroup = "Peitoral", sets = 4, reps = "10-12", weightKg = 20),
            Exercise(order = 2, name = "Agachamento", muscleGroup = "Pernas", sets = 4, reps = "10", weightKg = 30)
        )
        rvExercises.adapter = ExerciseAdapter(defaultExercises)

        val studentId = intent.getStringExtra("STUDENT_ID") ?: auth.currentUser?.uid
        val workoutId = intent.getStringExtra("WORKOUT_ID")

        if (!studentId.isNullOrBlank()) {
            lifecycleScope.launch {
                val flow = if (!workoutId.isNullOrBlank()) {
                    repository.getWorkoutExercises(studentId, workoutId)
                } else {
                    // Busca os exercícios do Treino do Dia se não veio WORKOUT_ID específico
                    kotlinx.coroutines.flow.flow {
                        repository.getWorkoutOfTheDay(studentId).collect { pair ->
                            if (pair != null) emit(pair.second)
                        }
                    }
                }

                flow.catch { }
                    .collect { firestoreExercises ->
                        if (firestoreExercises.isNotEmpty()) {
                            val uiExercises = firestoreExercises.map {
                                Exercise(
                                    order = it.order,
                                    name = it.name,
                                    muscleGroup = it.muscleGroup,
                                    sets = it.sets,
                                    reps = it.reps,
                                    weightKg = it.weightKg.toInt()
                                )
                            }
                            rvExercises.adapter = ExerciseAdapter(uiExercises)
                        }
                    }
            }
        }

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
            startActivity(Intent(this, com.example.telacadastro.CreateWorkoutActivity::class.java))
        }
        findViewById<android.view.View>(R.id.btnStartWorkout).setOnClickListener {
            startActivity(Intent(this, WorkoutInProgressActivity::class.java))
        }
    }
}
