package com.example.myapplication.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

enum class UserRole {
    STUDENT,
    INSTRUCTOR,
    ADMIN
}

data class SessionPreferences(
    val defaultRestTimeSec: Int = 60,
    val quickRestTimeSec: Int = 30,
    val audioAlertsEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)

data class User(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = UserRole.STUDENT.name,
    val photoUrl: String? = null,
    val phone: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val heightCm: Double = 0.0,
    val currentWeightKg: Double = 0.0,
    val instructorId: String? = null,
    val preferences: SessionPreferences = SessionPreferences(),
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)
