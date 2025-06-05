package com.api.crud.controllers;

import com.api.crud.models.TaskModel;
import com.api.crud.dto.request.TaskRequestDTO;
import com.api.crud.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
public class TaskController {


    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskModel> insertTask(@RequestBody TaskRequestDTO taskRequestDTO){
        TaskModel task = taskService.insertTask(taskRequestDTO);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskModel> updateTaskStatus(@PathVariable Long id, @RequestParam Boolean isCompleted){
        TaskModel updatedTask = taskService.updateTaskStatus(id, isCompleted);
        return ResponseEntity.ok(updatedTask);
    }


    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks () {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.ok("Tarea con id" +id+ "ha sido eliminada con suceso!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

}
