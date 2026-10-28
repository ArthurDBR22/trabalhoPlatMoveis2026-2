package com.example.telasocialepublicacao

import com.example.myapplication.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FeedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerFeed = view.findViewById<RecyclerView>(R.id.recycler_feed)

        recyclerFeed.layoutManager = LinearLayoutManager(requireContext())

        var listaPublicacoes = listOf(
            Postagem("Lucas Ferreira", "Treino de hoje concluído! Foco, disciplina e resultado! 💪", "142", android.R.drawable.ic_menu_gallery),

            Postagem("Mariana Costa", "Dia de perna! 💪", "98", android.R.drawable.ic_menu_gallery),

            Postagem("Juliana Lima", "Treino concluído e me sentindo cada dia mais forte! 💙", "76", android.R.drawable.ic_menu_gallery),

            Postagem("João Silva", "Corridinha matinal para começar bem o dia!", "54", android.R.drawable.ic_menu_gallery)
        )

        recyclerFeed.adapter = PostAdapter(listaPublicacoes)

        val btnBatePapo = view.findViewById<Button>(R.id.btn_bloco_batepapo)

        val btnTreino = view.findViewById<Button>(R.id.btn_bloco_treino)

        val btnPostagens = view.findViewById<Button>(R.id.btn_bloco_postagens)


        btnBatePapo.setOnClickListener {
            Toast.makeText(requireContext(), "Tela de Bate-papo", Toast.LENGTH_SHORT).show()
        }


        btnTreino.setOnClickListener {
            Toast.makeText(requireContext(), "Tela de Treino", Toast.LENGTH_SHORT).show()
        }


        btnPostagens.setOnClickListener {
            Toast.makeText(requireContext(), "Tela de Minhas postagens", Toast.LENGTH_SHORT).show()
        }


        var fabCriarPost = view.findViewById<FloatingActionButton>(R.id.fab_criar_post)

        fabCriarPost.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .replace(
                    R.id.fragment_container,
                    CriarPublicacaoFragment()
                )
                .addToBackStack(null)
                .commit()
        }


        var navInicio = view.findViewById<Button>(R.id.nav_inicio)

        var navTreinos = view.findViewById<Button>(R.id.nav_treinos)

        var navSocial = view.findViewById<Button>(R.id.nav_social)

        var navPerfil = view.findViewById<Button>(R.id.nav_perfil)


        navInicio.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.example.myapplication.CustomWorkoutActivity::class.java))
        }


        navTreinos.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.example.telatreinosamigosecadastro.TelaTreinosActivity::class.java))
        }


        navSocial.setOnClickListener {
            Toast.makeText(requireContext(), "Você já está na tela Social", Toast.LENGTH_SHORT).show()
        }


        navPerfil.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.example.myapplication.HistoryActivity::class.java))
        }
    }
}