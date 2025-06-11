package com.api.crud;

import com.api.crud.controllers.PomodoroController;
import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.dto.response.PomodoroResponseDTO;
import com.api.crud.services.PomodoroService;
import com.api.crud.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class PomodoroControllerTest {

    @Mock
    private PomodoroService pomodoroService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PomodoroController pomodoroController;

    private PomodoroResponseDTO mockResponse;
    private PomodoroRequestDTO mockRequest;
    private final String testUserId = "test-user-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(request.getAttribute("firebaseUserId")).thenReturn(testUserId);

        mockResponse = new PomodoroResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setFocusTime(25);
        mockResponse.setShortBreakTime(5);
        mockResponse.setLongBreakTime(15);
        mockResponse.setRounds(4);

        mockRequest = new PomodoroRequestDTO();
        mockRequest.setFocusTime(25);
        mockRequest.setShortBreakTime(5);
        mockRequest.setLongBreakTime(15);
        mockRequest.setRounds(4);
        mockRequest.setUserId(testUserId);
    }

    @Test
    void getUserPomodoro_ShouldReturnPomodoroSettings() {
        when(pomodoroService.findByUserId(anyString())).thenReturn(mockResponse);

        ResponseEntity<PomodoroResponseDTO> response = pomodoroController.getUserPomodoro(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Updated assertion
        assertEquals(mockResponse, response.getBody());
        verify(pomodoroService, times(1)).findByUserId(testUserId);
    }

    @Test
    void getPomodorosSettings_ShouldReturnSettingsWhenExists() {
        when(pomodoroService.getPomodoroSettings(anyString())).thenReturn(mockResponse);

        ResponseEntity<PomodoroResponseDTO> response = pomodoroController.getPomodorosSettings(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Updated assertion
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getPomodorosSettings_ShouldReturnNoContentWhenNotExists() {
        when(pomodoroService.getPomodoroSettings(anyString())).thenReturn(null);

        ResponseEntity<PomodoroResponseDTO> response = pomodoroController.getPomodorosSettings(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); // Updated assertion
    }

    @Test
    void insertPomodoro_ShouldCreateNewPomodoro() {
        when(pomodoroService.insertPomodoro(any(PomodoroRequestDTO.class), anyString())).thenReturn(mockResponse);

        ResponseEntity<PomodoroResponseDTO> response = pomodoroController.insertPomodoro(mockRequest, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Updated assertion
        assertEquals(mockResponse, response.getBody());
        verify(pomodoroService, times(1)).insertPomodoro(mockRequest, testUserId);
    }

    @Test
    void updatePomodoro_ShouldUpdateExistingPomodoro() {
        Long pomodoroId = 1L;
        when(pomodoroService.updatePomodoro(anyLong(), any(PomodoroRequestDTO.class), anyString())).thenReturn(mockResponse);

        ResponseEntity<PomodoroResponseDTO> response = pomodoroController.updatePomodoro(pomodoroId, mockRequest, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Updated assertion
        assertEquals(mockResponse, response.getBody());
        verify(pomodoroService, times(1)).updatePomodoro(pomodoroId, mockRequest, testUserId);
    }

    @Test
    void deleteAllPomodoroSettings_ShouldDeleteAllForUser() {
        doNothing().when(pomodoroService).deleteAll(anyString());

        ResponseEntity<String> response = pomodoroController.deleteAllPomodoroSettings(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Updated assertion
        assertEquals("Todas las configuraciones del pomodoro fueron borradas con éxito!", response.getBody());
        verify(pomodoroService, times(1)).deleteAll(testUserId);
    }

    @Test
    void ping_ShouldReturnPong() {
        ResponseEntity<String> response = pomodoroController.ping();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Updated assertion
        assertEquals("pong", response.getBody());
    }
}