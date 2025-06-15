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
import java.util.Optional;
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
        Optional<NotesModel> existing = notesRepository.findByRemoteIdAndUserId(dto.getRemoteId(), userId);

        if (existing.isPresent()) {
            return existing.get();
        }

        NotesModel model = new NotesModel();
        model.setNote(dto.getNote());
        model.setTitle(dto.getTitle());
        model.setDate(dto.getDate());
        model.setRemoteId(dto.getRemoteId() != null && !dto.getRemoteId().isEmpty()
                ? dto.getRemoteId()
                : UUID.randomUUID().toString());

        UserModel user = new UserModel();
        user.setId(userId);
        model.setUser(user);

        return notesRepository.save(model);
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
    @Transactional
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
