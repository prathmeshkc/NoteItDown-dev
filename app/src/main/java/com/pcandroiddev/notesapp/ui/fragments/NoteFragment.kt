package com.pcandroiddev.notesapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.pcandroiddev.notesapp.R
import com.pcandroiddev.notesapp.databinding.FragmentNoteBinding
import com.pcandroiddev.notesapp.models.note.NoteRequest
import com.pcandroiddev.notesapp.models.note.NoteResponse
import com.pcandroiddev.notesapp.util.NetworkResults
import com.pcandroiddev.notesapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding get() = _binding!!

    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNotes(noteId = it._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(title = title, description = description)
            if (note != null) {
                noteViewModel.updateNotes(noteId = note!!._id, noteRequest = noteRequest)
            } else {
                noteViewModel.createNotes(noteRequest = noteRequest)
            }
        }
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResults.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResults.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResults.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        } else {
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}