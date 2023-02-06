package com.example.jettrivia.model

data class QuestionItem(val difficulty: String = "",
                        val question: String = "",
                        val correct_answer: String = "",
                        var incorrect_answers: MutableList<String>,
                        val category: String = "",
                        val type: String = "")