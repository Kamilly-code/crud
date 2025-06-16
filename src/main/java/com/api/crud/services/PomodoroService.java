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
    private final UserService userRepository;

    @Autowired
    public PomodoroService(PomodoroRepository pomodoroRepository, UserService userRepository) {
        this.pomodoroRepository = pomodoroRepository;
        this.userRepository = userRepository;
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
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        PomodoroModel model = PomodoroMapper.toModel(requestDTO);
        model.setUser(user);

        // Garante que remoteId não seja nulo
        if (model.getRemoteId() == null || model.getRemoteId().trim().isEmpty()) {
            model.setRemoteId(UUID.randomUUID().toString());
        }

        PomodoroModel saved = pomodoroRepository.save(model);
        return PomodoroMapper.toResponseDTO(saved);
    }




    public PomodoroResponseDTO updatePomodoro(String remoteId, PomodoroRequestDTO dto, String userId) {
        PomodoroModel model = pomodoroRepository.findByRemoteIdAndUserId(remoteId, userId)
                .orElseThrow(() -> new PomodoroNotFoundException(remoteId));

        PomodoroMapper.updateModel(model, dto);
        PomodoroModel updated = pomodoroRepository.save(model);

        return PomodoroMapper.toResponseDTO(updated);
    }

    public PomodoroResponseDTO getPomodoroSettings(String userId) {
        return findByUserId(userId);
    }



    public void deleteAll(String userId) {
        pomodoroRepository.deleteAllByUserId(userId);
    }

    private void updateModel(PomodoroModel model, PomodoroRequestDTO dto) {
        model.setFocusTime(dto.getFocusTime());
        model.setShortBreakTime(dto.getShortBreakTime());
        model.setLongBreakTime(dto.getLongBreakTime());
        model.setRounds(dto.getRounds());
        model.setTotalMinutes(dto.getTotalMinutes());
        model.setCurrentRound(dto.getCurrentRound());

        if (dto.getCurrentState() != null) {
            try {
                model.setCurrentState(PomodoroState.valueOf(dto.getCurrentState()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + dto.getCurrentState());
            }
        }

        if (dto.getLastUpdatedDate() != null) {
            model.setLastUpdatedDate(dto.getLastUpdatedDate());
        }
    }
}
