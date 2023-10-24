package edu.tcu.falarcon.quiz

data class Question(
    val question: String,
    val image: Int,
    val options: List<String>,
    val correctAnswer: String
)