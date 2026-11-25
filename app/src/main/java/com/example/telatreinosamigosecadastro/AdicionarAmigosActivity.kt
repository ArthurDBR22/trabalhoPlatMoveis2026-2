package com.example.telatreinosamigosecadastro

import com.example.myapplication.R
import com.example.myapplication.ExerciciosActivity
import com.example.myapplication.StudentHomeActivity
import com.example.myapplication.StudentProfileActivity
import com.example.telasocialepublicacao.SocialActivity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdicionarAmigosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_amigos)

        // 1. Cabeçalho: Ação de Voltar
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        // 2. Busca e Botão "Ver todas"
        val etBuscarUsuario = findViewById<EditText>(R.id.etBuscarUsuario)
        val tvVerTodas = findViewById<TextView>(R.id.tvVerTodas)

        tvVerTodas.setOnClickListener {
            Toast.makeText(this, "Navegando para lista completa de sugestões...", Toast.LENGTH_SHORT).show()
        }

        // 3. Cards de Ações Rápidas
        val cardEscanearQr = findViewById<LinearLayout>(R.id.cardEscanearQr)
        val cardConvidarAmigos = findViewById<LinearLayout>(R.id.cardConvidarAmigos)

        cardEscanearQr.setOnClickListener {
            Toast.makeText(this, "Abrindo Leitor de QR Code...", Toast.LENGTH_SHORT).show()
        }

        cardConvidarAmigos.setOnClickListener {
            Toast.makeText(this, "Gerando link de convite...", Toast.LENGTH_SHORT).show()
        }

        // 4. Botões de Adicionar das Sugestões
        val btnAddMariana = findViewById<Button>(R.id.btnAddMariana)
        val btnAddLucas = findViewById<Button>(R.id.btnAddLucas)

        btnAddMariana.setOnClickListener {
            btnAddMariana.text = "Enviado"
            btnAddMariana.isEnabled = false
            Toast.makeText(this, "Solicitação enviada para Mariana Costa!", Toast.LENGTH_SHORT).show()
        }

        btnAddLucas.setOnClickListener {
            btnAddLucas.text = "Enviado"
            btnAddLucas.isEnabled = false
            Toast.makeText(this, "Solicitação enviada para Lucas Ferreira!", Toast.LENGTH_SHORT).show()
        }

        // 5. Opções de Convite Inferiores
        val btnConviteWhatsapp = findViewById<LinearLayout>(R.id.btnConviteWhatsapp)
        val btnCopiarLink = findViewById<LinearLayout>(R.id.btnCopiarLink)
        val btnCompartilhar = findViewById<LinearLayout>(R.id.btnCompartilhar)
        val btnMeuQrCode = findViewById<LinearLayout>(R.id.btnMeuQrCode)

        btnConviteWhatsapp.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Venha treinar comigo no UniFit! Baixe o app: https://unifit.app/invite")
                type = "text/plain"
                setPackage("com.whatsapp")
            }
            try {
                startActivity(sendIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show()
            }
        }

        btnCopiarLink.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Convite UniFit", "https://unifit.app/invite")
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Link copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
        }

        btnCompartilhar.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Junte-se a mim no UniFit! https://unifit.app/invite")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar convite via"))
        }

        btnMeuQrCode.setOnClickListener {
            Toast.makeText(this, "Exibindo seu QR Code de perfil...", Toast.LENGTH_SHORT).show()
        }

        // 6. Barra de Navegação Inferior Fixa
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        val navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        val navSocial = findViewById<LinearLayout>(R.id.navSocial)
        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio?.setOnClickListener {
            startActivity(Intent(this, StudentHomeActivity::class.java))
            finish()
        }

        navTreinos?.setOnClickListener {
            startActivity(Intent(this, ExerciciosActivity::class.java))
            finish()
        }

        navSocial?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
        }

        navPerfil?.setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
            finish()
        }
    }
}
