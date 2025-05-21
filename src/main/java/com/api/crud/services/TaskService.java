package com.api.crud.services;

import com.api.crud.manejar_errores.TaskFoundException;
import com.api.crud.models.TaskModel;
import com.api.crud.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskModel insertTask(TaskModel task){
        return taskRepository.save(task);
    }

    public TaskModel updateTaskStatus(Long id,Boolean isCompleted) {
        TaskModel task = taskRepository.findById(id).orElseThrow(() -> new TaskFoundException(id));

        task.setIsCompleted(isCompleted);
        return taskRepository.save(task);
    }

    public ArrayList<TaskModel> getAllTasks() {
        return (ArrayList<TaskModel>) taskRepository.findAll();
    }

    public void deleteTask(long id) {
        if (!taskRepository.existsById(id)){
            throw new TaskFoundException(id);
        }
        taskRepository.deleteById(id);
    }

}
