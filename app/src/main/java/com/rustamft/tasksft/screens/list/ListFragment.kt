package com.rustamft.tasksft.screens.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rustamft.tasksft.R
import com.rustamft.tasksft.activities.MainActivity
import com.rustamft.tasksft.databinding.FragmentListBinding
import com.rustamft.tasksft.screens.list.adapter.TasksListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // To make onCreateOptionsMenu work.
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root // Return the view.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(viewModel.getNightMode())
        binding.viewModel = viewModel
        binding.listAdapter = TasksListAdapter(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        viewModel.updateNightModeMenuIcon(requireContext(), menu.getItem(1))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listAdapter?.clear()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_menu_delete_finished -> {
                viewModel.deleteFinished()
            }
            R.id.list_menu_switch_night -> {
                viewModel.switchNightMode()
                viewModel.updateNightModeMenuIcon(requireContext(), item)
            }
            R.id.list_menu_about_app -> {
                viewModel.displayAboutApp(requireContext())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
