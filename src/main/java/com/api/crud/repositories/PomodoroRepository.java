package com.api.crud.repositories;

import com.api.crud.models.PomodoroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PomodoroRepository extends JpaRepository<PomodoroModel,Long> {
}
