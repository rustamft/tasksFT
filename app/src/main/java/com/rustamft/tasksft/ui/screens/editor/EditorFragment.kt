package com.rustamft.tasksft.ui.screens.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rustamft.tasksft.R
import com.rustamft.tasksft.databinding.FragmentEditorBinding
import com.rustamft.tasksft.ui.MainActivity
import com.rustamft.tasksft.ui.screens.editor.picker.DatePickerFragment
import com.rustamft.tasksft.ui.screens.editor.picker.TimePickerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableBackCallback() // Override system back behaviour.
        setHasOptionsMenu(true) // Make onCreateOptionsMenu work.
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root // Return the view.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel // Bind ViewModel to XML var.
        binding.datePicker = DatePickerFragment(childFragmentManager, binding.editorDateButton)
        binding.timePicker = TimePickerFragment(childFragmentManager, binding.editorTimeButton)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.editor_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.onBackClicked(requireView())
                return true
            }
            R.id.editor_menu_delete -> {
                viewModel.delete()
                viewModel.navigateBack(requireView())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun enableBackCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackClicked(requireView())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}
