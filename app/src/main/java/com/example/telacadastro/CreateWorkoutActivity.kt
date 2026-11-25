package com.example.telacadastro

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.CustomWorkoutActivity
import com.example.myapplication.Exercise
import com.example.myapplication.ExerciseAdapter
import com.example.myapplication.R
import com.example.myapplication.data.model.LibraryExercise
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.WorkoutExercise
import com.example.myapplication.data.model.WorkoutPlan
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateWorkoutActivity : AppCompatActivity() {

    private val repository = UnifitRepositoryImpl()
    private val auth = FirebaseAuth.getInstance()

    private var studentsList: List<User> = emptyList()
    private var selectedStudent: User? = null
    private var libraryExercises: List<LibraryExercise> = emptyList()
    private val currentExercises = mutableListOf<WorkoutExercise>()

    private lateinit var recyclerExercises: RecyclerView
    private lateinit var txtSelectedStudentName: TextView
    private lateinit var txtSelectedStudentInfo: TextView
    private lateinit var txtExercisesCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        txtSelectedStudentName = findViewById(R.id.txtSelectedStudentName)
        txtSelectedStudentInfo = findViewById(R.id.txtSelectedStudentInfo)
        txtExercisesCount = findViewById(R.id.txtExercisesCount)
        recyclerExercises = findViewById(R.id.recyclerExercises)
        recyclerExercises.layoutManager = LinearLayoutManager(this)

        val edtTitle = findViewById<EditText>(R.id.edtWorkoutTitle)
        val edtGoal = findViewById<EditText>(R.id.edtWorkoutGoal)
        val edtLevel = findViewById<EditText>(R.id.edtWorkoutLevel)
        val edtDuration = findViewById<EditText>(R.id.edtWorkoutDuration)
        val edtDescription = findViewById<EditText>(R.id.edtWorkoutDescription)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        // Preenche inicialmente com os 3 exercícios padrão do treino do dia
        loadInitialThreeExercises()

        // Se veio aluno pela Intent
        val intentStudentId = intent.getStringExtra("STUDENT_ID")
        val intentStudentName = intent.getStringExtra("STUDENT_NAME")
        if (!intentStudentId.isNullOrBlank()) {
            selectedStudent = User(id = intentStudentId, name = intentStudentName ?: "Aluno")
            updateSelectedStudentUI()
        }

        observeStudents(intentStudentId)
        observeLibraryExercises()

        findViewById<Button>(R.id.btnChangeStudent)?.setOnClickListener {
            showStudentSelectorDialog()
        }

        findViewById<Button>(R.id.btnAddExercise)?.setOnClickListener {
            showAddExerciseDialog()
        }

        findViewById<Button>(R.id.btnPreviewWorkout)?.setOnClickListener {
            val previewIntent = Intent(this, CustomWorkoutActivity::class.java).apply {
                putExtra("STUDENT_ID", selectedStudent?.id)
            }
            startActivity(previewIntent)
        }

        findViewById<Button>(R.id.btnSaveWorkout)?.setOnClickListener {
            val student = selectedStudent
            if (student == null || student.id.isBlank()) {
                Toast.makeText(this, "Selecione um aluno antes de salvar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentExercises.isEmpty()) {
                Toast.makeText(this, "Adicione ao menos 1 exercício ao treino.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val title = edtTitle.text.toString().trim().ifBlank { "Treino do Dia" }
            val goal = edtGoal.text.toString().trim().ifBlank { "Hipertrofia" }
            val level = edtLevel.text.toString().trim().ifBlank { "Intermediário" }
            val duration = edtDuration.text.toString().trim().toIntOrNull() ?: 50
            val description = edtDescription.text.toString().trim()

            val workoutPlan = WorkoutPlan(
                id = "treino_do_dia_padrao",
                title = title,
                goal = goal,
                level = level,
                description = description,
                frequencyWeekly = listOf("Seg", "Qua", "Sex"),
                averageDurationMinutes = duration,
                cycleWeeks = 4,
                exercisesCount = currentExercises.size,
                createdByInstructorId = auth.currentUser?.uid,
                isActive = true,
                isWorkoutOfDay = true
            )

            lifecycleScope.launch {
                val result = repository.saveWorkoutWithExercises(student.id, workoutPlan, currentExercises)
                result.onSuccess {
                    Toast.makeText(
                        this@CreateWorkoutActivity,
                        "Treino do Dia salvo no Firestore para ${student.name}!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }.onFailure { error ->
                    Toast.makeText(
                        this@CreateWorkoutActivity,
                        "Erro ao salvar no Firestore: ${error.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        setupBottomNavigation()
    }

    private fun loadInitialThreeExercises() {
        currentExercises.clear()
        
        // Use default exercises since seeder is no longer available
        val defaultExercises = listOf(
            WorkoutExercise(
                id = "ex_dia_1",
                order = 1,
                name = "Supino Reto",
                muscleGroup = "Peitoral",
                sets = 4,
                reps = "10-12",
                weightKg = 20.0,
                equipment = "Barra",
                notes = "Foque na contração"
            ),
            WorkoutExercise(
                id = "ex_dia_2",
                order = 2,
                name = "Agachamento Livre",
                muscleGroup = "Pernas",
                sets = 4,
                reps = "10",
                weightKg = 30.0,
                equipment = "Barra Livre",
                notes = "Mantenha a postura"
            ),
            WorkoutExercise(
                id = "ex_dia_3",
                order = 3,
                name = "Puxada Frente",
                muscleGroup = "Costas",
                sets = 3,
                reps = "12",
                weightKg = 35.0,
                equipment = "Polia",
                notes = "Aperte as escápulas"
            )
        )
        currentExercises.addAll(defaultExercises)
        refreshExercisesList()
    }

    private fun refreshExercisesList() {
        txtExercisesCount.text = "Exercícios (${currentExercises.size})"
        val uiList = currentExercises.mapIndexed { index, item ->
            Exercise(
                order = index + 1,
                name = item.name,
                muscleGroup = item.muscleGroup,
                sets = item.sets,
                reps = item.reps,
                weightKg = item.weightKg.toInt()
            )
        }
        recyclerExercises.adapter = ExerciseAdapter(uiList)
    }

    private fun observeStudents(preferredId: String?) {
        lifecycleScope.launch {
            repository.getAllStudents()
                .catch { }
                .collect { students ->
                    studentsList = students
                    if (students.isNotEmpty()) {
                        selectedStudent = if (!preferredId.isNullOrBlank()) {
                            students.firstOrNull { it.id == preferredId } ?: students.first()
                        } else if (selectedStudent == null) {
                            students.first()
                        } else {
                            students.firstOrNull { it.id == selectedStudent?.id } ?: students.first()
                        }
                        updateSelectedStudentUI()
                    }
                }
        }
    }

    private fun observeLibraryExercises() {
        lifecycleScope.launch {
            repository.getLibraryExercises()
                .catch { }
                .collect { exercises ->
                    if (exercises.isNotEmpty()) {
                        libraryExercises = exercises
                    }
                }
        }
    }

    private fun updateSelectedStudentUI() {
        val student = selectedStudent ?: return
        txtSelectedStudentName.text = student.name.ifBlank { student.email }
        txtSelectedStudentInfo.text = "${student.email}  •  Aluno ativo"
    }

    private fun showStudentSelectorDialog() {
        if (studentsList.isEmpty()) {
            Toast.makeText(this, "Nenhum aluno carregado do Firestore ainda.", Toast.LENGTH_SHORT).show()
            return
        }
        val names = studentsList.map { "${it.name} (${it.email})" }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Selecionar Aluno")
            .setItems(names) { _, which ->
                selectedStudent = studentsList[which]
                updateSelectedStudentUI()
            }
            .show()
    }

    private fun showAddExerciseDialog() {
        val names = libraryExercises.map {
            "${it.name} • ${it.muscleGroup} (${it.defaultSets}x ${it.defaultReps})"
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Adicionar Exercício do Catálogo")
            .setItems(names) { _, which ->
                val chosen = libraryExercises[which]
                val nextOrder = currentExercises.size + 1
                currentExercises.add(
                    WorkoutExercise(
                        order = nextOrder,
                        name = chosen.name,
                        muscleGroup = chosen.muscleGroup,
                        sets = chosen.defaultSets,
                        reps = chosen.defaultReps,
                        weightKg = chosen.defaultWeightKg,
                        equipment = chosen.equipment,
                        notes = chosen.instructions
                    )
                )
                refreshExercisesList()
                Toast.makeText(this, "${chosen.name} adicionado ao treino!", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Limpar lista") { _, _ ->
                currentExercises.clear()
                refreshExercisesList()
            }
            .setNegativeButton("Cancelar", null)
            .show()
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
            // Already here
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
