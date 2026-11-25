package com.example.telacadastro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.UserRole
import com.example.myapplication.data.repository.UnifitRepositoryImpl
import com.example.telatreinosamigosecadastro.TelaTreinosActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CadastroActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val repository = UnifitRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_aluno)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val edtName = findViewById<EditText>(R.id.edtName)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtUser = findViewById<EditText>(R.id.edtUser)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)
        val layoutErrorBanner = findViewById<View>(R.id.layoutErrorBanner)
        val txtLoginRedirect = findViewById<View>(R.id.txtLoginRedirect)

        txtLoginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

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
                return@setOnClickListener
            }

            if (password != confirmPassword || password.length < 6) {
                layoutErrorBanner.visibility = View.VISIBLE
                edtPassword.error = "A senha deve ter ao menos 6 caracteres e coincidir."
                edtConfirmPassword.error = "Verifique a confirmação da senha."
                return@setOnClickListener
            }

            // Se estiver tudo preenchido e válido, oculta os erros e prossegue
            layoutErrorBanner.visibility = View.GONE

            // Remove os erros visuais dos campos
            val normalBg = R.drawable.bg_input_rounded
            edtName.setBackgroundResource(normalBg)
            edtEmail.setBackgroundResource(normalBg)
            edtUser.setBackgroundResource(normalBg)
            edtPassword.setBackgroundResource(normalBg)
            edtConfirmPassword.setBackgroundResource(normalBg)

            btnRegister.isEnabled = false
            lifecycleScope.launch {
                try {
                    val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                    val uid = authResult.user?.uid ?: error("Falha ao obter UID do usuário")

                    val newUser = User(
                        id = uid,
                        name = name,
                        email = email,
                        role = UserRole.STUDENT.name
                    )
                    repository.saveUserProfile(newUser).getOrThrow()

                    Toast.makeText(this@CadastroActivity, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CadastroActivity, TelaTreinosActivity::class.java))
                    finishAffinity()
                } catch (e: Exception) {
                    btnRegister.isEnabled = true
                    Toast.makeText(
                        this@CadastroActivity,
                        "Erro ao cadastrar no Firebase: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}