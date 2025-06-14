package com.api.crud.repositories;

import com.api.crud.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel,Long> {
    Optional<TaskModel> findByRemoteIdAndUserId(String remoteId, String userId);

    List<TaskModel> findByUserId(String userId);

    @Query("SELECT t FROM TaskModel t WHERE t.remoteId = :remoteId AND t.user.id = :userId")
    Optional<TaskModel> findByRemoteIdAndUser(@Param("remoteId") String remoteId,
                                              @Param("userId") String userId);

    boolean existsByRemoteIdAndUserId(String remoteId, String userId);
}
