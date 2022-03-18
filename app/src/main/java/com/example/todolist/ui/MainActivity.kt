package com.example.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.todolist.Adapter.TaskListAdapter
import com.example.todolist.dataSource.TaskDataSource
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding
    private val adapter by lazy {TaskListAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerTask.adapter = adapter
        updateList()



        insertListeners()

    }

    private fun insertListeners() {

        binding.floatingActionButton.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEditar = {

            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)

        }

        adapter.listenerDeletar = {

            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList(){
        val list = TaskDataSource.getList()

        if (list.isEmpty()){
            binding.includeState.emptyState.visibility = View.VISIBLE
        }else{
            binding.includeState.emptyState.visibility = View.GONE
        }

        adapter.submitList(list)
    }


    companion object{
        private const val CREATE_NEW_TASK = 1000
    }
}