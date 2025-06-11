package com.api.crud.repositories;


import com.api.crud.models.PomodoroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PomodoroRepository extends JpaRepository<PomodoroModel,Long> {
    List<PomodoroModel> findByUserId(String userId);

    @Query("SELECT p FROM PomodoroModel p WHERE p.id = :id AND p.user.id = :userId")
    Optional<PomodoroModel> findByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);

    void deleteByUserId(String userId);
}
