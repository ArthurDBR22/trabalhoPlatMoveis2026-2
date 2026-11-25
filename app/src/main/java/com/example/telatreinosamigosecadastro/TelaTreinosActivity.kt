package com.example.telatreinosamigosecadastro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.CustomWorkoutActivity
import com.example.myapplication.HistoryActivity
import com.example.myapplication.NotificacoesActivity
import com.example.myapplication.R
import com.example.myapplication.StudentHomeActivity
import com.example.myapplication.StudentProfileActivity
import com.example.myapplication.WorkoutInProgressActivity
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.example.telacadastro.StudentExercisesActivity
import com.example.telasocialepublicacao.SocialActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TelaTreinosActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val repository = UnifitRepositoryImpl()
    private var workoutOfDayId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_treinos)

        loadStudentAndWorkoutOfDay()

        // Top Header buttons
        findViewById<ImageButton>(R.id.btnNotificacao)?.setOnClickListener {
            startActivity(Intent(this, NotificacoesActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnConfiguracoes)?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        // Profile Card summary -> Open Profile
        val openProfile = { startActivity(Intent(this, StudentProfileActivity::class.java)) }
        findViewById<CardView>(R.id.cardProfileSummary)?.setOnClickListener { openProfile() }

        // Treino do dia / Iniciar
        val openExercises = {
            val intent = Intent(this, CustomWorkoutActivity::class.java).apply {
                putExtra("STUDENT_ID", auth.currentUser?.uid)
                putExtra("WORKOUT_ID", workoutOfDayId)
            }
            startActivity(intent)
        }
        val openWorkoutInProgress = { startActivity(Intent(this, WorkoutInProgressActivity::class.java)) }

        findViewById<TextView>(R.id.btnVerTodosTreinos)?.setOnClickListener {
            startActivity(Intent(this, StudentExercisesActivity::class.java))
        }
        findViewById<CardView>(R.id.cardTreinoDoDia)?.setOnClickListener { openExercises() }
        findViewById<Button>(R.id.btnIniciarTreinoHoje)?.setOnClickListener { openWorkoutInProgress() }

        // Navigation Cards
        findViewById<CardView>(R.id.cardHistorico)?.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<CardView>(R.id.cardSocial)?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
        }

        findViewById<CardView>(R.id.cardMeusTreinos)?.setOnClickListener {
            openExercises()
        }

        // BottomNavigationView (versão do servidor)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        if (bottomNav != null) {
            bottomNav.selectedItemId = R.id.nav_workout
            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this, StudentHomeActivity::class.java))
                        finish()
                        true
                    }
                    R.id.nav_workout -> true
                    R.id.nav_social -> {
                        startActivity(Intent(this, SocialActivity::class.java))
                        finish()
                        true
                    }
                    R.id.nav_profile -> {
                        startActivity(Intent(this, StudentProfileActivity::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }
        } else {
            // Fallback caso activity_tela_treinos.xml use LinearLayout na barra inferior
            findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
                startActivity(Intent(this, StudentHomeActivity::class.java))
                finish()
            }
            findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
                // Already here
            }
            findViewById<LinearLayout>(R.id.navSocial)?.setOnClickListener {
                startActivity(Intent(this, SocialActivity::class.java))
                finish()
            }
            findViewById<LinearLayout>(R.id.navAgenda)?.setOnClickListener {
                Toast.makeText(this, "Agenda de aulas do aluno", Toast.LENGTH_SHORT).show()
            }
            findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
                startActivity(Intent(this, StudentProfileActivity::class.java))
                finish()
            }
        }
    }

    private fun loadStudentAndWorkoutOfDay() {
        val uid = auth.currentUser?.uid ?: return

        val txtGreetingStudent = findViewById<TextView>(R.id.txtGreetingStudent)
        val txtProfileName = findViewById<TextView>(R.id.txtProfileName)
        val txtProfileEmail = findViewById<TextView>(R.id.txtProfileEmail)

        val txtWorkoutDayTitle = findViewById<TextView>(R.id.txtWorkoutDayTitle)
        val txtWorkoutDayDesc = findViewById<TextView>(R.id.txtWorkoutDayDesc)
        val txtWorkoutDayDuration = findViewById<TextView>(R.id.txtWorkoutDayDuration)
        val txtWorkoutDayExercises = findViewById<TextView>(R.id.txtWorkoutDayExercises)
        val txtWorkoutDayLevel = findViewById<TextView>(R.id.txtWorkoutDayLevel)

        lifecycleScope.launch {
            val user = repository.getUserProfile(uid)
            if (user != null) {
                val firstName = user.name.substringBefore(" ").ifBlank { "Aluno" }
                txtGreetingStudent?.text = "Olá, $firstName!"
                txtProfileName?.text = user.name.ifBlank { user.email }
                txtProfileEmail?.text = user.email
            }
        }

        lifecycleScope.launch {
            repository.getWorkoutOfTheDay(uid)
                .catch { }
                .collect { pair ->
                    if (pair != null) {
                        val (plan, exercises) = pair
                        workoutOfDayId = plan.id
                        txtWorkoutDayTitle?.text = plan.title
                        txtWorkoutDayDesc?.text = plan.description.ifBlank { "Objetivo: ${plan.goal}" }
                        txtWorkoutDayDuration?.text = "⏱️ Duração: ${plan.averageDurationMinutes} min"
                        txtWorkoutDayExercises?.text = "🏋️ Exercícios: ${exercises.size}"
                        txtWorkoutDayLevel?.text = "📊 Nível: ${plan.level}"
                    }
                }
        }
    }
}