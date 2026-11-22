package com.example.myapplication.data.repository

import com.example.myapplication.data.model.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class UnifitRepositoryImpl(
    private val firestore: FirebaseFirestore
) : IUnifitRepository {

    // ----------------------------------------------------
    // PERFIL E PAPÉIS
    // ----------------------------------------------------
    override suspend fun getUserProfile(userId: String): User? {
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun isUserInstructor(userId: String): Boolean {
        val user = getUserProfile(userId) ?: return false
        return user.role == UserRole.INSTRUCTOR.name || user.role == UserRole.ADMIN.name
    }

    // ----------------------------------------------------
    // TREINOS E EXERCÍCIOS ORDENADOS
    // ----------------------------------------------------
    override fun getWorkoutPlan(studentId: String, workoutId: String): Flow<WorkoutPlan?> = callbackFlow {
        val docRef = firestore.collection("users")
            .document(studentId)
            .collection("workouts")
            .document(workoutId)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObject(WorkoutPlan::class.java))
        }
        awaitClose { listener.remove() }
    }

    override fun getWorkoutExercises(studentId: String, workoutId: String): Flow<List<WorkoutExercise>> = callbackFlow {
        val query = firestore.collection("users")
            .document(studentId)
            .collection("workouts")
            .document(workoutId)
            .collection("exercises")
            .orderBy("order", Query.Direction.ASCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val exercises = snapshot?.toObjects<WorkoutExercise>() ?: emptyList()
            trySend(exercises)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun saveWorkoutWithExercises(
        studentId: String,
        workout: WorkoutPlan,
        exercises: List<WorkoutExercise>
    ): Result<Unit> = runCatching {
        val batch = firestore.batch()
        val userWorkoutsRef = firestore.collection("users")
            .document(studentId)
            .collection("workouts")

        val workoutDocRef = if (workout.id.isBlank()) {
            userWorkoutsRef.document()
        } else {
            userWorkoutsRef.document(workout.id)
        }

        val workoutToSave = workout.copy(exercisesCount = exercises.size)
        batch.set(workoutDocRef, workoutToSave)

        exercises.forEachIndexed { index, exercise ->
            val exerciseRef = if (exercise.id.isBlank()) {
                workoutDocRef.collection("exercises").document()
            } else {
                workoutDocRef.collection("exercises").document(exercise.id)
            }
            batch.set(exerciseRef, exercise.copy(order = index + 1))
        }

        batch.commit().await()
    }

    // ----------------------------------------------------
    // AVALIAÇÕES E MEDIDAS CORPORAIS COM INTERVALOS DE TEMPO
    // ----------------------------------------------------
    override suspend fun saveAssessment(studentId: String, assessment: PhysicalAssessment): Result<String> = runCatching {
        val docRef = firestore.collection("users")
            .document(studentId)
            .collection("assessments")
            .document()
        docRef.set(assessment).await()
        docRef.id
    }

    override suspend fun addBodyMeasurement(studentId: String, measurement: BodyMeasurement): Result<String> = runCatching {
        val docRef = firestore.collection("users")
            .document(studentId)
            .collection("body_measurements")
            .document()
        docRef.set(measurement).await()
        docRef.id
    }

    override fun getBodyMeasurementsHistory(
        studentId: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<BodyMeasurement>> = callbackFlow {
        val query = firestore.collection("users")
            .document(studentId)
            .collection("body_measurements")
            .whereGreaterThanOrEqualTo("date", Timestamp(startDate))
            .whereLessThanOrEqualTo("date", Timestamp(endDate))
            .orderBy("date", Query.Direction.ASCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.toObjects<BodyMeasurement>() ?: emptyList()
            trySend(list)
        }
        awaitClose { listener.remove() }
    }

    // ----------------------------------------------------
    // HISTÓRICO DE TREINOS FILTRADO POR PERÍODO
    // ----------------------------------------------------
    override fun getWorkoutHistory(
        studentId: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<WorkoutSession>> = callbackFlow {
        val query = firestore.collection("users")
            .document(studentId)
            .collection("workout_sessions")
            .whereGreaterThanOrEqualTo("startedAt", Timestamp(startDate))
            .whereLessThanOrEqualTo("startedAt", Timestamp(endDate))
            .orderBy("startedAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val list = snapshot?.toObjects<WorkoutSession>() ?: emptyList()
            trySend(list)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun recordCompletedSession(
        studentId: String,
        session: WorkoutSession
    ): Result<String> = runCatching {
        val docRef = firestore.collection("users")
            .document(studentId)
            .collection("workout_sessions")
            .document()
        docRef.set(session).await()
        docRef.id
    }

    // ----------------------------------------------------
    // METAS DO ALUNO
    // ----------------------------------------------------
    override fun getStudentGoals(studentId: String): Flow<List<StudentGoal>> = callbackFlow {
        val query = firestore.collection("users")
            .document(studentId)
            .collection("goals")
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val goals = snapshot?.toObjects<StudentGoal>() ?: emptyList()
            trySend(goals)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateGoalProgress(
        studentId: String,
        goalId: String,
        newProgress: Int
    ): Result<Unit> = runCatching {
        require(newProgress in 0..100) { "O progresso deve estar entre 0 e 100%" }
        firestore.collection("users")
            .document(studentId)
            .collection("goals")
            .document(goalId)
            .update(
                mapOf(
                    "progressPercentage" to newProgress,
                    "isCompleted" to (newProgress >= 100),
                    "updatedAt" to Timestamp.now()
                )
            ).await()
    }
}
