package com.example.telacadastro

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                // Exibe o banner de aviso no topo
                layoutErrorBanner.visibility = View.VISIBLE

                // Altera o fundo dos campos para o vermelho de erro
                val errorBg = R.drawable.bg_input_error
                edtName.setBackgroundResource(errorBg)
                edtEmail.setBackgroundResource(errorBg)
                edtUser.setBackgroundResource(errorBg)
                edtPassword.setBackgroundResource(errorBg)
                edtConfirmPassword.setBackgroundResource(errorBg)

                // Adiciona a mensagem de erro nativa em cada campo
                edtName.error = "Este campo é obrigatório."
                edtEmail.error = "Este campo é obrigatório."
                edtUser.error = "Este campo é obrigatório."
                edtPassword.error = "Este campo é obrigatório."
                edtConfirmPassword.error = "Este campo é obrigatório."
            } else {
                // Se estiver tudo preenchido, oculta os erros e prossegue
                layoutErrorBanner.visibility = View.GONE

                // Remove os erros visuais dos campos se já estiverem preenchidos
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