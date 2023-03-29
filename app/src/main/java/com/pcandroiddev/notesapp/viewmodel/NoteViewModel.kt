package com.pcandroiddev.notesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.notesapp.models.note.NoteRequest
import com.pcandroiddev.notesapp.models.note.NoteResponse
import com.pcandroiddev.notesapp.repository.NoteRepository
import com.pcandroiddev.notesapp.util.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val notesLiveData: LiveData<NetworkResults<List<NoteResponse>>> get() = noteRepository.notesLiveData
    val statusLiveData: LiveData<NetworkResults<String>> get() = noteRepository.statusLiveData

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNotes()
        }
    }

    fun createNotes(
        noteRequest: NoteRequest
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.createNote(noteRequest = noteRequest)
        }
    }

    fun updateNotes(
        noteId: String,
        noteRequest: NoteRequest
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(
                noteId = noteId,
                noteRequest = noteRequest
            )
        }
    }

    fun deleteNotes(noteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(noteId = noteId)
        }
    }


}