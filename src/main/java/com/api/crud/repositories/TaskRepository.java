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
    List<TaskModel> findByUserId(String userId);

    Optional<TaskModel> findByRemoteId(String remoteId);


    @Query("SELECT t FROM TaskModel t WHERE t.remoteId = :remoteId AND t.user.id = :userId")
    Optional<TaskModel> findByRemoteIdAndUserId(@Param("remoteId") String remoteId,
                                                @Param("userId") String userId);
}
