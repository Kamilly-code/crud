package com.api.crud.services;

import com.api.crud.manejar_errores.TaskFoundException;
import com.api.crud.models.TaskModel;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.TaskRepository;
import com.api.crud.dto.request.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }


    public TaskModel insertTask(TaskRequestDTO taskRequestDTO, String userId) {
        try {
            UserModel user = userService.getUserById(userId);

            TaskModel task = new TaskModel();
            task.setTarea(taskRequestDTO.getTarea());
            task.setIsCompleted(taskRequestDTO.getIsCompleted() != null ?
                    taskRequestDTO.getIsCompleted() : false);
            task.setDate(taskRequestDTO.getDate());
            task.setUser(user);

            // Gera um remoteId se não foi fornecido
            task.setRemoteId(taskRequestDTO.getRemoteId() != null ?
                    taskRequestDTO.getRemoteId() : UUID.randomUUID().toString());

            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la tarea: " + e.getMessage(), e);
        }
    }

    public TaskModel updateTask(String remoteId, TaskRequestDTO request, String userId) {
        TaskModel task = taskRepository.findByRemoteIdAndUserId(remoteId, userId)
                .orElseThrow(() -> new TaskFoundException(remoteId));

        if (request.getTarea() != null) {
            task.setTarea(request.getTarea());
        }
        if (request.getCompleted() != null) {
            task.setIsCompleted(request.getCompleted());
        }
        if (request.getDate() != null) {
            task.setDate(request.getDate());
        }

        return taskRepository.save(task);
    }

    public List<TaskModel> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public void deleteTask(String remoteId, String userId) {
        TaskModel task = taskRepository.findByRemoteIdAndUserId(remoteId, userId)
                .orElseThrow(() -> new TaskFoundException(remoteId));
        taskRepository.delete(task);
    }
}