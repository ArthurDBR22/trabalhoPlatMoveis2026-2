package com.example.myapplication.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class FeedPost(
    @DocumentId
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String? = null,
    val authorRole: String = UserRole.STUDENT.name,
    val textContent: String = "",
    val mediaUrl: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isModerated: Boolean = false,
    val status: String = "ACTIVE",      // "ACTIVE", "REPORTED", "HIDDEN"
    @ServerTimestamp
    val createdAt: Timestamp? = null
)

data class DailyNutritionHydration(
    @DocumentId
    val dateKey: String = "",           // "yyyy-MM-dd"
    val waterIntakeMl: Int = 0,
    val waterGoalMl: Int = 2500,
    val caloriesConsumedKcal: Int = 0,
    val caloriesGoalKcal: Int = 2200,
    @ServerTimestamp
    val lastUpdated: Timestamp? = null
)
