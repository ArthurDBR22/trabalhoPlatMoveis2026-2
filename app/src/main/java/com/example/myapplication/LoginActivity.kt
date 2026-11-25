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
import com.example.telacadastro.InstructorHomeActivity
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
                isAdminShortcut -> "admin@unifit.com"
                isStudentShortcut -> "joao@unifit.com"
                else -> rawUser
            }
            val password = when {
                isAdminShortcut && (rawPass == "admin" || rawPass == "professor") ->
                    "Unifit2025!"
                isStudentShortcut && rawPass == "aluno" ->
                    "Unifit2025!"
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
                showInputError(llUserInput, llPassInput, llError)
            }
        }
    }

    private fun navigateByRole(role: String, name: String?) {
        val isInstructorOrAdmin = role == UserRole.ADMIN.name || role == UserRole.INSTRUCTOR.name
        if (isInstructorOrAdmin) {
            Toast.makeText(this, "Bem-vindo(a), ${name ?: "Professor"}!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, InstructorHomeActivity::class.java))
        } else {
            Toast.makeText(this, "Bem-vindo(a), ${name ?: "Aluno"}!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, StudentHomeActivity::class.java))
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
