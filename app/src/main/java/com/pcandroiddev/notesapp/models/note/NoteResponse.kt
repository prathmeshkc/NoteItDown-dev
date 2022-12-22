package com.pcandroiddev.notesapp.models.note

data class NoteResponse(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val img_urls: List<ImgUrl>,
    val title: String,
    val updatedAt: String,
    val userId: String
)