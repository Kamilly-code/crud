package com.api.crud.repositories;

import com.api.crud.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel,Long> {
    List<TaskModel> findByUserId(String userId);

    Optional<TaskModel> findByRemoteId(String remoteId);

    Optional<TaskModel> findByIdAndUserId(Long id, String userId);
}
