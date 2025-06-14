package com.api.crud.controllers;

import com.api.crud.dto.response.TaskResponseDTO;
import com.api.crud.models.TaskModel;
import com.api.crud.dto.request.TaskRequestDTO;
import com.api.crud.models.UserModel;
import com.api.crud.services.TaskService;
import com.api.crud.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tareas")
public class TaskController {


    private final TaskService taskService;


    private static final String FIREBASE_USER_ID = "firebaseUserId"; // Defined constant
    //private static final String FIREBASE_USER_EMAIL = "firebaseUserEmail";

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> insertTask(
            @RequestBody TaskRequestDTO taskRequestDTO,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        TaskModel task = taskService.insertTask(taskRequestDTO,userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TaskResponseDTO(task));
    }

    @PutMapping("/{remoteId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable String remoteId,
            @RequestBody TaskRequestDTO request,
            HttpServletRequest httpRequest) {

        String userId = (String) httpRequest.getAttribute(FIREBASE_USER_ID);
        TaskModel task = taskService.updateTask(remoteId, request, userId);
        return ResponseEntity.ok(new TaskResponseDTO(task));
    }


    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks(HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute(FIREBASE_USER_ID);
        List<TaskResponseDTO> tasks = taskService.getTasksByUserId(userId).stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{remoteId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String remoteId,
            HttpServletRequest httpRequest) {

        String userId = (String) httpRequest.getAttribute(FIREBASE_USER_ID);
        taskService.deleteTask(remoteId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<String> pingTasks() {
        return ResponseEntity.ok("pong-tasks");
    }
}