package com.example.telatreinosamigosecadastro

import com.example.myapplication.R

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DecisaoCadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decisao_cadastro)

        var cardEstudante = findViewById<LinearLayout>(R.id.cardEstudante)
        var btnContinuarEstudante = findViewById<Button>(R.id.btnContinuarEstudante)

        var cardInstrutor = findViewById<LinearLayout>(R.id.cardInstrutor)
        var btnContinuarInstrutor = findViewById<Button>(R.id.btnContinuarInstrutor)

        val acaoEstudante = {
            Toast.makeText(this, "Iniciando cadastro como Estudante...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, com.example.telacadastro.CadastroActivity::class.java))
        }

        btnContinuarEstudante.setOnClickListener { acaoEstudante() }
        cardEstudante.setOnClickListener { acaoEstudante() }

        val acaoInstrutor = {
            Toast.makeText(this, "Iniciando cadastro como Instrutor...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, com.example.telacadastro.InstrutorActivity::class.java))
        }

        btnContinuarInstrutor.setOnClickListener { acaoInstrutor() }
        cardInstrutor.setOnClickListener { acaoInstrutor() }
    }
}