package com.pcandroiddev.notesapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.pcandroiddev.notesapp.R
import com.pcandroiddev.notesapp.adapters.NoteAdapter
import com.pcandroiddev.notesapp.databinding.FragmentMainBinding
import com.pcandroiddev.notesapp.models.note.NoteResponse
import com.pcandroiddev.notesapp.util.Constants.TAG
import com.pcandroiddev.notesapp.util.NetworkResults
import com.pcandroiddev.notesapp.util.TokenManager
import com.pcandroiddev.notesapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var adapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter =
            NoteAdapter(::onNoteClicked) //Use "::" notation to convert a function into a lambda
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.getNotes()
        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

        binding.btnMenu.setOnClickListener { view ->
            showMenu(view = view, menuRes = R.menu.menu_settings)
        }

        bindObservers()

    }

    private fun bindObservers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResults.Success -> {
                    adapter.submitList(it.data)
                    if (it.data?.isEmpty() == true) {
                        binding.tvEmptyResponse.visibility = View.VISIBLE
                        Log.d(TAG, "bindObservers EmptyResponse: ${it.data} ")
                    } else {
                        binding.tvEmptyResponse.visibility = View.GONE
                        Log.d(TAG, "bindObservers Response: ${it.data} ")
                    }
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

    private fun onNoteClicked(noteResponse: NoteResponse) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)

    }

    private fun showMenu(view: View, @MenuRes menuRes: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(menuRes, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when (menuItem.itemId) {
                R.id.logout -> {
                    tokenManager.deleteToken()
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
        popupMenu.setOnDismissListener {
            // Respond to popup being dismissed.
            Log.d("PopupMenu", "Dismissed")
        }
        // Show the popup menu.
        popupMenu.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
