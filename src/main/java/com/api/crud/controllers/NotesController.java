package com.api.crud.controllers;

import com.api.crud.Request.NoteRequestDTO;
import com.api.crud.models.NotesModel;
import com.api.crud.services.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @GetMapping
    public ResponseEntity<ArrayList<NotesModel>> getAllNotes() {
        return ResponseEntity.ok(notesService.getAllNotes());
    }

    @PostMapping
    public ResponseEntity<NotesModel> insertNote(@RequestBody NoteRequestDTO noteDto) {
        return ResponseEntity.ok(notesService.insertNote(noteDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotesModel> updateNote(@PathVariable Long id, @RequestBody NoteRequestDTO noteDto) {
        return ResponseEntity.ok(notesService.updateNote(id, noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoteById(@PathVariable Long id) {
        notesService.deleteNoteById(id);
        return ResponseEntity.ok("Nota con id " + id + " ha sido borrado con suceso!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllNotes() {
        notesService.deleteAllNotes();
        return ResponseEntity.ok("Todas las notas fueron borradas con suceso!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

}
