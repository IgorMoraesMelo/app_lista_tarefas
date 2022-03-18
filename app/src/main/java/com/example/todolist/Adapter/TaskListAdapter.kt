package com.example.todolist.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Model.Task
import com.example.todolist.R
import com.example.todolist.databinding.ItemTaskBinding

class TaskListAdapter : androidx.recyclerview.widget.ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEditar : (Task) -> Unit = {}
    var listenerDeletar : (Task) -> Unit = {}

    // Responsavel por criar a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val inflater =  LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        
        holder.bind(getItem(position))

    }


    inner class TaskViewHolder(private val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Task) {
            binding.txtTitleTarefa.text = item.titulo
            binding.txtDataHora.text = "${item.data} ${item.hora}"
            binding.imgMore.setOnClickListener {
                showPopup(item)
            }
            }

        private fun showPopup(item: Task) {

            val imgMore = binding.imgMore
            val popupMenu = PopupMenu(imgMore.context, imgMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
               when(it.itemId){
                   R.id.editar -> listenerEditar(item)
                   R.id.deletar -> listenerDeletar(item)
               }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
    }

class DiffCallback : DiffUtil.ItemCallback<Task>(){

    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
}