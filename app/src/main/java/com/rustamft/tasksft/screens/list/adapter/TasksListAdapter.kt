package com.rustamft.tasksft.screens.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.databinding.ListItemBinding
import com.rustamft.tasksft.screens.list.ListViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class TasksListAdapter(private val viewModel: ListViewModel) :
    ListAdapter<AppTask, TasksListAdapter.ViewHolder>(DiffCallback()) {

    private val job = viewModel.viewModelScope.launch {
        viewModel.list.collect {
            ensureActive()
            submitList(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.viewModel = viewModel
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.task = this
                binding.checked = ObservableBoolean(isFinished)
            }
        }
    }

    fun clear() {
        job.cancel()
    }

    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<AppTask>() {
        override fun areItemsTheSame(oldItem: AppTask, newItem: AppTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AppTask, newItem: AppTask): Boolean {
            return oldItem == newItem
        }
    }
}
