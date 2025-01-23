package com.example.tagarela.data.models

data class CardResponse(
    val items: List<Card>
)

data class Card(
    val audio: String,
    val category: String,
    val id: String,
    val img: String,
    val name: String,
    val subcategory: String,
    val syllables: String,
    val video: String
)
