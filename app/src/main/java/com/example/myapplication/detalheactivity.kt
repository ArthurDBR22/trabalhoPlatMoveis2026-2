package com.example.unifit

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalheExercicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        // DADOS DO EXERCÍCIO
        val nome = intent.getStringExtra("nome") ?: "Exercício"

        val grupo = intent.getStringExtra("grupo") ?: "Peitoral"

        val series = intent.getStringExtra("series") ?: "4 séries"

        val repeticoes = intent.getStringExtra("repeticoes")
            ?: "10 a 12 repetições"

        val imagem = intent.getIntExtra(
            "imagem",
            R.drawable.treino_dia
        )

        val descricao = intent.getStringExtra("descricao")
            ?: "Exercício que ajuda a fortalecer os músculos."

        val objetivo = intent.getStringExtra("objetivo")
            ?: "Fortalecer os músculos e melhorar o condicionamento."

        val nivel = intent.getStringExtra("nivel")
            ?: "Intermediário"

        val equipamento = intent.getStringExtra("equipamento")
            ?: "Barra livre"

        val descanso = intent.getStringExtra("descanso") ?: "60s"

        val duracao = intent.getStringExtra("duracao") ?: "10min"

        val instrucao = intent.getStringExtra("instrucao")
            ?: """
                Posicione-se corretamente.
                Mantenha a postura adequada.
                Execute o movimento com controle.
                Retorne à posição inicial.
            """.trimIndent()

        val dicas = intent.getStringExtra("dicas")
            ?: """
                • Mantenha a postura correta.

                • Faça os movimentos com controle.

                • Respeite os intervalos de descanso.

                • Utilize uma carga adequada.
            """.trimIndent()

        val musculos = intent.getStringExtra("musculos")
            ?: grupo

        val auxiliares = intent.getStringExtra("auxiliares")
            ?: "Músculos auxiliares"

        // NOME E DESCRIÇÃO
        findViewById<TextView>(R.id.txtDetalheNome).text = nome

        findViewById<TextView>(R.id.txtDescricaoExercicio).text =
            descricao

        // FOTO DO EXERCÍCIO
        findViewById<ImageView>(R.id.imgDetalheExercicio)
            .setImageResource(imagem)

        // ETIQUETAS DO CABEÇALHO
        configurarEtiquetas(grupo, auxiliares)

        // EQUIPAMENTO
        findViewById<TextView>(R.id.txtEquipamento).text =
            "Equipamento: $equipamento"

        // OBJETIVO E NÍVEL
        findViewById<TextView>(R.id.txtObjetivo).text = objetivo

        findViewById<TextView>(R.id.txtNivel).text = nivel

        // SÉRIES
        findViewById<TextView>(R.id.txtDetalheSeries).text =
            series.replace(
                Regex("(?i)\\s*séries?"), ""
            ).trim()

        // REPETIÇÕES
        findViewById<TextView>(R.id.txtDetalheRepeticoes).text =
            repeticoes.replace(
                Regex("(?i)\\s*repetições?"), ""
            ).trim()

        // DESCANSO E DURAÇÃO
        findViewById<TextView>(R.id.txtDescanso).text = descanso

        findViewById<TextView>(R.id.txtDuracao).text = duracao

        // INSTRUÇÕES NUMERADAS
        val containerInstrucoes =
            findViewById<LinearLayout>(R.id.txtDetalheInstrucao)

        montarInstrucoes(containerInstrucoes, instrucao)

        // DICAS
        findViewById<TextView>(R.id.txtDicas).text = dicas

        // MÚSCULOS TRABALHADOS
        findViewById<TextView>(R.id.txtMusculosTrabalhados).text =
            musculos

        // BOTÃO VOLTAR
        findViewById<TextView>(R.id.btnVoltar).setOnClickListener {
            finish()
        }
    }

    // CONFIGURA AS ETIQUETAS DO CABEÇALHO
    private fun configurarEtiquetas(
        grupo: String,
        auxiliares: String
    ) {
        val grupos = grupo
            .split(",", "•", ";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val ids = listOf(
            R.id.txtMusculoTag1,
            R.id.txtMusculoTag2,
            R.id.txtMusculoTag3
        )

        ids.forEachIndexed { index, id ->
            val etiqueta = findViewById<TextView>(id)

            if (index < grupos.size) {
                etiqueta.text = grupos[index]
                etiqueta.visibility = View.VISIBLE
            } else {
                etiqueta.visibility = View.GONE
            }
        }

        val tagAuxiliar =
            findViewById<TextView>(R.id.txtMusculoTag4)

        if (auxiliares.isNotBlank()) {
            tagAuxiliar.text = auxiliares
            tagAuxiliar.visibility = View.VISIBLE
        } else {
            tagAuxiliar.visibility = View.GONE
        }
    }

    // CRIA AS INSTRUÇÕES COM CÍRCULOS AZUIS
    private fun montarInstrucoes(
        container: LinearLayout,
        texto: String
    ) {
        container.removeAllViews()

        val etapas = texto
            .split(Regex("\\n+"))
            .map {
                it.trim().replace(
                    Regex("^\\d+[.)]\\s*"), ""
                )
            }
            .filter { it.isNotEmpty() }

        etapas.forEachIndexed { index, etapa ->

            val linha = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL

                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = dp(13)
                }
            }

            // CÍRCULO AZUL
            val numero = TextView(this).apply {
                text = (index + 1).toString()
                textSize = 14f
                setTextColor(Color.WHITE)
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER

                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(Color.rgb(8, 124, 245))
                }

                layoutParams = LinearLayout.LayoutParams(
                    dp(32),
                    dp(32)
                )
            }

            // TEXTO DA INSTRUÇÃO
            val descricaoEtapa = TextView(this).apply {
                text = etapa
                textSize = 13f
                setTextColor(Color.rgb(82, 109, 171))
                setLineSpacing(dp(3).toFloat(), 1f)

                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    marginStart = dp(12)
                }
            }

            linha.addView(numero)
            linha.addView(descricaoEtapa)

            container.addView(linha)
        }
    }

    // CONVERTE DP
    private fun dp(valor: Int): Int {
        return (
                valor * resources.displayMetrics.density
                ).toInt()
    }
}