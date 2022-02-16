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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.databinding.FragmentListBinding
import com.rustamft.tasksft.screens.list.adapter.TasksListAdapter
import com.rustamft.tasksft.utils.Const
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_finished -> {
                viewModel.deleteFinished()
            }
            R.id.action_switch_night -> {
                switchNightMode()
            }
            R.id.action_about_app -> {
                displayAboutApp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(viewModel.getNightMode())
        binding.fragment = this // Bind Fragment to XML var.
        binding.adapter = TasksListAdapter(this, viewModel.getList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.adapter!!.job.cancel() // There's view reference leak in LeakCanary without this.
        _binding = null
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
        bundle.putInt(Const.TASK_ID, id)
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.action_listFragment_to_editorFragment, bundle)
        return true
    }

    private fun displayToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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
        val mode: Int
        val message: String
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                mode = AppCompatDelegate.MODE_NIGHT_YES
                message = "Night mode is on"
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                mode = AppCompatDelegate.MODE_NIGHT_NO
                message = "Day mode is on"
            }
            else -> {
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                message = "Auto night mode is on"
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.setNightMode(mode)
        displayToast(message)
    }

    private fun openGitHub() {
        val webPage = Uri.parse(Const.GITHUB_LINK)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}
