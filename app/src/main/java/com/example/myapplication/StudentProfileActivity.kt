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
import com.example.telasocialepublicacao.SocialActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentProfileActivity : AppCompatActivity() {

    private var isEditingPersonalInfo = false
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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
                saveUserProfile(edtName, edtEmail, edtDob, edtGender, edtHeight, edtWeight)
            }
        }

        findViewById<TextView>(R.id.btnEditPreferences)?.setOnClickListener {
            Toast.makeText(this, "Modo de edição de preferências ativado", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnSaveProfile)?.setOnClickListener {
            saveUserProfile(edtName, edtEmail, edtDob, edtGender, edtHeight, edtWeight)
        }

        findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
            confirmLogout()
        }

        // Bottom Navigation
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, StudentHomeActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            startActivity(Intent(this, ExerciciosActivity::class.java))
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

    private fun currentUserId(): String? = auth.currentUser?.uid

    private fun loadUserProfile(
        edtName: EditText?,
        edtEmail: EditText?,
        edtDob: EditText?,
        edtGender: EditText?,
        edtHeight: EditText?,
        edtWeight: EditText?
    ) {
        val uid = currentUserId() ?: return
        lifecycleScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                if (doc.exists()) {
                    doc.getString("name")?.takeIf { it.isNotBlank() }?.let { edtName?.setText(it) }
                    doc.getString("email")?.takeIf { it.isNotBlank() }?.let { edtEmail?.setText(it) }
                    doc.getString("birthDate")?.takeIf { it.isNotBlank() }?.let { edtDob?.setText(it) }
                    doc.getString("gender")?.takeIf { it.isNotBlank() }?.let { edtGender?.setText(it) }
                    doc.getString("height")?.takeIf { it.isNotBlank() }?.let { edtHeight?.setText(it) }
                    doc.getString("weight")?.takeIf { it.isNotBlank() }?.let { edtWeight?.setText(it) }
                }
            } catch (_: Exception) {
                Toast.makeText(
                    this@StudentProfileActivity,
                    "Não foi possível carregar os dados do perfil.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveUserProfile(
        edtName: EditText?,
        edtEmail: EditText?,
        edtDob: EditText?,
        edtGender: EditText?,
        edtHeight: EditText?,
        edtWeight: EditText?
    ) {
        val uid = currentUserId()
        if (uid == null) {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show()
            return
        }

        val profileData = mapOf(
            "name" to (edtName?.text?.toString()?.trim().orEmpty()),
            "email" to (edtEmail?.text?.toString()?.trim().orEmpty()),
            "birthDate" to (edtDob?.text?.toString()?.trim().orEmpty()),
            "gender" to (edtGender?.text?.toString()?.trim().orEmpty()),
            "height" to (edtHeight?.text?.toString()?.trim().orEmpty()),
            "weight" to (edtWeight?.text?.toString()?.trim().orEmpty())
        )

        lifecycleScope.launch {
            try {
                firestore.collection("users")
                    .document(uid)
                    .set(profileData, SetOptions.merge())
                    .await()
                Toast.makeText(
                    this@StudentProfileActivity,
                    "Dados salvos no Firebase Firestore!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@StudentProfileActivity,
                    "Erro ao salvar no Firebase: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
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