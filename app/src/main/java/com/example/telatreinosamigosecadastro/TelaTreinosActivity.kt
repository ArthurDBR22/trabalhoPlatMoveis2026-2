package com.example.telatreinosamigosecadastro

import com.example.myapplication.R
import com.example.myapplication.CustomWorkoutActivity
import com.example.myapplication.HistoryActivity
import com.example.myapplication.WorkoutInProgressActivity
import com.example.telacadastro.CreateWorkoutActivity
import com.example.telacadastro.StudentExercisesActivity
import com.example.telasocialepublicacao.SocialActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class TelaTreinosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_treinos)

        val btnNotificacao = findViewById<ImageButton>(R.id.btnNotificacao)
        val btnConfiguracoes = findViewById<ImageButton>(R.id.btnConfiguracoes)

        btnNotificacao.setOnClickListener {
            Toast.makeText(this, "Notificações clicadas", Toast.LENGTH_SHORT).show()
        }

        btnConfiguracoes.setOnClickListener {
            Toast.makeText(this, "Configurações clicadas", Toast.LENGTH_SHORT).show()
        }

        val btnVerExercicios = findViewById<Button>(R.id.btnVerExercicios)
        val btnAcessarExercicios = findViewById<Button>(R.id.btnAcessarExercicios)
        val btnCriarTreino = findViewById<Button>(R.id.btnCriarTreino)
        val btnVerCronometro = findViewById<Button>(R.id.btnVerCronometro)
        val btnVerHistorico = findViewById<Button>(R.id.btnVerHistorico)

        val cardExercicios = findViewById<CardView>(R.id.cardExercicios)
        val cardCriarTreino = findViewById<CardView>(R.id.cardCriarTreino)
        val cardCronometro = findViewById<CardView>(R.id.cardCronometro)
        val cardHistorico = findViewById<CardView>(R.id.cardHistorico)

        val openExercicios = { startActivity(Intent(this, StudentExercisesActivity::class.java)) }
        val openCriarTreino = { startActivity(Intent(this, CreateWorkoutActivity::class.java)) }
        val openCronometro = { startActivity(Intent(this, WorkoutInProgressActivity::class.java)) }
        val openHistorico = { startActivity(Intent(this, HistoryActivity::class.java)) }

        cardExercicios?.setOnClickListener { openExercicios() }
        btnVerExercicios?.setOnClickListener { openExercicios() }
        btnAcessarExercicios?.setOnClickListener { openExercicios() }

        cardCriarTreino?.setOnClickListener { openCriarTreino() }
        btnCriarTreino?.setOnClickListener { openCriarTreino() }

        cardCronometro?.setOnClickListener { openCronometro() }
        btnVerCronometro?.setOnClickListener { openCronometro() }

        cardHistorico?.setOnClickListener { openHistorico() }
        btnVerHistorico?.setOnClickListener { openHistorico() }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_workout

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, CustomWorkoutActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_workout -> {
                    true
                }
                R.id.nav_social -> {
                    startActivity(Intent(this, SocialActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
