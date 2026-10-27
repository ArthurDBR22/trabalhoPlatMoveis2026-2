package com.example.myapplication

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResetPasswordActivity : AppCompatActivity() {

    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etCode = findViewById<EditText>(R.id.etCode)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val ivToggleNewPassword = findViewById<ImageView>(R.id.ivToggleNewPassword)
        val ivToggleConfirmPassword = findViewById<ImageView>(R.id.ivToggleConfirmPassword)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<Button>(R.id.btnBack)

        ivToggleNewPassword.setOnClickListener {
            isNewPasswordVisible = !isNewPasswordVisible
            if (isNewPasswordVisible) {
                etNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etNewPassword.setSelection(etNewPassword.text.length)
        }

        ivToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }

        btnSubmit.setOnClickListener {
            val code = etCode.text.toString()
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (code.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            Toast.makeText(this, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}