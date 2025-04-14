package com.example.tagarela.data.models

data class QuizItem(
    val id: Int,
    val imgUrl: String,
    val videoUrl: String,
    val isCorrect: Boolean
)

data class Question(
    val level: Int,
    val gameItems: List<QuizItem>,
    val correctAnswerIndex: Int
)
