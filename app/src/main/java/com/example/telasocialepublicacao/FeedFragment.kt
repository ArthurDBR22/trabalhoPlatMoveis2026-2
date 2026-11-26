package com.example.telasocialepublicacao

import com.example.myapplication.R
import com.example.myapplication.ExerciciosActivity
import com.example.myapplication.StudentHomeActivity
import com.example.myapplication.StudentProfileActivity
import com.example.myapplication.data.model.FeedPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FeedFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUserId = auth.currentUser?.uid ?: "default"
    
    private val listaPublicacoes = mutableListOf<FeedPost>()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerFeed = view.findViewById<RecyclerView>(R.id.recycler_feed)
        recyclerFeed.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(listaPublicacoes, currentUserId) { postClicked ->
            curtirPublicacao(postClicked)
        }
        recyclerFeed.adapter = adapter

        // Escuta atualizações do Firestore em tempo real (Realtime Listener)
        ouvirFeedEmTempoReal()

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
    
    private fun ouvirFeedEmTempoReal() {
        firestore.collection("posts")
            .whereEqualTo("status", "ACTIVE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Erro ao atualizar feed.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Ordenamos as publicações localmente no celular (do mais recente ao mais antigo)
                    val posts = snapshot.toObjects(FeedPost::class.java).sortedByDescending { it.createdAt }
                    listaPublicacoes.clear()
                    listaPublicacoes.addAll(posts)
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun curtirPublicacao(post: FeedPost) {
        if (post.id.isNotBlank()) {
            val isLiked = post.likedBy.contains(currentUserId)
            val docRef = firestore.collection("posts").document(post.id)
            
            if (isLiked) {
                // Remove like
                docRef.update(
                    "likesCount", FieldValue.increment(-1),
                    "likedBy", FieldValue.arrayRemove(currentUserId)
                ).addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao remover curtida.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Add like
                docRef.update(
                    "likesCount", FieldValue.increment(1),
                    "likedBy", FieldValue.arrayUnion(currentUserId)
                ).addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao curtir.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
