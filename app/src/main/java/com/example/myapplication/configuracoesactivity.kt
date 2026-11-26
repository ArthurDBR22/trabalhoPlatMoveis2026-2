package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var switchNotificacoes: Switch
    private lateinit var switchSons: Switch
    private lateinit var switchVibracao: Switch
    private lateinit var grupoIdioma: RadioGroup

    private val preferencias by lazy {
        getSharedPreferences("configuracoes_unifit", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.applySavedLocale(this)
        setContentView(R.layout.activity_configuracoes)

        inicializarComponentes()
        carregarPreferencias()
        configurarSwitches()
        configurarIdioma()
        configurarBotoes()
    }

    // LOCALIZAR COMPONENTES
    private fun inicializarComponentes() {
        switchNotificacoes = findViewById(R.id.switchNotificacoes)
        switchSons = findViewById(R.id.switchSons)
        switchVibracao = findViewById(R.id.switchVibracao)

        grupoIdioma = findViewById(R.id.grupoIdioma)
    }

    // CARREGAR CONFIGURAÇÕES SALVAS
    private fun carregarPreferencias() {
        switchNotificacoes.isChecked =
            preferencias.getBoolean("notificacoes", true)

        switchSons.isChecked =
            preferencias.getBoolean("sons", true)

        switchVibracao.isChecked =
            preferencias.getBoolean("vibracao", true)

        val idioma = preferencias.getString(
            "idioma",
            "portugues"
        )

        when (idioma) {
            "ingles" -> grupoIdioma.check(R.id.radioIngles)
            "espanhol" -> grupoIdioma.check(R.id.radioEspanhol)
            else -> grupoIdioma.check(R.id.radioPortugues)
        }

        atualizarAparenciaIdioma()
    }

    // SALVAR ALTERAÇÕES DOS SWITCHES
    private fun configurarSwitches() {
        switchNotificacoes.setOnCheckedChangeListener { _, marcado ->
            preferencias.edit()
                .putBoolean("notificacoes", marcado)
                .apply()
        }

        switchSons.setOnCheckedChangeListener { _, marcado ->
            preferencias.edit()
                .putBoolean("sons", marcado)
                .apply()
        }

        switchVibracao.setOnCheckedChangeListener { _, marcado ->
            preferencias.edit()
                .putBoolean("vibracao", marcado)
                .apply()
        }
    }

    // CONFIGURAR IDIOMA DINÂMICO
    private fun configurarIdioma() {
        grupoIdioma.setOnCheckedChangeListener { _, checkedId ->

            val idioma = when (checkedId) {
                R.id.radioIngles -> "ingles"
                R.id.radioEspanhol -> "espanhol"
                else -> "portugues"
            }

            val idiomaAtual = preferencias.getString("idioma", "portugues")
            if (idioma != idiomaAtual) {
                LocaleHelper.setLocale(this, idioma)

                val mensagem = when (idioma) {
                    "ingles" -> "English selecionado."
                    "espanhol" -> "Español selecionado."
                    else -> "Português selecionado."
                }

                Toast.makeText(
                    this,
                    mensagem,
                    Toast.LENGTH_SHORT
                ).show()

                recreate()
            }
        }
    }

    // ATUALIZAR CORES DOS IDIOMAS
    private fun atualizarAparenciaIdioma() {
        val botoes = listOf(
            findViewById<RadioButton>(R.id.radioPortugues),
            findViewById<RadioButton>(R.id.radioIngles),
            findViewById<RadioButton>(R.id.radioEspanhol)
        )

        for (botao in botoes) {
            if (botao.isChecked) {
                botao.setBackgroundResource(
                    R.drawable.bg_config_option_selected
                )
                botao.setTextColor(Color.WHITE)
            } else {
                botao.setBackgroundResource(
                    R.drawable.bg_config_option
                )
                botao.setTextColor(
                    Color.rgb(82, 109, 171)
                )
            }
        }
    }

    // CONFIGURAR BOTÕES
    private fun configurarBotoes() {
        findViewById<TextView>(R.id.btnVoltarConfiguracoes)
            ?.setOnClickListener {
                finish()
            }

        findViewById<LinearLayout>(R.id.btnSair)
            ?.setOnClickListener {
                confirmarSaida()
            }
    }

    // CONFIRMAR SAÍDA
    private fun confirmarSaida() {
        AlertDialog.Builder(this)
            .setTitle("Sair da conta")
            .setMessage("Deseja realmente sair da sua conta?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sair") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(
                    this,
                    "Você saiu da conta.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finishAffinity()
            }
            .show()
    }
}
