package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.telatreinosamigosecadastro.TelaTreinosActivity
import com.example.telasocialepublicacao.SocialActivity

class StudentProfileActivity : AppCompatActivity() {

    private var isEditingPersonalInfo = false

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
}
