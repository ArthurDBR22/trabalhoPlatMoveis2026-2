package com.example.telatreinosamigosecadastro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TelaTreinosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_treinos)

        // 1. Interações do Cabeçalho Superior[cite: 1]
        val btnNotificacao = findViewById<ImageButton>(R.id.btnNotificacao)
        val btnConfiguracoes = findViewById<ImageButton>(R.id.btnConfiguracoes)

        btnNotificacao.setOnClickListener {
            Toast.makeText(this, "Notificações clicadas", Toast.LENGTH_SHORT).show()
        }

        btnConfiguracoes.setOnClickListener {
            Toast.makeText(this, "Configurações clicadas", Toast.LENGTH_SHORT).show()
        }

        var btnVerExercicios = findViewById<Button>(R.id.btnVerExercicios)
        var btnAcessarExercicios = findViewById<Button>(R.id.btnAcessarExercicios)
        var btnCriarTreino = findViewById<Button>(R.id.btnCriarTreino)
        var btnVerCronometro = findViewById<Button>(R.id.btnVerCronometro)
        var btnVerHistorico = findViewById<Button>(R.id.btnVerHistorico)

        btnVerExercicios.setOnClickListener {
            Toast.makeText(this, "Abrindo biblioteca de exercícios...", Toast.LENGTH_SHORT).show()
        }

        btnAcessarExercicios.setOnClickListener {
            Toast.makeText(this, "Abrindo exercícios da academia...", Toast.LENGTH_SHORT).show()
        }

        btnCriarTreino.setOnClickListener {
            Toast.makeText(this, "Abrindo criação de treino personalizado...", Toast.LENGTH_SHORT).show()
        }

        btnVerCronometro.setOnClickListener {
            Toast.makeText(this, "Abrindo cronômetro...", Toast.LENGTH_SHORT).show()
        }

        btnVerHistorico.setOnClickListener {
            Toast.makeText(this, "Abrindo histórico de treinos...", Toast.LENGTH_SHORT).show()
        }

        var navInicio = findViewById<LinearLayout>(R.id.navInicio)
        var navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        var navSocial = findViewById<LinearLayout>(R.id.navSocial)
        var navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio.setOnClickListener {
            Toast.makeText(this, "Navegando para Início", Toast.LENGTH_SHORT).show()
        }

        navTreinos.setOnClickListener {
            Toast.makeText(this, "Você já está na tela de Treinos", Toast.LENGTH_SHORT).show()
        }

        navSocial.setOnClickListener {
            Toast.makeText(this, "Navegando para Social", Toast.LENGTH_SHORT).show()
        }

        navPerfil.setOnClickListener {
            Toast.makeText(this, "Navegando para Perfil", Toast.LENGTH_SHORT).show()
        }
    }
}