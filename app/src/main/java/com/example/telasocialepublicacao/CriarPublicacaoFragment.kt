package com.example.telasocialepublicacao

import com.example.myapplication.R

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CriarPublicacaoFragment : Fragment() {
    private lateinit var edtPublicacao: EditText
    private var categoriaSelecionada = ""
    private val tipoUsuario = "Aluno"
    private val selecionarMidia =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            if (uris.isEmpty()) {
                return@registerForActivityResult
            }
            if (uris.size > 5) {
                Toast.makeText(requireContext(), "Você pode selecionar no máximo 5 arquivos.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "${uris.size} arquivo(s) selecionado(s)", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_criar_publicacao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtPublicacao = view.findViewById(R.id.edt_publicacao)
        val btnVoltar = view.findViewById<ImageButton>(R.id.btn_voltar)
        val areaMidia = view.findViewById<LinearLayout>(R.id.area_adicionar_midia)
        val btnCategoriaTreino = view.findViewById<Button>(R.id.btn_categoria_treino)
        val btnCategoriaMotivacao = view.findViewById<Button>(R.id.btn_categoria_motivacao)
        val btnPublicar = view.findViewById<Button>(R.id.btn_publicar)

        btnVoltar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        areaMidia.setOnClickListener {
            selecionarMidia.launch("*/*")
        }

        btnCategoriaTreino.setOnClickListener {
            categoriaSelecionada = "Treino"
            Toast.makeText(requireContext(), "Categoria selecionada: Treino", Toast.LENGTH_SHORT).show()
        }

        btnCategoriaMotivacao.setOnClickListener {
            categoriaSelecionada = "Motivação"
            Toast.makeText(requireContext(), "Categoria selecionada: Motivação", Toast.LENGTH_SHORT).show()
        }

        btnPublicar.setOnClickListener {
            publicar()
        }

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_social

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(requireContext(), com.example.myapplication.CustomWorkoutActivity::class.java))
                    true
                }
                R.id.nav_workout -> {
                    startActivity(Intent(requireContext(), com.example.telatreinosamigosecadastro.TelaTreinosActivity::class.java))
                    true
                }
                R.id.nav_social -> {
                    startActivity(Intent(requireContext(), SocialActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(requireContext(), com.example.myapplication.HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun publicar() {
        val texto = edtPublicacao.text.toString().trim()
        if (texto.isEmpty()) {
            Toast.makeText(requireContext(), "Escreva algo antes de publicar.", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(requireActivity(), "Publicação feita!", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }
}
