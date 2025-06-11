package com.api.crud;

import com.api.crud.controllers.NotesController;
import com.api.crud.dto.request.NoteRequestDTO;
import com.api.crud.dto.response.NoteResponseDTO;
import com.api.crud.models.NotesModel;
import com.api.crud.models.UserModel;
import com.api.crud.services.NotesService;
import com.api.crud.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class NotesControllerTest {
    @Mock
    private NotesService notesService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotesController notesController;

    private MockHttpServletRequest mockRequest;
    private final String testUserId = "test-user-id";
    private final String testUserEmail = "test@example.com";

    @BeforeEach
    void setUp() {
        mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("firebaseUserId", testUserId);
        mockRequest.setAttribute("firebaseUserEmail", testUserEmail);
    }

    @Test
    void getUserNotes_ShouldReturnNotesList() {
        // Arrange
        NotesModel note1 = createTestNote(1L);
        NotesModel note2 = createTestNote(2L);
        List<NotesModel> expectedNotes = Arrays.asList(note1, note2);

        when(notesService.findByUserId(testUserId)).thenReturn(expectedNotes);

        // Act
        ResponseEntity<List<NotesModel>> response = notesController.getUserNotes(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedNotes, response.getBody());
        verify(notesService).findByUserId(testUserId);
    }

    @Test
    void insertNote_ShouldCreateNewNoteAndReturnDTO() {
        // Arrange
        NoteRequestDTO requestDTO = new NoteRequestDTO();
        requestDTO.setTitle("Test Title");
        requestDTO.setNote("Test content");
        requestDTO.setDate(LocalDate.now());
        requestDTO.setRemoteId(UUID.randomUUID().toString());

        UserModel testUser = new UserModel();
        testUser.setId(testUserId);
        testUser.setEmail(testUserEmail);

        NotesModel savedNote = createTestNote(1L);
        savedNote.setTitle(requestDTO.getTitle());
        savedNote.setNote(requestDTO.getNote());
        savedNote.setRemoteId(requestDTO.getRemoteId());
        savedNote.setDate(requestDTO.getDate());
        savedNote.setUser(testUser);

        when(userService.getOrCreateUser(testUserId, testUserEmail)).thenReturn(testUser);
        when(notesService.insertNote(any(NoteRequestDTO.class), any(UserModel.class))).thenReturn(savedNote);

        // Act
        ResponseEntity<NoteResponseDTO> response = notesController.insertNote(requestDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedNote.getId(), response.getBody().getId());
        assertEquals(savedNote.getRemoteId(), response.getBody().getRemoteId());
        assertEquals(savedNote.getTitle(), response.getBody().getTitle());
        assertEquals(savedNote.getNote(), response.getBody().getNote());
        assertEquals(savedNote.getDate(), response.getBody().getDate());

        verify(userService).getOrCreateUser(testUserId, testUserEmail);
        verify(notesService).insertNote(any(NoteRequestDTO.class), any(UserModel.class));
    }

    @Test
    void updateNote_ShouldUpdateExistingNote() {
        // Arrange
        Long noteId = 1L;
        NoteRequestDTO updateDTO = new NoteRequestDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setNote("Updated content");
        updateDTO.setDate(LocalDate.now().plusDays(1));

        NotesModel updatedNote = createTestNote(noteId);
        updatedNote.setTitle(updateDTO.getTitle());
        updatedNote.setNote(updateDTO.getNote());
        updatedNote.setDate(updateDTO.getDate());

        when(notesService.updateNote(eq(noteId), any(NoteRequestDTO.class), eq(testUserId))).thenReturn(updatedNote);

        // Act
        ResponseEntity<NotesModel> response = notesController.updateNote(noteId, updateDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(noteId, response.getBody().getId());
        assertEquals(updateDTO.getTitle(), response.getBody().getTitle());
        assertEquals(updateDTO.getNote(), response.getBody().getNote());
        assertEquals(updateDTO.getDate(), response.getBody().getDate());

        verify(notesService).updateNote(eq(noteId), any(NoteRequestDTO.class), eq(testUserId));
    }

    @Test
    void deleteNoteById_ShouldDeleteNote() {
        // Arrange
        Long noteId = 1L;

        // Act
        ResponseEntity<String> response = notesController.deleteNoteById(noteId, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nota deletada com sucesso!", response.getBody());

        verify(notesService).deleteNoteById(noteId, testUserId);
    }

    @Test
    void deleteAllNotes_ShouldDeleteAllUserNotes() {
        // Act
        ResponseEntity<String> response = notesController.deleteAllNotes(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notas deletadas com sucesso!", response.getBody());

        verify(notesService).deleteAllNotesByUserId(testUserId);
    }

    @Test
    void ping_ShouldReturnPong() {
        // Act
        ResponseEntity<String> response = notesController.ping();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("pong", response.getBody());
    }

    private NotesModel createTestNote(Long id) {
        NotesModel note = new NotesModel();
        note.setId(id);
        note.setRemoteId(UUID.randomUUID().toString());
        note.setTitle("Test Note");
        note.setNote("This is a test note content");
        note.setDate(LocalDate.now());
        return note;
    }
}
