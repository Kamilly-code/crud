package com.api.crud.controllers;

import com.api.crud.models.TaskModel;
import com.api.crud.dto.request.TaskRequestDTO;
import com.api.crud.models.UserModel;
import com.api.crud.services.TaskService;
import com.api.crud.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
public class TaskController {


    private final TaskService taskService;
    private final UserService userService;

    private static final String FIREBASE_USER_ID = "firebaseUserId"; // Defined constant
    private static final String FIREBASE_USER_EMAIL = "firebaseUserEmail";

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TaskModel> insertTask(
            @RequestBody TaskRequestDTO taskRequestDTO,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        String userEmail = (String) request.getAttribute(FIREBASE_USER_EMAIL);
        UserModel user = userService.getOrCreateUser(userId,userEmail);

        TaskModel task = taskService.insertTask(taskRequestDTO, user);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskModel> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody TaskRequestDTO taskRequestDTO, // Adicionar TaskRequestDTO
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        TaskModel updatedTask = taskService.updateTaskStatus(id,taskRequestDTO, userId);

        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        taskService.deleteTask(id, userId);

        return ResponseEntity.ok("Tarea con id " + id + " ha sido eliminada con éxito!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}