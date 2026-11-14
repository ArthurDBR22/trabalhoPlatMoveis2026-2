package com.example.telacadastro

import com.example.myapplication.R
import com.example.myapplication.ConfiguracoesActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btnNotification)?.setOnClickListener {
            Toast.makeText(this, "Notificações", Toast.LENGTH_SHORT).show()
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, InstructorHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navAlunos)?.setOnClickListener {
            startActivity(Intent(this, StudentsListActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navAgenda)?.setOnClickListener {
            startActivity(Intent(this, InstructorAgendaActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }
    }
}
