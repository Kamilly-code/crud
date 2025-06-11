package com.api.crud;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.api.crud.controllers.TaskController;
import com.api.crud.dto.request.TaskRequestDTO;
import com.api.crud.models.TaskModel;
import com.api.crud.models.UserModel;
import com.api.crud.services.TaskService;
import com.api.crud.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskController taskController;

    private MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("firebaseUserId", "user123");
        mockRequest.setAttribute("firebaseUserEmail", "user@example.com");
    }

    @Test
    void insertTask_ShouldReturnCreatedTask() {
        // Arrange
        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setTarea("Test task");
        requestDTO.setCompleted(false);
        requestDTO.setDate(LocalDate.now());

        UserModel user = new UserModel();
        user.setId("user123");
        user.setEmail("user@example.com");

        TaskModel expectedTask = new TaskModel();
        expectedTask.setId(1L);
        expectedTask.setTarea("Test task");
        expectedTask.setIsCompleted(false);
        expectedTask.setUser(user);

        when(userService.getOrCreateUser("user123", "user@example.com")).thenReturn(user);
        when(taskService.insertTask(any(TaskRequestDTO.class), any(UserModel.class))).thenReturn(expectedTask);

        // Act
        ResponseEntity<TaskModel> response = taskController.insertTask(requestDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test task", response.getBody().getTarea());
        assertFalse(response.getBody().getIsCompleted());
    }

    @Test
    void updateTaskStatus_ShouldReturnUpdatedTask() {
        // Arrange
        Long taskId = 1L;
        Boolean isCompleted = true;
        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setTarea("Updated task");
        requestDTO.setDate(LocalDate.now());

        TaskModel updatedTask = new TaskModel();
        updatedTask.setId(taskId);
        updatedTask.setTarea("Updated task");
        updatedTask.setIsCompleted(isCompleted);

        when(taskService.updateTaskStatus(eq(taskId), any(TaskRequestDTO.class), eq("user123")))
                .thenReturn(updatedTask);

        // Act
        ResponseEntity<TaskModel> response = taskController.updateTaskStatus(taskId, requestDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskId, response.getBody().getId());
        assertEquals(isCompleted, response.getBody().getIsCompleted());
    }

    @Test
    void getAllTasks_ShouldReturnUserTasks() {
        // Arrange
        TaskModel task1 = new TaskModel();
        task1.setId(1L);
        task1.setTarea("Task 1");

        TaskModel task2 = new TaskModel();
        task2.setId(2L);
        task2.setTarea("Task 2");

        List<TaskModel> expectedTasks = Arrays.asList(task1, task2);

        when(taskService.getTasksByUserId("user123")).thenReturn(expectedTasks);

        // Act
        ResponseEntity<List<TaskModel>> response = taskController.getAllTasks(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void deleteTask_ShouldReturnSuccessMessage() {
        // Arrange
        Long taskId = 1L;

        // Act
        ResponseEntity<String> response = taskController.deleteTask(taskId, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tarea con id " + taskId + " ha sido eliminada con éxito!", response.getBody());
        verify(taskService, times(1)).deleteTask(taskId, "user123");
    }

    @Test
    void ping_ShouldReturnPong() {
        // Act
        ResponseEntity<String> response = taskController.ping();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("pong", response.getBody());
    }
}