package com.example.myapplication

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

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false

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
        }

        btnSubmit.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()


            llUserInput.setBackgroundResource(R.drawable.bg_edittext)
            llPassInput.setBackgroundResource(R.drawable.bg_edittext)
            llError.visibility = View.GONE

            if (user.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (user == "admin" || user == "professor") {
                if (pass == "admin" || pass == "professor") {
                    Toast.makeText(this, "Login de Professor realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, com.example.telacadastro.InstructorHomeActivity::class.java))
                    finishAffinity()
                } else {
                    llUserInput.setBackgroundResource(R.drawable.bg_edittext_error)
                    llPassInput.setBackgroundResource(R.drawable.bg_edittext_error)
                    llError.visibility = View.VISIBLE
                }
            } else if (user == "aluno" || user.isNotBlank()) {
                if (pass == "aluno" || pass.isNotBlank()) {
                    Toast.makeText(this, "Login de Aluno realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, com.example.telasocialepublicacao.SocialActivity::class.java))
                    finishAffinity()
                } else {
                    llUserInput.setBackgroundResource(R.drawable.bg_edittext_error)
                    llPassInput.setBackgroundResource(R.drawable.bg_edittext_error)
                    llError.visibility = View.VISIBLE
                }
            } else {
                llUserInput.setBackgroundResource(R.drawable.bg_edittext_error)
                llPassInput.setBackgroundResource(R.drawable.bg_edittext_error)
                llError.visibility = View.VISIBLE
            }
        }

        btnForgot.setOnClickListener {
            startActivity(Intent(this, PasswordRecoveryActivity::class.java))
        }
    }
}