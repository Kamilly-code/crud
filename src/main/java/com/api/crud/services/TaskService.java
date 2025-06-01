package com.api.crud.services;

import com.api.crud.manejar_errores.TaskFoundException;
import com.api.crud.models.TaskModel;
import com.api.crud.repositories.TaskRepository;
import com.api.crud.request.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class TaskService {


    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel insertTask(TaskRequestDTO taskRequestDTO){
        TaskModel task = new TaskModel();
        task.setTarea(taskRequestDTO.getTarea());
        task.setIsCompleted(taskRequestDTO.getCompleted() != null && taskRequestDTO.getCompleted());
        task.setRemoteId(UUID.randomUUID().toString());
        return taskRepository.save(task);
    }

    public TaskModel updateTaskStatus(Long id,Boolean isCompleted) {
        TaskModel task = taskRepository.findById(id).orElseThrow(() -> new TaskFoundException(id));

        task.setIsCompleted(isCompleted);
        return taskRepository.save(task);
    }

    public List<TaskModel> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(long id) {
        if (!taskRepository.existsById(id)){
            throw new TaskFoundException(id);
        }
        taskRepository.deleteById(id);
    }

}
