package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.model.UserRole
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.example.myapplication.data.seed.UnifitDatabaseSeeder
import com.example.telacadastro.InstructorHomeActivity
import com.example.telatreinosamigosecadastro.TelaTreinosActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private val auth = FirebaseAuth.getInstance()
    private val repository = UnifitRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPass = findViewById<EditText>(R.id.etPass)
        val llUserInput = findViewById<LinearLayout>(R.id.llUserInput)
        val llPassInput = findViewById<LinearLayout>(R.id.llPassInput)
        val llError = findViewById<LinearLayout>(R.id.llError)
        val ivTogglePassword = findViewById<ImageView>(R.id.ivTogglePassword)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnForgot = findViewById<Button>(R.id.btnForgot)

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPass.setSelection(etPass.text.length)
        }

        btnSubmit.setOnClickListener {
            val rawUser = etUser.text.toString().trim()
            val rawPass = etPass.text.toString().trim()

            llUserInput.setBackgroundResource(R.drawable.bg_edittext)
            llPassInput.setBackgroundResource(R.drawable.bg_edittext)
            llError.visibility = View.GONE

            if (rawUser.isBlank() || rawPass.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Atalhos convenientes para as contas seed ou login direto por e-mail
            val isAdminShortcut = rawUser.equals("admin", ignoreCase = true) ||
                rawUser.equals("professor", ignoreCase = true)
            val isStudentShortcut = rawUser.equals("aluno", ignoreCase = true)

            val email = when {
                isAdminShortcut -> UnifitDatabaseSeeder.ADMIN_EMAIL
                isStudentShortcut -> "joao@unifit.com"
                else -> rawUser
            }
            val password = when {
                isAdminShortcut && (rawPass == "admin" || rawPass == "professor") ->
                    UnifitDatabaseSeeder.DEFAULT_PASSWORD
                isStudentShortcut && rawPass == "aluno" ->
                    UnifitDatabaseSeeder.DEFAULT_PASSWORD
                else -> rawPass
            }

            performFirebaseLogin(
                email = email,
                password = password,
                isDemoShortcut = isAdminShortcut || isStudentShortcut,
                fallbackAsInstructor = isAdminShortcut,
                llUserInput = llUserInput,
                llPassInput = llPassInput,
                llError = llError
            )
        }

        btnForgot.setOnClickListener {
            startActivity(Intent(this, PasswordRecoveryActivity::class.java))
        }
    }

    private fun performFirebaseLogin(
        email: String,
        password: String,
        isDemoShortcut: Boolean,
        fallbackAsInstructor: Boolean,
        llUserInput: LinearLayout,
        llPassInput: LinearLayout,
        llError: LinearLayout
    ) {
        lifecycleScope.launch {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid
                if (uid == null) {
                    showInputError(llUserInput, llPassInput, llError)
                    return@launch
                }

                val profile = repository.getUserProfile(uid)
                val role = profile?.role ?: if (fallbackAsInstructor) UserRole.ADMIN.name else UserRole.STUDENT.name

                navigateByRole(role, profile?.name)
            } catch (e: Exception) {
                if (isDemoShortcut || email in UnifitDatabaseSeeder.DEFAULT_ACCOUNTS.map { it.email }) {
                    offerSeedOrOfflineDialog(email, password, fallbackAsInstructor, e)
                } else {
                    showInputError(llUserInput, llPassInput, llError)
                }
            }
        }
    }

    private fun offerSeedOrOfflineDialog(
        email: String,
        password: String,
        fallbackAsInstructor: Boolean,
        error: Exception
    ) {
        AlertDialog.Builder(this)
            .setTitle("Configurar Contas no Firebase")
            .setMessage(
                "Não foi possível autenticar ($email): ${error.localizedMessage}\n\n" +
                    "Se você acabou de ativar o Firebase Authentication (E-mail/Senha) e as regras do Firestore, " +
                    "toque em \"Criar Contas (Seed)\" para criar automaticamente as 5 contas, 3 exercícios e o Treino do Dia."
            )
            .setPositiveButton("Criar Contas (Seed)") { _, _ ->
                lifecycleScope.launch {
                    Toast.makeText(this@LoginActivity, "Criando contas no Firebase...", Toast.LENGTH_SHORT).show()
                    val seedResult = UnifitDatabaseSeeder(applicationContext).seedAll()
                    seedResult.onSuccess {
                        try {
                            val result = auth.signInWithEmailAndPassword(email, password).await()
                            val uid = result.user?.uid
                            val profile = uid?.let { repository.getUserProfile(it) }
                            val role = profile?.role ?: if (fallbackAsInstructor) UserRole.ADMIN.name else UserRole.STUDENT.name
                            navigateByRole(role, profile?.name)
                        } catch (loginErr: Exception) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Contas criadas! Erro ao entrar: ${loginErr.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.onFailure { seedErr ->
                        Toast.makeText(
                            this@LoginActivity,
                            "Erro no Seed Firebase: ${seedErr.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNeutralButton("Entrar Offline") { _, _ ->
                val role = if (fallbackAsInstructor) UserRole.ADMIN.name else UserRole.STUDENT.name
                navigateByRole(role, null)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun navigateByRole(role: String, name: String?) {
        val isInstructorOrAdmin = role == UserRole.ADMIN.name || role == UserRole.INSTRUCTOR.name
        if (isInstructorOrAdmin) {
            Toast.makeText(this, "Bem-vindo(a), ${name ?: "Professor"}!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, InstructorHomeActivity::class.java))
        } else {
            Toast.makeText(this, "Bem-vindo(a), ${name ?: "Aluno"}!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, TelaTreinosActivity::class.java))
        }
        finishAffinity()
    }

    private fun showInputError(
        llUserInput: LinearLayout,
        llPassInput: LinearLayout,
        llError: LinearLayout
    ) {
        llUserInput.setBackgroundResource(R.drawable.bg_edittext_error)
        llPassInput.setBackgroundResource(R.drawable.bg_edittext_error)
        llError.visibility = View.VISIBLE
    }
}
