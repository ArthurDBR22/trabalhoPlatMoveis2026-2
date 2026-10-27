package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WorkoutInProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_in_progress)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        findViewById<LinearLayout>(R.id.btnMinusTime).setOnClickListener {
            Toast.makeText(this, "Subtraiu 1 min", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnPlusTime).setOnClickListener {
            Toast.makeText(this, "Adicionou 1 min", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnStart).setOnClickListener {
            Toast.makeText(this, "Treino inciado!", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnStop).setOnClickListener {
            Toast.makeText(this, "Treino parado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}