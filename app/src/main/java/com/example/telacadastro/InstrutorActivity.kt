package com.example.telacadastro

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class InstrutorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrutor) // Aponta para o novo layout

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val edtName = findViewById<EditText>(R.id.edtName)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtUser = findViewById<EditText>(R.id.edtUser)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)
        val layoutErrorBanner = findViewById<View>(R.id.layoutErrorBanner)

        btnRegister.setOnClickListener {
            val name = edtName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val user = edtUser.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                layoutErrorBanner.visibility = View.VISIBLE

                val errorBg = R.drawable.bg_input_error
                edtName.setBackgroundResource(errorBg)
                edtEmail.setBackgroundResource(errorBg)
                edtUser.setBackgroundResource(errorBg)
                edtPassword.setBackgroundResource(errorBg)
                edtConfirmPassword.setBackgroundResource(errorBg)

                edtName.error = "Este campo é obrigatório."
                edtEmail.error = "Este campo é obrigatório."
                edtUser.error = "Este campo é obrigatório."
                edtPassword.error = "Este campo é obrigatório."
                edtConfirmPassword.error = "Este campo é obrigatório."
            } else {
                layoutErrorBanner.visibility = View.GONE

                val normalBg = R.drawable.bg_input_rounded
                edtName.setBackgroundResource(normalBg)
                edtEmail.setBackgroundResource(normalBg)
                edtUser.setBackgroundResource(normalBg)
                edtPassword.setBackgroundResource(normalBg)
                edtConfirmPassword.setBackgroundResource(normalBg)
            }
        }
    }
}