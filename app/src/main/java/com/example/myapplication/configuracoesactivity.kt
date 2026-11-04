package com.example.unifit

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import android.graphics.Color

class ConfiguracoesActivity : Activity() {

    private lateinit var switchLembretes: Switch
    private lateinit var switchNotificacoes: Switch
    private lateinit var switchSons: Switch
    private lateinit var switchVibracao: Switch
    private lateinit var grupoIdioma: RadioGroup

    private val preferencias by lazy {
        getSharedPreferences("configuracoes_unifit", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        inicializarComponentes()
        carregarPreferencias()
        configurarSwitches()
        configurarIdioma()
        configurarBotoes()
    }

    // LOCALIZAR COMPONENTES
    private fun inicializarComponentes() {

        switchLembretes = findViewById(R.id.switchLembretes)
        switchNotificacoes = findViewById(R.id.switchNotificacoes)
        switchSons = findViewById(R.id.switchSons)
        switchVibracao = findViewById(R.id.switchVibracao)

        grupoIdioma = findViewById(R.id.grupoIdioma)
    }

    // CARREGAR CONFIGURAÇÕES SALVAS
    private fun carregarPreferencias() {

        switchLembretes.isChecked =
            preferencias.getBoolean("lembretes", true)

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

        switchLembretes.setOnCheckedChangeListener { _, marcado ->
            preferencias.edit()
                .putBoolean("lembretes", marcado)
                .apply()
        }

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

    // CONFIGURAR IDIOMA
    private fun configurarIdioma() {

        grupoIdioma.setOnCheckedChangeListener { _, checkedId ->

            val idioma = when (checkedId) {
                R.id.radioIngles -> "ingles"
                R.id.radioEspanhol -> "espanhol"
                else -> "portugues"
            }

            preferencias.edit()
                .putString("idioma", idioma)
                .apply()

            atualizarAparenciaIdioma()

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
            .setOnClickListener {
                finish()
            }

        findViewById<LinearLayout>(R.id.btnPrivacidade)
            .setOnClickListener {
                mostrarPrivacidade()
            }

        findViewById<LinearLayout>(R.id.btnSobre)
            .setOnClickListener {
                mostrarSobre()
            }

        findViewById<LinearLayout>(R.id.btnSair)
            .setOnClickListener {
                confirmarSaida()
            }
    }

    // PRIVACIDADE
    private fun mostrarPrivacidade() {

        AlertDialog.Builder(this)
            .setTitle("Privacidade e segurança")
            .setMessage(
                "Suas configurações e preferências do UniFit " +
                        "são armazenadas localmente neste dispositivo."
            )
            .setPositiveButton("Entendi", null)
            .show()
    }

    // SOBRE O APLICATIVO
    private fun mostrarSobre() {

        AlertDialog.Builder(this)
            .setTitle("Sobre o UniFit")
            .setMessage(
                "UniFit\n\n" +
                        "Mais saúde, mais movimento.\n\n" +
                        "Aplicativo desenvolvido para auxiliar " +
                        "na organização dos treinos e atividades físicas."
            )
            .setPositiveButton("Fechar", null)
            .show()
    }

    // CONFIRMAR SAÍDA
    private fun confirmarSaida() {

        AlertDialog.Builder(this)
            .setTitle("Sair da conta")
            .setMessage("Deseja realmente sair do aplicativo?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sair") { _, _ ->

                Toast.makeText(
                    this,
                    "Configure aqui a saída da sua conta.",
                    Toast.LENGTH_LONG
                ).show()
            }
            .show()
    }
}