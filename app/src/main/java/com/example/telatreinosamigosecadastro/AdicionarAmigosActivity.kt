package com.example.telatreinosamigosecadastro

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

        // 1. Cabeçalho: Ação de Voltar[cite: 2]
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish() // Retorna à tela anterior
        }

        // 2. Busca e Botão "Ver todas"[cite: 2]
        val etBuscarUsuario = findViewById<EditText>(R.id.etBuscarUsuario)
        val tvVerTodas = findViewById<TextView>(R.id.tvVerTodas)

        tvVerTodas.setOnClickListener {
            Toast.makeText(this, "Navegando para lista completa de sugestões...", Toast.LENGTH_SHORT).show()
        }

        // 3. Cards de Ações Rápidas[cite: 2]
        val cardEscanearQr = findViewById<LinearLayout>(R.id.cardEscanearQr)
        val cardConvidarAmigos = findViewById<LinearLayout>(R.id.cardConvidarAmigos)

        cardEscanearQr.setOnClickListener {
            Toast.makeText(this, "Abrindo Leitor de QR Code...", Toast.LENGTH_SHORT).show()
        }

        cardConvidarAmigos.setOnClickListener {
            Toast.makeText(this, "Gerando link de convite...", Toast.LENGTH_SHORT).show()
        }

        // 4. Botões de Adicionar das Sugestões[cite: 2]
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

        // 5. Opções de Convite Inferiores[cite: 2]
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

        // 6. Barra de Navegação Inferior Fixa (4 Telas)[cite: 2]
        val navInicio = findViewById<LinearLayout>(R.id.navInicio)
        val navTreinos = findViewById<LinearLayout>(R.id.navTreinos)
        val navSocial = findViewById<LinearLayout>(R.id.navSocial)
        val navPerfil = findViewById<LinearLayout>(R.id.navPerfil)

        navInicio.setOnClickListener {
            Toast.makeText(this, "Navegando para Início", Toast.LENGTH_SHORT).show()
        }

        navTreinos.setOnClickListener {
            startActivity(Intent(this, TelaTreinosActivity::class.java))
            finish()
        }

        navSocial.setOnClickListener {
            // Tela atual
        }

        navPerfil.setOnClickListener {
            Toast.makeText(this, "Navegando para Perfil", Toast.LENGTH_SHORT).show()
        }
    }
}