package com.example.unifit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotificacoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacoes)

        configurarBotoes()
    }

    private fun configurarBotoes() {

        val btnVoltar = findViewById<TextView>(
            R.id.btnVoltarNotificacoes
        )

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}