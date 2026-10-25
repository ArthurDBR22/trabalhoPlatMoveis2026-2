package com.example.telasocialepublicacao

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

class CriarPublicacaoFragment : Fragment() {
    private lateinit var edtPublicacao: EditText
    private var categoriaSelecionada = ""
    private val tipoUsuario = "Aluno"
    private val selecionarMidia =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            uris: List<Uri> ->

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

        val navInicio = view.findViewById<Button>(R.id.nav_inicio)

        val navTreinos = view.findViewById<Button>(R.id.nav_treinos)

        val navSocial = view.findViewById<Button>(R.id.nav_social)

        val navPerfil = view.findViewById<Button>(R.id.nav_perfil)

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

        navInicio.setOnClickListener {
            Toast.makeText(requireContext(), "Tela de Início", Toast.LENGTH_SHORT).show()
        }

        navTreinos.setOnClickListener {
            Toast.makeText(requireContext(), "Tela de Treinos", Toast.LENGTH_SHORT).show()
        }

        navSocial.setOnClickListener {
            Toast.makeText(requireContext(), "Você já está na área Social", Toast.LENGTH_SHORT).show()
        }

        navPerfil.setOnClickListener {
            Toast.makeText(requireContext(), "Tela de Perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun publicar() {

        var texto = edtPublicacao.text.toString().trim()

        if (texto.isEmpty()) {
            Toast.makeText(requireContext(), "Escreva algo antes de publicar.", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(requireActivity(), "Publicação feita!", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }
}