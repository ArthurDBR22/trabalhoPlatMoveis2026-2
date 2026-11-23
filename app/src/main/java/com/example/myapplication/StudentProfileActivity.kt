package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.telasocialepublicacao.SocialActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StudentProfileActivity : AppCompatActivity() {

    private var isEditingPersonalInfo = false
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_student"

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
            Toast.makeText(this, "Perfil salvo no Firebase com sucesso!", Toast.LENGTH_SHORT).show()
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

    private fun loadUserProfile(
        edtName: EditText,
        edtEmail: EditText,
        edtDob: EditText,
        edtGender: EditText,
        edtHeight: EditText,
        edtWeight: EditText
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doc = firestore.collection("users").document(userId).get().await()
                if (doc.exists()) {
                    val name = doc.getString("name") ?: "João Silva"
                    val email = doc.getString("email") ?: "joao.silva@email.com"
                    val dob = doc.getString("birthDate") ?: "12/05/2005"
                    val gender = doc.getString("gender") ?: "Masculino"
                    val height = doc.getString("height") ?: "1,78 m"
                    val weight = doc.getString("weight") ?: "72 kg"

                    withContext(Dispatchers.Main) {
                        edtName.setText(name)
                        edtEmail.setText(email)
                        edtDob.setText(dob)
                        edtGender.setText(gender)
                        edtHeight.setText(height)
                        edtWeight.setText(weight)
                    }
                }
            } catch (e: Exception) {
                // Fallback
            }
        }
    }

    private fun saveUserProfile(
        edtName: EditText,
        edtEmail: EditText,
        edtDob: EditText,
        edtGender: EditText,
        edtHeight: EditText,
        edtWeight: EditText
    ) {
        val profileData = mapOf(
            "name" to edtName.text.toString(),
            "email" to edtEmail.text.toString(),
            "birthDate" to edtDob.text.toString(),
            "gender" to edtGender.text.toString(),
            "height" to edtHeight.text.toString(),
            "weight" to edtWeight.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestore.collection("users").document(userId).set(profileData, com.google.firebase.firestore.SetOptions.merge()).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StudentProfileActivity, "Dados salvos no Firebase Firestore!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StudentProfileActivity, "Erro ao salvar no Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
