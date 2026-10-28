package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.telasocialepublicacao.SocialActivity

class WorkoutInProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_in_progress)

        findViewById<android.view.View?>(R.id.main)?.let { v ->
            ViewCompat.setOnApplyWindowInsetsListener(v) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        findViewById<TextView>(R.id.tvVerTreino)?.setOnClickListener {
            startActivity(Intent(this, CustomWorkoutActivity::class.java))
        }

        findViewById<ImageView>(R.id.ivBack)?.setOnClickListener {
            finish()
        }

        findViewById<LinearLayout>(R.id.btnMinusTime)?.setOnClickListener {
            Toast.makeText(this, "Subtraiu 1 min", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnPlusTime)?.setOnClickListener {
            Toast.makeText(this, "Adicionou 1 min", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnStart)?.setOnClickListener {
            Toast.makeText(this, "Treino iniciado!", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnStop)?.setOnClickListener {
            Toast.makeText(this, "Treino parado!", Toast.LENGTH_SHORT).show()
            finish()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.let { nav ->
            nav.selectedItemId = R.id.nav_workout
            nav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this, CustomWorkoutActivity::class.java))
                        finish()
                        true
                    }
                    R.id.nav_workout -> true
                    R.id.nav_social -> {
                        startActivity(Intent(this, SocialActivity::class.java))
                        finish()
                        true
                    }
                    R.id.nav_profile -> {
                        startActivity(Intent(this, HistoryActivity::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
