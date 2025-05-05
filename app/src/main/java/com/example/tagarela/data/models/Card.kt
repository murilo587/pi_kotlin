package com.example.tagarela.data.models

import java.io.File

data class Card(
    val audio: String,
    val category: String,
    val id: String,
    val image: String,
    val name: String,
    val subcategory: String,
    val syllables: String,
    val video: String
)

data class NewCard(
    val audio: File,
    val category: String,
    val image: File,
    val name: String,
    val subcategory: String,
    val syllables: String,
    val video: File,
)
