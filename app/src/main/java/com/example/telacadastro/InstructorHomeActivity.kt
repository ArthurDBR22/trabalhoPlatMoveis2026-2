package com.example.telacadastro

import com.example.myapplication.R
import com.example.myapplication.ConfiguracoesActivity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InstructorHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_home)

        // Top bar icons
        findViewById<ImageView>(R.id.btnCalendar)?.setOnClickListener {
            startActivity(Intent(this, InstructorAgendaActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnNotification)?.setOnClickListener {
            Toast.makeText(this, "Notificações do Instrutor", Toast.LENGTH_SHORT).show()
        }

        // Quick Actions
        val openStudents = { startActivity(Intent(this, StudentsListActivity::class.java)) }
        val openWorkout = { startActivity(Intent(this, CreateWorkoutActivity::class.java)) }
        val openAgenda = { startActivity(Intent(this, InstructorAgendaActivity::class.java)) }
        val openEvaluations = { Toast.makeText(this, "Abrindo avaliações dos alunos...", Toast.LENGTH_SHORT).show() }

        findViewById<LinearLayout>(R.id.cardAddStudent)?.setOnClickListener { openStudents() }
        findViewById<TextView>(R.id.txtSeeAllActions)?.setOnClickListener { openStudents() }

        findViewById<LinearLayout>(R.id.cardCreateWorkout)?.setOnClickListener { openWorkout() }

        findViewById<LinearLayout>(R.id.cardScheduleClass)?.setOnClickListener { openAgenda() }
        findViewById<TextView>(R.id.txtSeeAllClasses)?.setOnClickListener { openAgenda() }
        findViewById<LinearLayout>(R.id.cardClass1)?.setOnClickListener { openAgenda() }
        findViewById<LinearLayout>(R.id.cardClass2)?.setOnClickListener { openAgenda() }

        findViewById<LinearLayout>(R.id.cardViewEvaluations)?.setOnClickListener { openEvaluations() }

        // Bottom Navigation Bar
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            // Already here
        }

        findViewById<LinearLayout>(R.id.navAlunos)?.setOnClickListener {
            openStudents()
        }

        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            openWorkout()
        }

        findViewById<LinearLayout>(R.id.navAgenda)?.setOnClickListener {
            openAgenda()
        }

        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }
    }
}
