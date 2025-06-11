package com.api.crud.controllers;

import com.api.crud.dto.request.NoteRequestDTO;
import com.api.crud.dto.response.NoteResponseDTO;
import com.api.crud.models.NotesModel;
import com.api.crud.models.UserModel;
import com.api.crud.services.NotesService;
import com.api.crud.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private static final Logger log = LoggerFactory.getLogger(NotesController.class);
    private static final String FIREBASE_USER_ID = "firebaseUserId"; // Defined constant
    private static final String FIREBASE_USER_EMAIL = "firebaseUserEmail";

    private final NotesService notesService;
    private final UserService userService;

    @Autowired
    public NotesController(NotesService notesService, UserService userService) {
        this.notesService = notesService;
        this.userService = userService;
    }


    @GetMapping("/my-notes")
    public ResponseEntity<List<NotesModel>> getUserNotes(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        return ResponseEntity.ok(notesService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> insertNote(@RequestBody @Valid NoteRequestDTO noteDto, HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        String userEmail = (String) request.getAttribute(FIREBASE_USER_EMAIL);
        UserModel user = userService.getOrCreateUser(userId,userEmail);

        NotesModel note = notesService.insertNote(noteDto, user);

        NoteResponseDTO response = new NoteResponseDTO();
        response.setId(note.getId());
        response.setRemoteId(note.getRemoteId());
        response.setTitle(note.getTitle());
        response.setNote(note.getNote());
        response.setDate(note.getDate());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotesModel> updateNote(
            @PathVariable Long id,
            @RequestBody NoteRequestDTO noteDto,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);

        if (log.isInfoEnabled()) {
            log.info("Atualizando nota ID: {} com dados: {}", id, noteDto);
        }

        NotesModel updatedNote = notesService.updateNote(id, noteDto, userId);

        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoteById(
            @PathVariable Long id,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        notesService.deleteNoteById(id, userId);
        return ResponseEntity.ok("Nota deletada com sucesso!");
    }


    @DeleteMapping
    public ResponseEntity<String> deleteAllNotes(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        notesService.deleteAllNotesByUserId(userId);
        return ResponseEntity.ok("Notas deletadas com sucesso!");
    }
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

}
