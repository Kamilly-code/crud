package com.api.crud.services;

import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.manejar_errores.PomodoroNotFoundException;
import com.api.crud.mapper.PomodoroMapper;
import com.api.crud.models.PomodoroModel;
import com.api.crud.repositories.PomodoroRepository;
import com.api.crud.dto.response.PomodoroResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PomodoroService {

    private final PomodoroRepository pomodoroRepository;

    @Autowired
    public PomodoroService(PomodoroRepository pomodoroRepository) {
        this.pomodoroRepository = pomodoroRepository;
    }

    public List<PomodoroModel> getPomodoros(){
        return pomodoroRepository.findAll();
    }

    public PomodoroResponseDTO insertPomodoro(PomodoroRequestDTO requestDTO) {
        Optional<PomodoroModel> existing = pomodoroRepository.findAll().stream().findFirst();
        PomodoroModel model = existing.orElseGet(PomodoroModel::new);
        PomodoroMapper.updateModel(model, requestDTO);
        PomodoroModel saved = pomodoroRepository.save(model);
        return PomodoroMapper.toResponseDTO(saved);
    }

    public PomodoroResponseDTO updatePomodoro(Long id, PomodoroRequestDTO dto) {
        PomodoroModel model = pomodoroRepository.findById(id)
                .orElseThrow(() -> new PomodoroNotFoundException(id));
        PomodoroMapper.updateModel(model, dto);
        return PomodoroMapper.toResponseDTO(pomodoroRepository.save(model));
    }

    public PomodoroResponseDTO getPomodoroSettings() {
        return pomodoroRepository.findAll()
                .stream()
                .findFirst()
                .map(PomodoroMapper::toResponseDTO)
                .orElse(null);
    }

    public void deleteAll() {
        pomodoroRepository.deleteAll();
    }

}
