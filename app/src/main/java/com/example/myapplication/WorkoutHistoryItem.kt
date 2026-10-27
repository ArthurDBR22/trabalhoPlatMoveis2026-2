package com.example.myapplication

data class WorkoutHistoryItem(
    val title: String,
    val type: String,
    val exercisesCount: Int,
    val calories: Int,
    val date: String,
    val duration: String
)