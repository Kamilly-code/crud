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

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel insertTask(TaskRequestDTO taskRequestDTO, UserModel user) {
        TaskModel task = new TaskModel();
        task.setTarea(taskRequestDTO.getTarea());
        task.setIsCompleted(taskRequestDTO.getCompleted() != null && taskRequestDTO.getCompleted());

        // Usar o remoteId da requisição se existir, senão criar um novo
        task.setRemoteId(taskRequestDTO.getRemoteId() != null && !taskRequestDTO.getRemoteId().isEmpty()
                ? taskRequestDTO.getRemoteId()
                : UUID.randomUUID().toString());

        if (taskRequestDTO.getDate() == null) {
            throw new IllegalArgumentException("A data da tarefa não pode ser nula");
        }
        task.setDate(taskRequestDTO.getDate());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public TaskModel updateTaskStatus(String remoteId, TaskRequestDTO dto) {
        TaskModel task = taskRepository.findByRemoteId(remoteId)
                .orElseThrow(() -> new TaskFoundException(remoteId));

        if (dto.getTarea() != null) {
            task.setTarea(dto.getTarea());
        }
        if (dto.getDate() != null) {
            task.setDate(dto.getDate());
        }

        return taskRepository.save(task);
    }

    public List<TaskModel> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public void deleteTask(long id, String userId) {
        TaskModel task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskFoundException(userId));


        taskRepository.delete(task);
    }
}