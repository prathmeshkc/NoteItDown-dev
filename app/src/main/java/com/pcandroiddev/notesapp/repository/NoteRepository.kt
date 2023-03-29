package com.pcandroiddev.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pcandroiddev.notesapp.api.NoteService
import com.pcandroiddev.notesapp.models.note.NoteRequest
import com.pcandroiddev.notesapp.models.note.NoteResponse
import com.pcandroiddev.notesapp.util.NetworkResults
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteService: NoteService) {

    private val _notesLiveData = MutableLiveData<NetworkResults<List<NoteResponse>>>()
    val notesLiveData: LiveData<NetworkResults<List<NoteResponse>>> get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResults<String>>()
    val statusLiveData: LiveData<NetworkResults<String>> get() = _statusLiveData

    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResults.Loading())
        val response = noteService.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _notesLiveData.postValue(NetworkResults.Success(data = response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResults.Error(message = errorObj.getString("message")))
        } else {
            _notesLiveData.postValue(NetworkResults.Error(message = "Something Went Wrong!"))
        }
    }

    suspend fun createNote(
        noteRequest: NoteRequest
    ) {
        _statusLiveData.postValue(NetworkResults.Loading())
        val response =
            noteService.createNote(
                images = noteRequest.images,
                title = noteRequest.title,
                description = noteRequest.description
            )
        handleResponse(response = response, message = "Note Created!")

    }

    suspend fun updateNote(
        noteId: String,
        noteRequest: NoteRequest
    ) {
        _statusLiveData.postValue(NetworkResults.Loading())
        val response = noteService.updateNote(
            noteId = noteId,
            images = noteRequest.images,
            title = noteRequest.title,
            description = noteRequest.description
        )
        handleResponse(response = response, message = "Note Updated!")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResults.Loading())
        val response = noteService.deleteNote(noteId = noteId)
        handleResponse(response = response, message = "Note Deleted!")
    }

    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResults.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResults.Error("Something Went Wrong!"))
        }
    }

}