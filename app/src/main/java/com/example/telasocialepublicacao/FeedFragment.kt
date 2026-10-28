package com.example.telasocialepublicacao

import com.example.myapplication.R

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FeedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerFeed = view.findViewById<RecyclerView>(R.id.recycler_feed)
        recyclerFeed.layoutManager = LinearLayoutManager(requireContext())

        val listaPublicacoes = listOf(
            Postagem("Lucas Ferreira", "Treino de hoje concluído! Foco, disciplina e resultado! 💪", "142", android.R.drawable.ic_menu_gallery),
            Postagem("Mariana Costa", "Dia de perna! 💪", "98", android.R.drawable.ic_menu_gallery),
            Postagem("Juliana Lima", "Treino concluído e me sentindo cada dia mais forte! 💙", "76", android.R.drawable.ic_menu_gallery),
            Postagem("João Silva", "Corridinha matinal para começar bem o dia!", "54", android.R.drawable.ic_menu_gallery)
        )

        recyclerFeed.adapter = PostAdapter(listaPublicacoes)
        val fabCriarPost = view.findViewById<FloatingActionButton>(R.id.fab_criar_post)
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
                R.id.nav_profile -> {
                    startActivity(Intent(requireContext(), com.example.myapplication.HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
