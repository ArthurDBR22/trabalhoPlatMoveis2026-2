package com.example.telasocialepublicacao

import com.example.myapplication.R
import com.example.myapplication.ExerciciosActivity
import com.example.myapplication.StudentHomeActivity
import com.example.myapplication.StudentProfileActivity
import com.example.myapplication.data.model.FeedPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.UUID

class CriarPublicacaoFragment : Fragment() {
    private lateinit var edtPublicacao: EditText
    private var categoriaSelecionada = ""
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
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

        val txtNomeUsuario = view.findViewById<TextView>(R.id.txt_nome_usuario)
        val txtUsernameUsuario = view.findViewById<TextView>(R.id.txt_username_usuario)
        val txtTipoUsuario = view.findViewById<TextView>(R.id.txt_tipo_usuario)

        // Carregar dados reais do perfil do usuário logado
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            firestore.collection("users").document(currentUid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val name = doc.getString("name") ?: "Aluno"
                        val handle = doc.getString("handle") ?: "usuario"
                        val role = doc.getString("role") ?: "STUDENT"

                        txtNomeUsuario.text = name
                        txtUsernameUsuario.text = if (handle.startsWith("@")) " $handle" else " @$handle"
                        txtTipoUsuario.text = if (role == "INSTRUCTOR" || role == "ADMIN") "Instrutor" else "Aluno"
                    }
                }
        }

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
                    startActivity(Intent(requireContext(), StudentHomeActivity::class.java))
                    true
                }
                R.id.nav_workout -> {
                    startActivity(Intent(requireContext(), ExerciciosActivity::class.java))
                    true
                }
                R.id.nav_social -> true
                R.id.nav_profile -> {
                    startActivity(Intent(requireContext(), StudentProfileActivity::class.java))
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
        
        val userId = auth.currentUser?.uid ?: "user_desconhecido"
        val postId = UUID.randomUUID().toString()
        
        // Buscando o nome do usuário para atrelar ao post
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                val authorName = doc.getString("name") ?: "Usuário Atual"
                
                val post = FeedPost(
                    id = postId,
                    authorId = userId,
                    authorName = authorName,
                    textContent = texto,
                    likesCount = 0,
                    commentsCount = 0,
                    mediaUrl = null // Ninguém publicou foto, então salvamos como nulo
                )
                
                firestore.collection("posts").document(postId).set(post)
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Publicação feita com sucesso!", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireActivity(), "Erro ao publicar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), "Erro ao identificar usuário.", Toast.LENGTH_SHORT).show()
            }
    }
}
