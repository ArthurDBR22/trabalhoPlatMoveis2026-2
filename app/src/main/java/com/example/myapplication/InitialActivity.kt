package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)

        findViewById<Button>(R.id.btnCadastrar).setOnClickListener {
            startActivity(Intent(this, com.example.telatreinosamigosecadastro.DecisaoCadastroActivity::class.java))
        }

        findViewById<Button>(R.id.btnEntrar).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}