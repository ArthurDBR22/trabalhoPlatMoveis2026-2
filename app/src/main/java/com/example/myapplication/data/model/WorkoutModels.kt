package com.example.myapplication.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

data class WorkoutPlan(
    @DocumentId
    val id: String = "",
    val title: String = "",             // Ex: "Treino A - Pernas & Glúteos"
    val goal: String = "",              // Ex: "Hipertrofia", "Resistência"
    val level: String = "Intermediário",
    val description: String = "",
    val frequencyWeekly: List<String> = emptyList(), // Ex: ["Seg", "Qua", "Sex"]
    val averageDurationMinutes: Int = 60,
    val cycleWeeks: Int = 8,
    val exercisesCount: Int = 0,
    val createdByInstructorId: String? = null,
    @get:PropertyName("isActive")
    @set:PropertyName("isActive")
    var isActive: Boolean = true,
    @get:PropertyName("isWorkoutOfDay")
    @set:PropertyName("isWorkoutOfDay")
    var isWorkoutOfDay: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)

data class WorkoutExercise(
    @DocumentId
    val id: String = "",
    val order: Int = 1,
    val name: String = "",
    val muscleGroup: String = "",       // Ex: "Quadríceps", "Peitoral"
    val sets: Int = 3,
    val reps: String = "10-12",
    val weightKg: Double = 0.0,
    val equipment: String = "",         // Ex: "Barra Livre", "Cadeira Extensora"
    val imageUrl: String? = null,
    val restTimeSeconds: Int = 60,
    val notes: String? = null
)

data class LibraryExercise(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val muscleGroup: String = "",
    val equipment: String = "",
    val defaultSets: Int = 3,
    val defaultReps: String = "10-12",
    val defaultWeightKg: Double = 0.0,
    val level: String = "Intermediário",
    val instructions: String = "",
    val imageUrl: String? = null,
    val createdByInstructorId: String? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null
)

enum class SessionStatus {
    IN_PROGRESS,
    COMPLETED,
    ABANDONED
}

data class ExerciseLog(
    val exerciseName: String = "",
    val muscleGroup: String = "",
    val setNumber: Int = 1,
    val repsPerformed: Int = 0,
    val weightUsedKg: Double = 0.0,
    val completed: Boolean = true
)

data class WorkoutSession(
    @DocumentId
    val id: String = "",
    val workoutPlanId: String? = null,
    val title: String = "",             // Ex: "Treino A"
    val type: String = "Personalizado", // Ex: "Personalizado", "Funcional"
    val status: String = SessionStatus.COMPLETED.name,
    val exercisesCompletedCount: Int = 0,
    val caloriesKcal: Int = 0,
    val durationSeconds: Long = 0L,
    val executedExercises: List<ExerciseLog> = emptyList(),
    @ServerTimestamp
    val startedAt: Timestamp? = null,
    val finishedAt: Timestamp? = null
)
