package com.rustamft.tasksft.screens.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.databinding.ListItemBinding
import com.rustamft.tasksft.screens.list.ListFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TasksListAdapter(
    private val owner: ListFragment,
    list: Flow<List<AppTask>>,
) : ListAdapter<AppTask, TasksListAdapter.ViewHolder>(DiffCallback()) {

    private val job = owner.lifecycleScope.launch {
        list.collect {
            submitList(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.fragment = owner
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

    fun destroy() {
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
