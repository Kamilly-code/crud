package com.api.crud.services;

import com.api.crud.manejar_errores.NoteNotFoundException;
import com.api.crud.request.NoteRequestDTO;
import com.api.crud.models.NotesModel;
import com.api.crud.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class NotesService {


    private final NotesRepository notesRepository;

    @Autowired
    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public List<NotesModel> getAllNotes() {
        return notesRepository.findAll();
    }

    public  NotesModel insertNote(NoteRequestDTO noteDto){
        NotesModel note = new NotesModel();
        note.setTitle(noteDto.getTitle());
        note.setNote(noteDto.getNote());
        note.setRemoteId(UUID.randomUUID().toString());

        return notesRepository.save(note);
    }

    public NotesModel updateNote(Long id, NoteRequestDTO dto) {
        NotesModel existingNote = notesRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Nota com ID " + id + " não encontrada"));

        existingNote.setTitle(dto.getTitle());
        existingNote.setNote(dto.getNote());


        return notesRepository.save(existingNote);
    }

    public void deleteNoteById (Long id) {
        if (!notesRepository.existsById(id)){
            throw new NoteNotFoundException(id);
        }
        notesRepository.deleteById(id);
    }

    public void deleteAllNotes() {
        notesRepository.deleteAll();
    }

}
