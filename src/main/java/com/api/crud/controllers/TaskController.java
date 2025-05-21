package com.api.crud.controllers;

import com.api.crud.models.TaskModel;
import com.api.crud.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/tareas")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskModel> insertTask(@RequestBody TaskModel task){
        return ResponseEntity.ok(taskService.insertTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskModel> updateTaskStatus(@PathVariable Long id, @RequestParam Boolean isCompleted){
        TaskModel updatedTask = taskService.updateTaskStatus(id, isCompleted);
        return ResponseEntity.ok(updatedTask);
    }


    @GetMapping
    public ResponseEntity<ArrayList<TaskModel>> getAllTasks () {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.ok("Tarea con id" +id+ "ha sido eliminada con suceso!");
    }

}
