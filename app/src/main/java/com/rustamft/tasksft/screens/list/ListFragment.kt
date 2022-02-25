package com.rustamft.tasksft.screens.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.databinding.FragmentListBinding
import com.rustamft.tasksft.screens.list.adapter.TasksListAdapter
import com.rustamft.tasksft.utils.Constants
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
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root // Return the view.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(viewModel.getNightMode())
        binding.fragment = this // Bind Fragment to XML var.
        binding.adapter = TasksListAdapter(this, viewModel.getList())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        updateNightModeMenuIcon(menu.getItem(1))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.adapter?.destroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_menu_delete_finished -> {
                viewModel.deleteFinished()
            }
            R.id.list_menu_switch_night -> {
                switchNightMode()
                updateNightModeMenuIcon(item)
            }
            R.id.list_menu_about_app -> {
                displayAboutApp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onTaskChecked(task: AppTask) {
        task.isFinished = !task.isFinished
        viewModel.update(task)
    }

    fun defineDateTimeText(task: AppTask): String = viewModel.defineDateTimeText(task)

    fun navigateNext() {
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.action_listFragment_to_editorFragment)
    }

    fun navigateNext(id: Int): Boolean {
        val bundle = Bundle()
        bundle.putInt(Constants.TASK_ID, id)
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.action_listFragment_to_editorFragment, bundle)
        return true
    }

    private fun displayAboutApp() {
        val message = getString(R.string.about_app_content) + viewModel.buildAppVersion()
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.about_app)
            .setMessage(message)
            .setPositiveButton(R.string.action_close) { _, _ ->
                // Close.
            }
            .setNegativeButton("GitHub") { _, _ ->
                openGitHub()
            }
        builder.show()
    }

    private fun switchNightMode() {
        val mode: Int = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            else -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.setNightMode(mode)
    }

    private fun updateNightModeMenuIcon(item: MenuItem) {
        val drawableID: Int = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                R.drawable.ic_night_mode_auto
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                R.drawable.ic_night_mode_on
            }
            else -> {
                R.drawable.ic_night_mode_off
            }
        }
        val drawable = ContextCompat.getDrawable(requireContext(), drawableID)
        if (item.icon != drawable) {
            item.icon = drawable
        }
    }

    private fun openGitHub() {
        val webPage = Uri.parse(Constants.GITHUB_LINK)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}
