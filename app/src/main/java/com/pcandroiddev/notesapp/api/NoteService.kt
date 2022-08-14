package com.pcandroiddev.notesapp.api

import com.pcandroiddev.notesapp.models.note.NoteRequest
import com.pcandroiddev.notesapp.models.note.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteService {

    @GET("/notes")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("notes/{noteId}")
    suspend fun updateNote(
        @Path("noteId")
        noteId: String,
        @Body
        noteRequest: NoteRequest
    ): Response<NoteResponse>

    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(
        @Path("noteId")
        noteId: String
    ): Response<NoteResponse>
}