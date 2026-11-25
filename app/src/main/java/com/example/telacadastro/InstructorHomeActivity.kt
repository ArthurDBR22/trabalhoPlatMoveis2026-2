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
import com.example.myapplication.data.seed.UnifitDatabaseSeeder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class InstructorHomeActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
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

    private fun loadProfessorProfile() {
        val uid = auth.currentUser?.uid ?: return
        val txtGreeting = findViewById<TextView>(R.id.txtGreeting)
        lifecycleScope.launch {
            val user = repository.getUserProfile(uid)
            if (user != null && user.name.isNotBlank()) {
                txtGreeting?.text = "Olá, ${user.name}!"
            }
        }
    }

    private fun showSeedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Popular Banco de Dados (Firebase)")
            .setMessage(
                "Deseja criar/atualizar no Firebase Auth e Firestore:\n\n" +
                    "• 1 conta Admin/Professor (admin@unifit.com)\n" +
                    "• 4 contas de Alunos\n" +
                    "• 3 exercícios no catálogo\n" +
                    "• 1 Treino do Dia atribuído aos alunos?"
            )
            .setPositiveButton("Executar agora") { _, _ ->
                runSeeder()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun runSeeder() {
        Toast.makeText(this, "Criando contas e treinos no Firebase...", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val result = UnifitDatabaseSeeder(applicationContext).seedAll()
            result.onSuccess { summary ->
                val details = buildString {
                    appendLine("Contas criadas/validadas (senha: ${UnifitDatabaseSeeder.DEFAULT_PASSWORD}):")
                    summary.accountsCreatedOrVerified.forEach { appendLine("• $it") }
                    appendLine("\nExercícios adicionados:")
                    summary.exercisesAdded.forEach { appendLine("• $it") }
                    appendLine("\nTreino do Dia montado:")
                    appendLine("• ${summary.workoutOfDayTitle} (${summary.assignedStudentsCount} alunos)")
                }
                AlertDialog.Builder(this@InstructorHomeActivity)
                    .setTitle("Banco Populado com Sucesso!")
                    .setMessage(details)
                    .setPositiveButton("OK", null)
                    .show()
            }.onFailure { err ->
                AlertDialog.Builder(this@InstructorHomeActivity)
                    .setTitle("Erro ao Popular Firebase")
                    .setMessage(
                        "Não foi possível concluir a gravação no Firebase:\n${err.localizedMessage}\n\n" +
                            "Verifique no Firebase Console se:\n" +
                            "1. Authentication > E-mail/Senha está ativado\n" +
                            "2. Firestore Database foi criado e as regras (firestore.rules) foram publicadas."
                    )
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}
