package com.api.crud.repositories;

import com.api.crud.models.NotesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<NotesModel,Long> {
    List<NotesModel> findByUserId(String userId);

    Optional<NotesModel> findByRemoteIdAndUserId(String remoteId, String userId);

    boolean existsByRemoteIdAndUserId(String remoteId, String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}
