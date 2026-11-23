package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Gravity
import android.widget.*
import android.graphics.Color
import android.content.Intent
import com.example.telasocialepublicacao.SocialActivity

// MODELO DOS EXERCÍCIOS
data class Exercicio(
    val nome: String,
    val grupo: String,
    val series: String,
    val repeticoes: String,
    val imagem: Int,
    val treino: String
)

class ExerciciosActivity : Activity() {

    private lateinit var containerExercicios: LinearLayout
    private lateinit var containerFiltros: LinearLayout
    private lateinit var spinnerTreino: Spinner

    private var filtroSelecionado = "Todos"
    private var treinoSelecionado = "Treino A"

    // LISTA DOS SEIS EXERCÍCIOS
    private val exercicios = listOf(

        Exercicio(
            "Supino reto",
            "Peito",
            "4 séries",
            "10 a 12 repetições",
            R.drawable.supino_reto,
            "Treino A"
        ),

        Exercicio(
            "Remada baixa",
            "Costas",
            "4 séries",
            "10 a 12 repetições",
            R.drawable.remada_baixa,
            "Treino A"
        ),

        Exercicio(
            "Leg press",
            "Pernas",
            "4 séries",
            "12 a 15 repetições",
            R.drawable.leg_press,
            "Treino B"
        ),

        Exercicio(
            "Desenvolvimento",
            "Ombros",
            "3 séries",
            "10 a 12 repetições",
            R.drawable.desenvolvimento,
            "Treino C"
        ),

        Exercicio(
            "Cadeira extensora",
            "Pernas",
            "3 séries",
            "12 a 15 repetições",
            R.drawable.cadeira_extensora,
            "Treino B"
        ),

        Exercicio(
            "Tríceps pulley",
            "Braços",
            "3 séries",
            "10 a 12 repetições",
            R.drawable.triceps_pulley,
            "Treino C"
        )
    )

    // CATEGORIAS
    private val categorias = listOf(
        "Todos",
        "Peito",
        "Costas",
        "Pernas",
        "Ombros",
        "Braços",
        "Core"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_treinos)

        containerExercicios =
            findViewById(R.id.containerExercicios)

        containerFiltros =
            findViewById(R.id.containerFiltros)

        spinnerTreino =
            findViewById(R.id.spinnerTreino)

        configurarSpinner()
        configurarFiltros()
        carregarExercicios()
        configurarBotoes()
    }

    // CONFIGURAR SELETOR DE TREINOS
    private fun configurarSpinner() {

        val treinos = listOf(
            "Treino A",
            "Treino B",
            "Treino C"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            treinos
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerTreino.adapter = adapter

        spinnerTreino.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    treinoSelecionado = parent?.getItemAtPosition(position).toString()
                    carregarExercicios()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    // CONFIGURAR FILTROS
    private fun configurarFiltros() {
        containerFiltros.removeAllViews()

        for (categoria in categorias) {
            val botao = Button(this).apply {
                text = categoria
                textSize = 12f
                isAllCaps = false
                setPadding(24, 0, 24, 0)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    36.dp()
                ).apply {
                    marginEnd = 8.dp()
                }

                if (categoria == filtroSelecionado) {
                    setBackgroundResource(R.drawable.fundo_filtro_ativo)
                    setTextColor(Color.WHITE)
                } else {
                    setBackgroundResource(R.drawable.fundo_filtro_inativo)
                    setTextColor(Color.rgb(82, 109, 171))
                }

                setOnClickListener {
                    filtroSelecionado = categoria
                    configurarFiltros()
                    carregarExercicios()
                }
            }
            containerFiltros.addView(botao)
        }
    }

    // CARREGAR EXERCÍCIOS
    private fun carregarExercicios() {
        containerExercicios.removeAllViews()

        val listaFiltrada = exercicios.filter {
            it.treino == treinoSelecionado &&
                    (filtroSelecionado == "Todos" || it.grupo.equals(filtroSelecionado, true))
        }

        if (listaFiltrada.isEmpty()) {
            val mensagem = TextView(this)
            mensagem.text = "Nenhum exercício encontrado nesta categoria."
            mensagem.textSize = 14f
            mensagem.setTextColor(Color.GRAY)
            mensagem.gravity = Gravity.CENTER
            mensagem.setPadding(10, 25, 10, 25)
            containerExercicios.addView(mensagem)
            return
        }

        for ((indice, exercicio) in listaFiltrada.withIndex()) {
            val item = LayoutInflater.from(this).inflate(
                R.layout.item_exercicio,
                containerExercicios,
                false
            )

            val numero = item.findViewById<TextView>(R.id.txtNumero)
            val imagem = item.findViewById<ImageView>(R.id.imgExercicio)
            val nome = item.findViewById<TextView>(R.id.txtNome)
            val grupo = item.findViewById<TextView>(R.id.txtGrupo)
            val series = item.findViewById<TextView>(R.id.txtSeries)
            val repeticoes = item.findViewById<TextView>(R.id.txtRepeticoes)
            val btnDetalhar = item.findViewById<TextView>(R.id.btnDetalhar)

            numero.text = (indice + 1).toString()
            nome.text = exercicio.nome
            grupo.text = exercicio.grupo
            series.text = exercicio.series
            repeticoes.text = exercicio.repeticoes
            imagem.setImageResource(exercicio.imagem)

            btnDetalhar.setOnClickListener {
                val intent = Intent(this, DetalheExercicioActivity::class.java).apply {
                    putExtra("numero", indice + 1)
                    putExtra("nome", exercicio.nome)
                    putExtra("grupo", exercicio.grupo)
                    putExtra("series", exercicio.series)
                    putExtra("repeticoes", exercicio.repeticoes)
                    putExtra("imagem", exercicio.imagem)
                    putExtra("treino", exercicio.treino)
                    putExtra("instrucao", "Realize o exercício com atenção à postura e à execução correta.")
                }
                startActivity(intent)
            }

            containerExercicios.addView(item)
        }
    }

    // CONFIGURAR BOTÕES
    private fun configurarBotoes() {
        findViewById<Button>(R.id.btnIniciarTreino)
            ?.setOnClickListener {
                Toast.makeText(this, "$treinoSelecionado iniciado!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutInProgressActivity::class.java))
            }

        findViewById<LinearLayout>(R.id.navInicio)
            ?.setOnClickListener {
                startActivity(Intent(this, StudentHomeActivity::class.java))
                finish()
            }

        findViewById<LinearLayout>(R.id.navTreinos)
            ?.setOnClickListener {
                // Already here
            }

        findViewById<LinearLayout>(R.id.navSocial)
            ?.setOnClickListener {
                startActivity(Intent(this, SocialActivity::class.java))
                finish()
            }

        findViewById<LinearLayout>(R.id.navPerfil)
            ?.setOnClickListener {
                startActivity(Intent(this, StudentProfileActivity::class.java))
                finish()
            }
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
