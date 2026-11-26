package com.example.telacadastro

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.UserRole
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StudentsListActivity : AppCompatActivity() {

    private val repository = UnifitRepositoryImpl()
    private lateinit var studentAdapter: StudentAdapter
    private var fullStudentList: List<User> = emptyList()

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

        studentAdapter = StudentAdapter(emptyList()) { student ->
            val intent = Intent(this, CreateWorkoutActivity::class.java).apply {
                putExtra("STUDENT_ID", student.id)
                putExtra("STUDENT_NAME", student.name)
            }
            startActivity(intent)
        }
        recyclerStudents.adapter = studentAdapter

        // Search Filter
        findViewById<EditText>(R.id.edtSearchStudent)?.addTextChangedListener { text ->
            val query = text?.toString()?.trim().orEmpty()
            val filtered = if (query.isBlank()) {
                fullStudentList
            } else {
                fullStudentList.filter {
                    it.name.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
                }
            }
            studentAdapter.updateList(filtered)
        }

        observeStudents()
        setupBottomNavigation()
    }

    private fun observeStudents() {
        lifecycleScope.launch {
            repository.getAllStudents()
                .catch {
                    val list = fallbackStudents()
                    fullStudentList = list
                    studentAdapter.updateList(list)
                    updateStatsCards(list)
                }
                .collect { students ->
                    val list = if (students.isEmpty()) fallbackStudents() else students
                    fullStudentList = list
                    studentAdapter.updateList(list)
                    updateStatsCards(list)
                }
        }
    }

    private fun updateStatsCards(students: List<User>) {
        val txtStatAtivos = findViewById<TextView>(R.id.txtStatAtivos)
        val txtStatEvoluindo = findViewById<TextView>(R.id.txtStatEvoluindo)
        val txtStatNovos = findViewById<TextView>(R.id.txtStatNovos)
        val txtStatInativos = findViewById<TextView>(R.id.txtStatInativos)

        val threeDaysMs = 3L * 24 * 60 * 60 * 1000
        val now = System.currentTimeMillis()

        var ativosCount = 0
        var evoluindoCount = 0
        var inativosCount = 0
        var novosCount = 0

        students.forEach { student ->
            val lastAccess = student.updatedAt?.toDate()?.time ?: student.createdAt?.toDate()?.time ?: 0L
            val isAtivo = (now - lastAccess) <= threeDaysMs || lastAccess == 0L

            if (isAtivo) {
                ativosCount++
                evoluindoCount++ // Regra: se estiver ativo, está evoluindo
            } else {
                inativosCount++
            }

            // Regra: cadastrado nos últimos 30 dias é "Novo"
            val createdAtTime = student.createdAt?.toDate()?.time ?: 0L
            val thirtyDaysMs = 30L * 24 * 60 * 60 * 1000
            if (createdAtTime > 0 && (now - createdAtTime) <= thirtyDaysMs) {
                novosCount++
            }
        }

        txtStatAtivos?.text = ativosCount.toString()
        txtStatEvoluindo?.text = evoluindoCount.toString()
        txtStatNovos?.text = novosCount.toString()
        txtStatInativos?.text = inativosCount.toString()
    }

    private fun fallbackStudents(): List<User> = listOf(
        User(id = "demo_1", name = "Mariana Costa", email = "mariana@unifit.com", role = UserRole.STUDENT.name, currentWeightKg = 61.5),
        User(id = "demo_2", name = "Lucas Ferreira", email = "lucas@unifit.com", role = UserRole.STUDENT.name, currentWeightKg = 78.0),
        User(id = "demo_3", name = "Juliana Lima", email = "juliana@unifit.com", role = UserRole.STUDENT.name, currentWeightKg = 64.0),
        User(id = "demo_4", name = "João Silva", email = "joao@unifit.com", role = UserRole.STUDENT.name, currentWeightKg = 74.5)
    )

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
