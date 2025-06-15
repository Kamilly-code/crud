package com.api.crud.repositories;


import com.api.crud.models.PomodoroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PomodoroRepository extends JpaRepository<PomodoroModel,Long> {
    List<PomodoroModel> findByUserId(String userId);

    Optional<PomodoroModel> findByRemoteIdAndUserId(String remoteId, String userId);

    void deleteByUserId(String userId);
}
