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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private static final Logger log = LoggerFactory.getLogger(NotesController.class);
    private static final String FIREBASE_USER_ID = "firebaseUserId"; // Defined constant
    //private static final String FIREBASE_USER_EMAIL = "firebaseUserEmail";

    private final NotesService notesService;

    @Autowired
    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }


    @GetMapping("/my-notes")
    public ResponseEntity<List<NoteResponseDTO>> getUserNotes(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        List<NoteResponseDTO> notes = notesService.findByUserId(userId).stream()
                .map(NoteResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> insertNote(@RequestBody @Valid NoteRequestDTO noteDto, HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        NotesModel note = notesService.insertNote(noteDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NoteResponseDTO(note));
    }

    @PutMapping("/{remoteId}")
    public ResponseEntity<NoteResponseDTO> updateNote(
            @PathVariable String remoteId,
            @RequestBody NoteRequestDTO noteDto,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);

        if (log.isInfoEnabled()) {
            log.info("Atualizando nota ID: {} com dados: {}", remoteId, noteDto);
        }

        NotesModel updatedNote = notesService.updateNote(remoteId, noteDto, userId);
        return ResponseEntity.ok(new NoteResponseDTO(updatedNote));
    }

    @PutMapping("/{remoteId}")
    public ResponseEntity<String> deleteNoteById(
            @PathVariable String remoteId,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        notesService.deleteNoteByRemoteId(remoteId, userId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping
    public ResponseEntity<String> deleteAllNotes(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        notesService.deleteAllNotesByUserId(userId);
        return ResponseEntity.ok("Notas deletadas com sucesso!");
    }
    @GetMapping("/ping")
    public ResponseEntity<String> pingNotes() {
        return ResponseEntity.ok("pong-notes");
    }

}
