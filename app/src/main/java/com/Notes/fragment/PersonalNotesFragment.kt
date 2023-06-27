package com.Notes.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.Notes.activity.DetailNoteActivity
import com.Notes.adapter.NoteAdapter
import com.Notes.databinding.FragmentPersonalNotesBinding
import com.Notes.entity.Note
import com.Notes.viewModel.NotesViewModel

class PersonalNotesFragment : Fragment() {
    private var _binding: FragmentPersonalNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listNoteAdapter: NoteAdapter
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initListener()

    }

    private fun initView() {

        binding.rvPersonal.setHasFixedSize(true)
        listNoteAdapter = NoteAdapter()
        listNoteAdapter.notifyDataSetChanged()

        binding.rvPersonal.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvPersonal.adapter = listNoteAdapter

        listNoteAdapter.setOnClicked(object : NoteAdapter.NoteListener {
            override fun onItemClicked(note: Note) {
                val intent = Intent(context, DetailNoteActivity::class.java)
                intent.putExtra(DetailNoteActivity().editNoteExtra, note)
                startActivity(intent)
            }

        })

    }

    private fun initViewModel() {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        notesViewModel.getNotes().observe(viewLifecycleOwner, Observer { notes ->

            if (notes.isNotEmpty()) {
                binding.textViewNoteEmpty.visibility = View.GONE
            } else {
                binding.textViewNoteEmpty.visibility = View.VISIBLE
            }

            listNoteAdapter.setData(notes)
        })
    }

    private fun initListener() {
        notesViewModel.setNotesByLabel("Personal")
    }

    override fun onResume() {
        super.onResume()

        //update list
        initListener()
    }
}