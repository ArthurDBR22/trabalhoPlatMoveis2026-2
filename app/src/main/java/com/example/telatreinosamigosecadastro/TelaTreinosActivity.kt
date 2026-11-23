package com.example.telatreinosamigosecadastro

import com.example.myapplication.R
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.HistoryActivity
import com.example.myapplication.StudentHomeActivity
import com.example.myapplication.StudentProfileActivity
import com.example.myapplication.WorkoutInProgressActivity
import com.example.telacadastro.CreateWorkoutActivity
import com.example.telacadastro.StudentExercisesActivity
import com.example.telasocialepublicacao.SocialActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class TelaTreinosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_treinos)

        val btnNotificacao = findViewById<ImageButton>(R.id.btnNotificacao)
        val btnConfiguracoes = findViewById<ImageButton>(R.id.btnConfiguracoes)

        btnNotificacao?.setOnClickListener {
            startActivity(Intent(this, com.example.myapplication.NotificacoesActivity::class.java))
        }

        btnConfiguracoes?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.selectedItemId = R.id.nav_workout

        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, StudentHomeActivity::class.java))
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
                    startActivity(Intent(this, StudentProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
