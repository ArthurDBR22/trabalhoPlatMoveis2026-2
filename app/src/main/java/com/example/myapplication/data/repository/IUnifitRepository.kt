package com.example.myapplication.data.repository

import com.example.myapplication.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IUnifitRepository {
    // Autenticação & Papéis
    suspend fun getUserProfile(userId: String): User?
    suspend fun isUserInstructor(userId: String): Boolean

    // Treinos & Exercícios
    fun getWorkoutPlan(studentId: String, workoutId: String): Flow<WorkoutPlan?>
    fun getWorkoutExercises(studentId: String, workoutId: String): Flow<List<WorkoutExercise>>
    suspend fun saveWorkoutWithExercises(
        studentId: String,
        workout: WorkoutPlan,
        exercises: List<WorkoutExercise>
    ): Result<Unit>

    // Avaliações & Medidas Corporais
    suspend fun saveAssessment(studentId: String, assessment: PhysicalAssessment): Result<String>
    suspend fun addBodyMeasurement(studentId: String, measurement: BodyMeasurement): Result<String>
    fun getBodyMeasurementsHistory(
        studentId: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<BodyMeasurement>>

    // Histórico de Treinos
    fun getWorkoutHistory(
        studentId: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<WorkoutSession>>
    suspend fun recordCompletedSession(studentId: String, session: WorkoutSession): Result<String>

    // Metas
    fun getStudentGoals(studentId: String): Flow<List<StudentGoal>>
    suspend fun updateGoalProgress(studentId: String, goalId: String, newProgress: Int): Result<Unit>
}
