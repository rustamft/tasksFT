package com.rustamft.tasksft.ui.screens.backup

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rustamft.tasksft.databinding.FragmentBackupBinding
import com.rustamft.tasksft.ui.MainActivity
import com.rustamft.tasksft.utils.enableBackCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackupFragment : Fragment() {

    private var _binding: FragmentBackupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BackupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().enableBackCallback(this) { // Override system back behaviour.
            viewModel.navigateBack(requireView())
        }
        setHasOptionsMenu(true) // Make onCreateOptionsMenu work.
        viewModel.exportBackupLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.exportTasks(requireView(), result)
            }
        }
        viewModel.importBackupLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.importTasks(requireView(), result)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root // Return the view.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            viewModel.navigateBack(requireView())
        }
        return super.onOptionsItemSelected(item)
    }
}
