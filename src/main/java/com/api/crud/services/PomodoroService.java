package com.api.crud.services;

import com.api.crud.PomodoroState;
import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.manejar_errores.PomodoroNotFoundException;
import com.api.crud.manejar_errores.UserNotFoundException;
import com.api.crud.mapper.PomodoroMapper;
import com.api.crud.models.PomodoroModel;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.PomodoroRepository;
import com.api.crud.dto.response.PomodoroResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.api.crud.mapper.PomodoroMapper.toResponseDTO;
import static com.api.crud.mapper.PomodoroMapper.updateModel;

@Service
public class PomodoroService {

    private final PomodoroRepository pomodoroRepository;


    @Autowired
    public PomodoroService(PomodoroRepository pomodoroRepository) {
        this.pomodoroRepository = pomodoroRepository;
    }

    public List<PomodoroModel> getPomodoros(String userId) {
        return pomodoroRepository.findByUserId(userId);
    }

    public PomodoroResponseDTO findByUserId(String userId) {
        return pomodoroRepository.findByUserId(userId)
                .stream()
                .findFirst()
                .map(PomodoroMapper::toResponseDTO)
                .orElse(null);
    }

    public PomodoroResponseDTO insertPomodoro(PomodoroRequestDTO requestDTO, String userId) {
        PomodoroModel model = new PomodoroModel();
        updateModel(model, requestDTO);
        model.setRemoteId(requestDTO.getRemoteId() != null ?
                requestDTO.getRemoteId() : UUID.randomUUID().toString());

        UserModel user = new UserModel();
        user.setId(userId);
        model.setUser(user);

        PomodoroModel saved = pomodoroRepository.save(model);
        return toResponseDTO(saved);
    }



    public PomodoroResponseDTO updatePomodoro(String remoteId, PomodoroRequestDTO dto, String userId) {
        PomodoroModel model = pomodoroRepository.findByRemoteIdAndUserId(remoteId, userId)
                .orElseThrow(() -> new PomodoroNotFoundException(remoteId));

        updateModel(model, dto);
        return toResponseDTO(pomodoroRepository.save(model));
    }

    public PomodoroResponseDTO getPomodoroSettings(String userId) {
        return pomodoroRepository.findByUserId(userId)
                .stream()
                .findFirst()
                .map(PomodoroMapper::toResponseDTO)
                .orElse(null);
    }



    public void deleteAll(String userId) {
        pomodoroRepository.deleteByUserId(userId);
    }

    private void updateModel(PomodoroModel model, PomodoroRequestDTO dto) {
        model.setFocusTime(dto.getFocusTime());
        model.setShortBreakTime(dto.getShortBreakTime());
        model.setLongBreakTime(dto.getLongBreakTime());
        model.setRounds(dto.getRounds());
        model.setTotalMinutes(dto.getTotalMinutes());
        model.setCurrentRound(dto.getCurrentRound());

        if (dto.getCurrentState() != null) {
            model.setCurrentState(PomodoroState.valueOf(dto.getCurrentState()));
        }

        if (dto.getLastUpdatedDate() != null) {
            model.setLastUpdatedDate(dto.getLastUpdatedDate());
        }
    }
}
