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
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IUnifitRepository {

    // ----------------------------------------------------
    // PERFIL, ALUNOS E PAPÉIS
    // ----------------------------------------------------
    override suspend fun getUserProfile(userId: String): User? {
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            snapshot.toObject(User::class.java)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun saveUserProfile(user: User): Result<Unit> = runCatching {
        require(user.id.isNotBlank()) { "ID do usuário não pode ser vazio" }
        firestore.collection("users").document(user.id).set(user).await()
    }

    override suspend fun isUserInstructor(userId: String): Boolean {
        val user = getUserProfile(userId) ?: return false
        return user.role == UserRole.INSTRUCTOR.name || user.role == UserRole.ADMIN.name
    }

    override fun getAllStudents(): Flow<List<User>> = callbackFlow {
        val query = firestore.collection("users")
            .whereEqualTo("role", UserRole.STUDENT.name)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val students = snapshot?.toObjects<User>()?.sortedBy { it.name } ?: emptyList()
            trySend(students)
        }
        awaitClose { listener.remove() }
    }

    // ----------------------------------------------------
    // BIBLIOTECA DE EXERCÍCIOS
    // ----------------------------------------------------
    override suspend fun addLibraryExercises(exercises: List<LibraryExercise>): Result<Unit> = runCatching {
        val batch = firestore.batch()
        val libraryRef = firestore.collection("exercise_library")

        exercises.forEach { exercise ->
            val docRef = if (exercise.id.isBlank()) {
                libraryRef.document()
            } else {
                libraryRef.document(exercise.id)
            }
            batch.set(docRef, exercise)
        }
        batch.commit().await()
    }

    override fun getLibraryExercises(): Flow<List<LibraryExercise>> = callbackFlow {
        val query = firestore.collection("exercise_library")
            .orderBy("name", Query.Direction.ASCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObjects<LibraryExercise>() ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    // ----------------------------------------------------
    // TREINOS, TREINO DO DIA E EXERCÍCIOS ORDENADOS
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

    override fun getWorkoutOfTheDay(studentId: String): Flow<Pair<WorkoutPlan, List<WorkoutExercise>>?> = callbackFlow {
        val workoutsRef = firestore.collection("users")
            .document(studentId)
            .collection("workouts")
            .whereEqualTo("isWorkoutOfDay", true)
            .limit(1)

        var exercisesListener: com.google.firebase.firestore.ListenerRegistration? = null

        val workoutListener = workoutsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val plan = snapshot?.toObjects<WorkoutPlan>()?.firstOrNull()
            exercisesListener?.remove()

            if (plan == null) {
                trySend(null)
            } else {
                exercisesListener = firestore.collection("users")
                    .document(studentId)
                    .collection("workouts")
                    .document(plan.id)
                    .collection("exercises")
                    .orderBy("order", Query.Direction.ASCENDING)
                    .addSnapshotListener { exSnap, exErr ->
                        if (exErr != null) {
                            close(exErr)
                            return@addSnapshotListener
                        }
                        val exercises = exSnap?.toObjects<WorkoutExercise>() ?: emptyList()
                        trySend(plan to exercises)
                    }
            }
        }
        awaitClose {
            exercisesListener?.remove()
            workoutListener.remove()
        }
    }

    override suspend fun saveWorkoutWithExercises(
        studentId: String,
        workout: WorkoutPlan,
        exercises: List<WorkoutExercise>
    ): Result<String> = runCatching {
        val userWorkoutsRef = firestore.collection("users")
            .document(studentId)
            .collection("workouts")

        // Se for o novo Treino do Dia, desmarca outros marcados anteriormente
        if (workout.isWorkoutOfDay) {
            val previousDayWorkouts = userWorkoutsRef
                .whereEqualTo("isWorkoutOfDay", true)
                .get()
                .await()
            val resetBatch = firestore.batch()
            previousDayWorkouts.documents.forEach { doc ->
                if (doc.id != workout.id) {
                    resetBatch.update(doc.reference, "isWorkoutOfDay", false)
                }
            }
            resetBatch.commit().await()
        }

        val batch = firestore.batch()
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
        workoutDocRef.id
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
