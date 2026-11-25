package com.example.telatreinosamigosecadastro

import com.example.myapplication.R
import com.example.myapplication.ExerciciosActivity
import com.example.myapplication.StudentHomeActivity
import com.example.myapplication.StudentProfileActivity
import com.example.telasocialepublicacao.SocialActivity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        val btnIrAdicionarAmigo = findViewById<Button>(R.id.btnIrAdicionarAmigo)
        btnIrAdicionarAmigo.setOnClickListener {
            val intent = Intent(this, AdicionarAmigosActivity::class.java)
            startActivity(intent)
        }

        val tabFiltroTodos = findViewById<TextView>(R.id.tabFiltroTodos)
        val tabFiltroTreino = findViewById<TextView>(R.id.tabFiltroTreino)
        val tabFiltroOnline = findViewById<TextView>(R.id.tabFiltroOnline)

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

        // Navbar integration
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        val navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        val navSocial = findViewById<LinearLayout>(R.id.navSocial)
        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio?.setOnClickListener {
            startActivity(Intent(this, StudentHomeActivity::class.java))
            finish()
        }

        navTreinos?.setOnClickListener {
            startActivity(Intent(this, ExerciciosActivity::class.java))
            finish()
        }

        navSocial?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
        }

        navPerfil?.setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
            finish()
        }
    }
}
