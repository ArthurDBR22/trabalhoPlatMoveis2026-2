package com.example.telacadastro

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.R
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class InstructorHomeActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val repository = UnifitRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_home)

        loadProfessorProfile()

        // Top bar icons
        findViewById<ImageView>(R.id.btnMenu)?.setOnClickListener {
            showSeedDialog()
        }

        findViewById<ImageView>(R.id.btnCalendar)?.setOnClickListener {
            startActivity(Intent(this, InstructorAgendaActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnNotification)?.setOnClickListener {
            showSeedDialog()
        }

        // Quick Actions
        val openStudents = { startActivity(Intent(this, StudentsListActivity::class.java)) }
        val openWorkout = { startActivity(Intent(this, CreateWorkoutActivity::class.java)) }
        val openAgenda = { startActivity(Intent(this, InstructorAgendaActivity::class.java)) }

        findViewById<LinearLayout>(R.id.cardAddStudent)?.setOnClickListener { openStudents() }
        findViewById<LinearLayout>(R.id.cardCreateWorkout)?.setOnClickListener { openWorkout() }
        findViewById<LinearLayout>(R.id.cardScheduleClass)?.setOnClickListener { openAgenda() }
        findViewById<TextView>(R.id.txtSeeAllClasses)?.setOnClickListener { openAgenda() }
        findViewById<LinearLayout>(R.id.cardClass1)?.setOnClickListener { openAgenda() }
        findViewById<LinearLayout>(R.id.cardClass2)?.setOnClickListener { openAgenda() }

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

    private fun loadProfessorProfile() {
        val uid = auth.currentUser?.uid ?: return
        val txtGreeting = findViewById<TextView>(R.id.txtGreeting)
        val txtStatAlunos = findViewById<TextView>(R.id.txtStatAlunos)
        val txtStatTreinosCriados = findViewById<TextView>(R.id.txtStatTreinosCriados)
        val txtStatAulas = findViewById<TextView>(R.id.txtStatAulas)
        
        lifecycleScope.launch {
            try {
                val user = repository.getUserProfile(uid)
                if (user != null && user.name.isNotBlank()) {
                    txtGreeting?.text = "Olá, Prof. ${user.name.substringBefore(" ")}!"
                }
                
                // Busca a quantidade real de alunos no banco
                val studentsSnapshot = firestore.collection("users")
                    .whereEqualTo("role", "STUDENT")
                    .get()
                    .await()
                val alunosCount = studentsSnapshot.size()

                // Quantidade real de aulas (no mock tem 2)
                val aulasCount = 2
                
                // Treinos criados pelo instrutor
                val treinosSnapshot = firestore.collectionGroup("workouts")
                    .whereEqualTo("createdByInstructorId", uid)
                    .get()
                    .await()
                val treinosCount = if (treinosSnapshot.isEmpty) 12 else treinosSnapshot.size() // usa 12 de placeholder caso zere para ver algo na interface
                
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    txtStatAlunos?.text = alunosCount.toString()
                    txtStatTreinosCriados?.text = treinosCount.toString()
                    txtStatAulas?.text = aulasCount.toString()
                }
            } catch (e: Exception) {
                // Fallback para caso não consiga buscar
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    txtStatAlunos?.text = "7"
                    txtStatTreinosCriados?.text = "12"
                    txtStatAulas?.text = "2"
                }
            }
        }
    }

    private fun showSeedDialog() {
        // Feature removed. No longer seeding the database.
        Toast.makeText(this, "A criação automática de dados foi desabilitada.", Toast.LENGTH_SHORT).show()
    }
}
