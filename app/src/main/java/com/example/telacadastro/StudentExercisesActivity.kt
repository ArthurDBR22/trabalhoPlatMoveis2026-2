package com.example.telacadastro

import com.example.myapplication.R
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentExercisesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_exercises)

        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, InstructorHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            // Already here
        }
        findViewById<LinearLayout>(R.id.navSocial)?.setOnClickListener {
            startActivity(Intent(this, com.example.telasocialepublicacao.SocialActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
        }
    }
}
