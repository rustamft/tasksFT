package com.rustamft.tasksft.screens.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.rustamft.tasksft.R
import com.rustamft.tasksft.activities.MainActivity
import com.rustamft.tasksft.databinding.FragmentEditorBinding
import com.rustamft.tasksft.screens.editor.picker.DatePickerFragment
import com.rustamft.tasksft.screens.editor.picker.TimePickerFragment
import com.rustamft.tasksft.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableBackCallback() // Override system back behaviour.
        setActionbarBackEnabled(true) // Display ActionBar back button
        setHasOptionsMenu(true) // Make onCreateOptionsMenu work.
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root // Return the view.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this // Bind Fragment to XML var.
        binding.viewModel = viewModel // Bind ViewModel to XML var.
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
                onBackClicked()
                return true
            }
            R.id.editor_menu_delete -> {
                if (viewModel.observableTask.id != -1) {
                    viewModel.delete()
                }
                navigateBack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun displayDatePicker() {
        val datePicker = DatePickerFragment(binding.editorDateButton)
        datePicker.show(childFragmentManager, Constants.DATE_PICKER_DIALOG_TAG)
    }

    fun displayTimePicker() {
        val timePicker = TimePickerFragment(binding.editorTimeButton)
        timePicker.show(childFragmentManager, Constants.TIME_PICKER_DIALOG_TAG)
    }

    fun save() {
        lifecycleScope.launch {
            val deferred = lifecycleScope.async {
                try {
                    viewModel.save()
                } catch (e: Exception) {
                    e.printStackTrace()
                    displayToast(e.message.toString())
                }
            }
            deferred.await()
            if (viewModel.hasTaskReminder()) {
                val dateTime = viewModel.dateTimeUntilReminder()
                var message = getString(R.string.reminder_in)
                if (dateTime.month > 0) {
                    message += dateTime.month.toString() + getString(R.string.reminder_months)
                }
                if (dateTime.day > 0) {
                    message += dateTime.day.toString() + getString(R.string.reminder_days)
                }
                if (dateTime.hour > 0) {
                    message += dateTime.hour.toString() + getString(R.string.reminder_hours)
                }
                if (dateTime.minute > 0) {
                    message += dateTime.minute.toString() + getString(R.string.reminder_minutes)
                }
                displayToast(message)
            }
            navigateBack()
        }
    }

    private fun enableBackCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackClicked()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun setActionbarBackEnabled(isEnabled: Boolean) {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(isEnabled)
    }

    private fun onBackClicked() {
        if (viewModel.isTaskChanged()) {
            displaySaveDialog()
        } else {
            navigateBack()
        }
    }

    private fun displaySaveDialog() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.unsaved_changes))
            .setMessage(getString(R.string.what_to_do))
            .setPositiveButton(getString(R.string.action_save)) { _, _ -> save() }
            .setNegativeButton(getString(R.string.action_cancel)) { _, _ -> /* Close the dialog */ }
            .setNeutralButton(getString(R.string.action_discard)) { _, _ -> navigateBack() }
        builder.show()
    }

    private fun displayToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun navigateBack() {
        val navController = NavHostFragment.findNavController(this)
        setActionbarBackEnabled(false)
        navController.navigate(R.id.action_editorFragment_to_listFragment)
    }
}
