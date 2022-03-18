package com.example.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.Model.Task
import com.example.todolist.dataSource.TaskDataSource
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.sql.Time
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.txtInpTitulo.text = it.titulo
                binding.txtInpDescription.text = it.descricao
                binding.txtInpHora.text = it.hora
                binding.txtInpData.text = it.data
            }
        }

        insertListeners()
    }

    private fun insertListeners() {

        // Metodo para acessar o calendario e seta a data escolhida pelo usuario
        binding.txtInpData.editText?.setOnClickListener {
          val datePicker =  MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.txtInpData.text =  Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        // Metodo para acessar o relogio e setar o time
        binding.txtInpHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}"
                else timePicker.minute

                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}"
                else timePicker.hour

                binding.txtInpHora.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager,null)
        }

        // Metodo para cancelar a tarefa
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        // Metodo para criar a tarefa
        binding.btnCriarTarefa.setOnClickListener {

            val task = Task(
                titulo = binding.txtInpTitulo.text,
                descricao = binding.txtInpDescription.text,
                hora = binding.txtInpHora.text,
                data = binding.txtInpData.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }



}