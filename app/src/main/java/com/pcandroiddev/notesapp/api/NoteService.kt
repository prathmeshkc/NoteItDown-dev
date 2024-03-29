package com.pcandroiddev.notesapp.api

import com.pcandroiddev.notesapp.models.note.NoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NoteService {

    @GET("/notes")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @Multipart
    @POST("/notes")
    suspend fun createNote(
        @Part images: List<MultipartBody.Part> = listOf(),
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Response<NoteResponse>

    @Multipart
    @PUT("notes/{noteId}")
    suspend fun updateNote(
        @Path("noteId")
        noteId: String,
        @Part images: List<MultipartBody.Part> = listOf(),
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Response<NoteResponse>

    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(
        @Path("noteId")
        noteId: String
    ): Response<NoteResponse>
}