package com.api.crud.services;

import com.api.crud.manejar_errores.NoteNotFoundException;
import com.api.crud.dto.request.NoteRequestDTO;
import com.api.crud.models.NotesModel;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.NotesRepository;
import jakarta.transaction.Transactional;
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

    public List<NotesModel> findByUserId(String userId) {
        return notesRepository.findByUserId(userId);
    }

    public  NotesModel insertNote(NoteRequestDTO dto , String userId){
        NotesModel note = new NotesModel();
        note.setTitle(dto.getTitle());
        note.setNote(dto.getNote());
        note.setRemoteId(dto.getRemoteId() != null && !dto.getRemoteId().isEmpty()
                ? dto.getRemoteId()
                : UUID.randomUUID().toString());

        // Converta a String para LocalDate
        if (dto.getDate() == null) {
            throw new IllegalArgumentException("A data da nota não pode ser nula");
        }
        note.setDate(dto.getDate());
        UserModel user = new UserModel();
        user.setId(userId);
        note.setUser(user);

        return notesRepository.save(note);
    }

    @Transactional
    public NotesModel updateNote(String remoteId, NoteRequestDTO dto, String userId) {
        NotesModel existingNote = notesRepository.findByRemoteIdAndUserId(remoteId, userId)
                .orElseThrow(() -> new NoteNotFoundException(remoteId));

        if(dto.getTitle() != null) {
            existingNote.setTitle(dto.getTitle());
        }
        if(dto.getNote() != null) {
            existingNote.setNote(dto.getNote());
        }
        if(dto.getDate() != null) {
            existingNote.setDate(dto.getDate());
        }

        return notesRepository.save(existingNote);
    }


    public void deleteAllNotes() {
        notesRepository.deleteAll();
    }

    public void deleteNoteByRemoteId(String remoteId, String userId) {
        NotesModel note = notesRepository.findByRemoteIdAndUserId(remoteId, userId)
                .orElseThrow(() -> new NoteNotFoundException(remoteId));
        notesRepository.delete(note);
    }

    public void deleteAllNotesByUserId(String userId) {
        List<NotesModel> userNotes = notesRepository.findByUserId(userId);
        if (userNotes.isEmpty()) {
            throw new NoteNotFoundException("Nenhuma nota encontrada para o usuário com ID: " + userId);
        }
        notesRepository.deleteAll(userNotes);
    }

}
