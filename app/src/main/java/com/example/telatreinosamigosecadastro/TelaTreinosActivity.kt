package com.example.telatreinosamigosecadastro

import com.example.myapplication.R
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.HistoryActivity
import com.example.myapplication.NotificacoesActivity
import com.example.myapplication.StudentProfileActivity
import com.example.myapplication.WorkoutInProgressActivity
import com.example.telacadastro.CreateWorkoutActivity
import com.example.telacadastro.StudentExercisesActivity
import com.example.telasocialepublicacao.SocialActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class TelaTreinosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_treinos)

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
        val openExercises = { startActivity(Intent(this, StudentExercisesActivity::class.java)) }
        val openWorkoutInProgress = { startActivity(Intent(this, WorkoutInProgressActivity::class.java)) }

        findViewById<TextView>(R.id.btnVerTodosTreinos)?.setOnClickListener { openExercises() }
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
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
        }

        // Bottom Navigation Bar
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        val navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        val navSocial = findViewById<LinearLayout>(R.id.navSocial)
        val navAgenda = findViewById<LinearLayout>(R.id.navAgenda)
        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio?.setOnClickListener {
            // Already here
        }

        navTreinos?.setOnClickListener {
            startActivity(Intent(this, StudentExercisesActivity::class.java))
        }

        navSocial?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
        }

        navAgenda?.setOnClickListener {
            Toast.makeText(this, "Agenda de aulas do aluno", Toast.LENGTH_SHORT).show()
        }

        navPerfil?.setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
            finish()
        }
    }
}
