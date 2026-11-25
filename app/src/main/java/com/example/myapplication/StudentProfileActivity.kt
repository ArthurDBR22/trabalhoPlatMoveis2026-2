package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.example.telasocialepublicacao.SocialActivity
import com.example.telatreinosamigosecadastro.TelaTreinosActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class StudentProfileActivity : AppCompatActivity() {

    private var isEditingPersonalInfo = false
    private val auth = FirebaseAuth.getInstance()
    private val repository = UnifitRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        val edtName = findViewById<EditText>(R.id.edtProfileName)
        val edtEmail = findViewById<EditText>(R.id.edtProfileEmail)
        val edtDob = findViewById<EditText>(R.id.edtProfileDob)
        val edtGender = findViewById<EditText>(R.id.edtProfileGender)
        val edtHeight = findViewById<EditText>(R.id.edtProfileHeight)
        val edtWeight = findViewById<EditText>(R.id.edtProfileWeight)
        val btnEditPersonalInfo = findViewById<TextView>(R.id.btnEditPersonalInfo)

        loadUserProfile(edtName, edtEmail, edtDob, edtGender, edtHeight, edtWeight)

        btnEditPersonalInfo?.setOnClickListener {
            isEditingPersonalInfo = !isEditingPersonalInfo
            val fields = listOf(edtName, edtEmail, edtDob, edtGender, edtHeight, edtWeight)

            if (isEditingPersonalInfo) {
                btnEditPersonalInfo.text = "Salvar"
                fields.forEach { field ->
                    field?.isEnabled = true
                    field?.isFocusable = true
                    field?.isFocusableInTouchMode = true
                }
                edtName?.requestFocus()
                Toast.makeText(this, "Modo de edição ativado", Toast.LENGTH_SHORT).show()
            } else {
                btnEditPersonalInfo.text = "Editar"
                fields.forEach { field ->
                    field?.isEnabled = false
                    field?.isFocusable = false
                    field?.isFocusableInTouchMode = false
                }
                Toast.makeText(this, "Informações pessoais atualizadas com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.btnEditPreferences)?.setOnClickListener {
            Toast.makeText(this, "Modo de edição de preferências ativado", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnSaveProfile)?.setOnClickListener {
            Toast.makeText(this, "Todas as alterações do perfil foram salvas!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
            confirmLogout()
        }

        // Bottom Navigation
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, TelaTreinosActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            startActivity(Intent(this, TelaTreinosActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.navSocial)?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            // Already here
        }
    }

    private fun loadUserProfile(
        edtName: EditText?,
        edtEmail: EditText?,
        edtDob: EditText?,
        edtGender: EditText?,
        edtHeight: EditText?,
        edtWeight: EditText?
    ) {
        val uid = auth.currentUser?.uid ?: return
        lifecycleScope.launch {
            val user = repository.getUserProfile(uid) ?: return@launch
            if (user.name.isNotBlank()) edtName?.setText(user.name)
            if (user.email.isNotBlank()) edtEmail?.setText(user.email)
            if (user.birthDate.isNotBlank()) edtDob?.setText(user.birthDate)
            if (user.gender.isNotBlank()) edtGender?.setText(user.gender)
            if (user.heightCm > 0) edtHeight?.setText("${user.heightCm / 100.0} m")
            if (user.currentWeightKg > 0) edtWeight?.setText("${user.currentWeightKg} kg")
        }
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Sair da conta")
            .setMessage("Deseja realmente sair da sua conta?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sair") { _, _ ->
                auth.signOut()
                Toast.makeText(this, "Você saiu da conta.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finishAffinity()
            }
            .show()
    }
}
