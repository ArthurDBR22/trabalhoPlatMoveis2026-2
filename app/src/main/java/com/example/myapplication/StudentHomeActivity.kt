package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.telacadastro.CreateWorkoutActivity
import com.example.telacadastro.StudentExercisesActivity
import com.example.telasocialepublicacao.SocialActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StudentHomeActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        loadStudentHomeData()

        findViewById<ImageButton>(R.id.btnNotificacao)?.setOnClickListener {
            startActivity(Intent(this, NotificacoesActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnConfiguracoes)?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        val openProfile = { startActivity(Intent(this, StudentProfileActivity::class.java)) }
        findViewById<CardView>(R.id.cardProfileSummary)?.setOnClickListener { openProfile() }
        findViewById<Button>(R.id.btnEditProfileCard)?.setOnClickListener { openProfile() }

        val openExercises = { startActivity(Intent(this, ExerciciosActivity::class.java)) }
        val openWorkoutInProgress = { startActivity(Intent(this, WorkoutInProgressActivity::class.java)) }

        findViewById<TextView>(R.id.btnVerTodosTreinos)?.setOnClickListener { openExercises() }
        findViewById<CardView>(R.id.cardTreinoDoDia)?.setOnClickListener { openExercises() }
        findViewById<Button>(R.id.btnIniciarTreinoHoje)?.setOnClickListener { openWorkoutInProgress() }

        findViewById<CardView>(R.id.cardHistorico)?.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<CardView>(R.id.cardSocial)?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
        }

        findViewById<CardView>(R.id.cardMeusTreinos)?.setOnClickListener {
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
        }

        // Bottom Navigation Bar (4 tabs: Início, Treinos, Social, Perfil)
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        val navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        val navSocial = findViewById<LinearLayout>(R.id.navSocial)
        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio?.setOnClickListener {
            // Already here
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

    private fun loadStudentHomeData() {
        val txtGreeting = findViewById<TextView>(R.id.txtGreeting)
        val txtStudentName = findViewById<TextView>(R.id.txtStudentName)
        val txtStudentHandle = findViewById<TextView>(R.id.txtStudentHandle)
        val txtStatTreinos = findViewById<TextView>(R.id.txtStatTreinos)
        val txtStatDias = findViewById<TextView>(R.id.txtStatDias)
        val txtStatConquistas = findViewById<TextView>(R.id.txtStatConquistas)
        val txtStatFreq = findViewById<TextView>(R.id.txtStatFreq)

        // Estado inicial de carregamento ou "vazio" até o Firebase responder
        txtGreeting?.text = "Olá, ..."
        txtStudentName?.text = "Carregando..."
        txtStudentHandle?.text = "@usuario"
        txtStatTreinos?.text = "0"
        txtStatDias?.text = "0"
        txtStatConquistas?.text = "0"
        txtStatFreq?.text = "0%"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val docRef = firestore.collection("users").document(userId)
                val doc = docRef.get().await()
                if (doc.exists()) {
                    val name = doc.getString("name") ?: "Aluno"
                    val handle = doc.getString("handle") ?: "@usuario"
                    val treinos = doc.getLong("completedWorkouts")?.toString() ?: "0"
                    val dias = doc.getLong("consecutiveDays")?.toString() ?: "0"
                    val conquistas = doc.getLong("achievements")?.toString() ?: "0"
                    val freq = doc.getString("frequency") ?: "0%"

                    withContext(Dispatchers.Main) {
                        txtGreeting?.text = "Olá, ${name.substringBefore(" ")}!"
                        txtStudentName?.text = name
                        txtStudentHandle?.text = if (handle.startsWith("@")) handle else "@$handle"
                        txtStatTreinos?.text = treinos
                        txtStatDias?.text = dias
                        txtStatConquistas?.text = conquistas
                        txtStatFreq?.text = freq
                    }
                }
            } catch (e: Exception) {
                // Manter zeros se falhar
            }
        }
    }
}
