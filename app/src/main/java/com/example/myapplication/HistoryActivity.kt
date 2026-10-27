package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        val historyItems = listOf(
            WorkoutHistoryItem("Treino de Pernas", "Personalizado", 5, 420, "28/04/2025", "1h 20min"),
            WorkoutHistoryItem("Treino de Peito e Tríceps", "Personalizado", 6, 368, "26/04/2025", "1h 15min"),
            WorkoutHistoryItem("Treino de Costas", "Personalizado", 5, 320, "23/04/2025", "1h 10min"),
            WorkoutHistoryItem("Treino de Ombros", "Personalizado", 4, 280, "20/04/2025", "50min"),
            WorkoutHistoryItem("Treino de Glúteos", "Personalizado", 5, 340, "16/04/2025", "1h 05min")
        )

        rvHistory.adapter = WorkoutHistoryAdapter(historyItems)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_workout
    }
}