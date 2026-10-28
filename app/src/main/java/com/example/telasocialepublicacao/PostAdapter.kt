package com.example.telasocialepublicacao

import com.example.myapplication.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Postagem(
    var nomeUsuario: String,
    var legenda: String,
    var curtidas: String,
    var imagemPostRes: Int
)

class PostAdapter(private val listaPosts: List<Postagem>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtNome: TextView = itemView.findViewById<TextView>(R.id.txt_nome_usuario)
        var txtLegenda: TextView = itemView.findViewById<TextView>(R.id.txt_legenda)
        var txtCurtidas: TextView = itemView.findViewById<TextView>(R.id.txt_curtidas)
        var imgPostagem: ImageView = itemView.findViewById<ImageView>(R.id.img_postagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_postagem, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var post = listaPosts[position]
        holder.txtNome.text = post.nomeUsuario
        holder.txtLegenda.text = post.legenda
        holder.txtCurtidas.text = post.curtidas
        holder.imgPostagem.setImageResource(post.imagemPostRes)
    }

    override fun getItemCount(): Int = listaPosts.size
}