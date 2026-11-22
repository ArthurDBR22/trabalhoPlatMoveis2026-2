package com.example.myapplication.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class PhysicalAssessment(
    @DocumentId
    val id: String = "",
    val weightKg: Double = 0.0,
    val bodyFatPercentage: Double = 0.0,
    val muscleMassKg: Double = 0.0,
    val waistCircumferenceCm: Double = 0.0,
    val notes: String = "",
    val evaluatedByInstructorId: String? = null,
    @ServerTimestamp
    val date: Timestamp? = null
)

data class BodyMeasurement(
    @DocumentId
    val id: String = "",
    val bustCm: Double = 0.0,
    val waistCm: Double = 0.0,
    val hipCm: Double = 0.0,
    val thighCm: Double = 0.0,
    val armCm: Double = 0.0,
    val calfCm: Double = 0.0,
    @ServerTimestamp
    val date: Timestamp? = null
)
