package com.example.telatreinosamigosecadastro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MeusAmigosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_amigos)

        var btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        var btnIrAdicionarAmigo = findViewById<Button>(R.id.btnIrAdicionarAmigo)
        btnIrAdicionarAmigo.setOnClickListener {
            var intent = Intent(this, AdicionarAmigosActivity::class.java)
            startActivity(intent)
        }

        var tabFiltroTodos = findViewById<TextView>(R.id.tabFiltroTodos)
        var tabFiltroTreino = findViewById<TextView>(R.id.tabFiltroTreino)
        var tabFiltroOnline = findViewById<TextView>(R.id.tabFiltroOnline)

        tabFiltroTodos.setOnClickListener {
            tabFiltroTodos.setBackgroundResource(R.drawable.bg_filter_active)
            tabFiltroTodos.setTextColor(resources.getColor(android.R.color.white, null))

            tabFiltroTreino.setBackgroundResource(R.drawable.bg_filter_inactive)
            tabFiltroTreino.setTextColor(resources.getColor(android.R.color.darker_gray, null))

            tabFiltroOnline.setBackgroundResource(R.drawable.bg_filter_inactive)
            tabFiltroOnline.setTextColor(resources.getColor(android.R.color.darker_gray, null))

            Toast.makeText(this, "Exibindo todos os amigos", Toast.LENGTH_SHORT).show()
        }

        tabFiltroTreino.setOnClickListener {
            tabFiltroTreino.setBackgroundResource(R.drawable.bg_filter_active)
            tabFiltroTreino.setTextColor(resources.getColor(android.R.color.white, null))

            tabFiltroTodos.setBackgroundResource(R.drawable.bg_filter_inactive)
            tabFiltroTodos.setTextColor(resources.getColor(android.R.color.darker_gray, null))

            tabFiltroOnline.setBackgroundResource(R.drawable.bg_filter_inactive)
            tabFiltroOnline.setTextColor(resources.getColor(android.R.color.darker_gray, null))

            Toast.makeText(this, "Exibindo amigos de treino", Toast.LENGTH_SHORT).show()
        }

        tabFiltroOnline.setOnClickListener {
            tabFiltroOnline.setBackgroundResource(R.drawable.bg_filter_active)
            tabFiltroOnline.setTextColor(resources.getColor(android.R.color.white, null))

            tabFiltroTodos.setBackgroundResource(R.drawable.bg_filter_inactive)
            tabFiltroTodos.setTextColor(resources.getColor(android.R.color.darker_gray, null))

            tabFiltroTreino.setBackgroundResource(R.drawable.bg_filter_inactive)
            tabFiltroTreino.setTextColor(resources.getColor(android.R.color.darker_gray, null))

            Toast.makeText(this, "Exibindo amigos online", Toast.LENGTH_SHORT).show()
        }

        var btnChatMariana = findViewById<ImageButton>(R.id.btnChatMariana)
        var btnChatRafael = findViewById<ImageButton>(R.id.btnChatRafael)

        btnChatMariana.setOnClickListener {
            Toast.makeText(this, "Abrindo chat com Mariana Costa...", Toast.LENGTH_SHORT).show()
        }

        btnChatRafael.setOnClickListener {
            Toast.makeText(this, "Abrindo chat com Rafael Silva...", Toast.LENGTH_SHORT).show()
        }

        var navInicio = findViewById<LinearLayout>(R.id.navInicio)
        var navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        var navSocial = findViewById<LinearLayout>(R.id.navSocial)
        var navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio.setOnClickListener {
            Toast.makeText(this, "Navegando para Início", Toast.LENGTH_SHORT).show()
        }

        navTreinos.setOnClickListener {
            var intent = Intent(this, TelaTreinosActivity::class.java)
            startActivity(intent)
            finish()
        }

        navSocial.setOnClickListener {}

        navPerfil.setOnClickListener {
            Toast.makeText(this, "Navegando para Perfil", Toast.LENGTH_SHORT).show()
        }
    }
}
