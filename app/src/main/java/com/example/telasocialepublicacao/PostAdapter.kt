package com.example.telasocialepublicacao

import com.example.myapplication.R
import com.example.myapplication.data.model.FeedPost
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private val listaPosts: List<FeedPost>,
    private val currentUserId: String,
    private val onLikeClicked: (FeedPost) -> Unit
) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtNome: TextView = itemView.findViewById(R.id.txt_nome_usuario)
        var txtLegenda: TextView = itemView.findViewById(R.id.txt_legenda)
        var txtCurtidas: TextView = itemView.findViewById(R.id.txt_curtidas)
        var imgPostagem: ImageView = itemView.findViewById(R.id.img_postagem)
        var btnCurtir: ImageView = itemView.findViewById(R.id.btn_curtir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_postagem, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = listaPosts[position]
        holder.txtNome.text = post.authorName
        holder.txtLegenda.text = post.textContent
        holder.txtCurtidas.text = post.likesCount.toString()
        
        // Verifica se o usuário atual curtiu
        val isLiked = post.likedBy.contains(currentUserId)
        if (isLiked) {
            holder.btnCurtir.setImageResource(R.drawable.ic_heart_filled)
        } else {
            holder.btnCurtir.setImageResource(R.drawable.ic_heart_outline)
        }
        
        holder.btnCurtir.setOnClickListener {
            onLikeClicked(post)
        }
        
        // Se a postagem não tem imagem, oculta o ImageView completamente
        if (post.mediaUrl.isNullOrEmpty()) {
            holder.imgPostagem.visibility = View.GONE
        } else {
            holder.imgPostagem.visibility = View.VISIBLE
            // Aqui carregaria a imagem (Glide/Picasso). Como não tem, deixamos um placeholder
            holder.imgPostagem.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun getItemCount(): Int = listaPosts.size
}
