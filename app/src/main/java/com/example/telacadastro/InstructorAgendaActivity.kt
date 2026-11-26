package com.example.telacadastro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ConfiguracoesActivity
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InstructorAgendaActivity : AppCompatActivity() {

    private var currentWeekCalendar = Calendar.getInstance()
    private var selectedDayCalendar = Calendar.getInstance()

    private val dayContainers = mutableListOf<LinearLayout>()
    private val dayNames = mutableListOf<TextView>()
    private val dayNumbers = mutableListOf<TextView>()
    private val weekDates = mutableListOf<Calendar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_agenda)

        initWeekCalendarViews()
        updateWeekCalendar()

        findViewById<ImageView>(R.id.btnPrevWeek)?.setOnClickListener {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekCalendar()
        }

        findViewById<ImageView>(R.id.btnNextWeek)?.setOnClickListener {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            updateWeekCalendar()
        }

        setupBottomNavigation()
    }

    private fun initWeekCalendarViews() {
        dayContainers.clear()
        dayNames.clear()
        dayNumbers.clear()

        val containerIds = listOf(
            R.id.dayContainer1, R.id.dayContainer2, R.id.dayContainer3,
            R.id.dayContainer4, R.id.dayContainer5, R.id.dayContainer6, R.id.dayContainer7
        )

        val nameIds = listOf(
            R.id.txtDayName1, R.id.txtDayName2, R.id.txtDayName3,
            R.id.txtDayName4, R.id.txtDayName5, R.id.txtDayName6, R.id.txtDayName7
        )

        val numberIds = listOf(
            R.id.txtDayNumber1, R.id.txtDayNumber2, R.id.txtDayNumber3,
            R.id.txtDayNumber4, R.id.txtDayNumber5, R.id.txtDayNumber6, R.id.txtDayNumber7
        )

        for (i in 0..6) {
            val container = findViewById<LinearLayout>(containerIds[i])
            val name = findViewById<TextView>(nameIds[i])
            val number = findViewById<TextView>(numberIds[i])

            if (container != null && name != null && number != null) {
                dayContainers.add(container)
                dayNames.add(name)
                dayNumbers.add(number)

                val index = i
                container.setOnClickListener {
                    if (index < weekDates.size) {
                        selectedDayCalendar = weekDates[index].clone() as Calendar
                        renderSelectedDay()
                    }
                }
            }
        }
    }

    private fun updateWeekCalendar() {
        weekDates.clear()

        // Calcular a segunda-feira da semana de 'currentWeekCalendar'
        val cal = currentWeekCalendar.clone() as Calendar
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val dayNameFormat = SimpleDateFormat("EEE", Locale("pt", "BR"))
        val dayNumFormat = SimpleDateFormat("dd", Locale("pt", "BR"))

        val todayCal = Calendar.getInstance()

        for (i in 0..6) {
            val dayCal = cal.clone() as Calendar
            weekDates.add(dayCal)

            val nameStr = dayNameFormat.format(dayCal.time).replace(".", "").replaceFirstChar { it.uppercase() }
            val numStr = dayNumFormat.format(dayCal.time)

            if (i < dayNames.size) {
                dayNames[i].text = if (isSameDay(dayCal, todayCal)) "Hoje" else nameStr
                dayNumbers[i].text = numStr
            }

            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        renderSelectedDay()
    }

    private fun renderSelectedDay() {
        val dateHeaderFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val selectedDateStr = dateHeaderFormat.format(selectedDayCalendar.time)

        findViewById<TextView>(R.id.txtClassesHeaderTitle)?.text = "Aulas do dia ($selectedDateStr)"

        for (i in 0 until weekDates.size) {
            if (i >= dayContainers.size) break

            val dayCal = weekDates[i]
            val isSelected = isSameDay(dayCal, selectedDayCalendar)

            if (isSelected) {
                dayContainers[i].setBackgroundResource(R.drawable.bg_input_rounded)
                dayContainers[i].backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#007BFF"))
                dayNames[i].setTextColor(Color.WHITE)
                dayNumbers[i].setTextColor(Color.WHITE)
            } else {
                dayContainers[i].background = null
                dayNames[i].setTextColor(Color.parseColor("#6C757D"))
                dayNumbers[i].setTextColor(Color.parseColor("#0A192F"))
            }
        }
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.navInicio)?.setOnClickListener {
            startActivity(Intent(this, InstructorHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navAlunos)?.setOnClickListener {
            startActivity(Intent(this, StudentsListActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navTreinos)?.setOnClickListener {
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navAgenda)?.setOnClickListener {
            // Already here
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }
    }
}
