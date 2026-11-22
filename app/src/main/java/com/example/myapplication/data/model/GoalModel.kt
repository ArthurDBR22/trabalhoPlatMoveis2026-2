package com.example.myapplication.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class StudentGoal(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val progressPercentage: Int = 0,    // 0 a 100
    val targetDate: Timestamp? = null,
    val isCompleted: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)
