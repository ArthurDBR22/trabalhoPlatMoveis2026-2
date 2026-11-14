package com.example.telacadastro

import com.example.myapplication.R
import com.example.myapplication.ConfiguracoesActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_students)

        findViewById<ImageView>(R.id.btnCalendar)?.setOnClickListener {
            startActivity(Intent(this, InstructorAgendaActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnNotification)?.setOnClickListener {
            Toast.makeText(this, "Notificações de Alunos", Toast.LENGTH_SHORT).show()
        }

        val recyclerStudents = findViewById<RecyclerView>(R.id.recyclerStudents)
        recyclerStudents.layoutManager = LinearLayoutManager(this)

        val listaAlunos = listOf("Mariana Costa", "Lucas Ferreira", "Juliana Lima", "João Silva")

        recyclerStudents.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = layoutInflater.inflate(R.layout.item_student, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    startActivity(Intent(this@StudentsListActivity, StudentDetailsActivity::class.java))
                }
            }
            override fun getItemCount(): Int = listaAlunos.size
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, InstructorHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navAlunos)?.setOnClickListener {
            // Already here
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
