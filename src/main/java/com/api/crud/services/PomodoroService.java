package com.api.crud.services;

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

@Service
public class PomodoroService {

    private final PomodoroRepository pomodoroRepository;
    private final UserService userService;

    @Autowired
    public PomodoroService(PomodoroRepository pomodoroRepository, UserService userService) {
        this.pomodoroRepository = pomodoroRepository;
        this.userService = userService;
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

    public PomodoroResponseDTO insertPomodoro(PomodoroRequestDTO requestDTO, String firebaseUserId) {
        if (requestDTO.getUserId() == null || !requestDTO.getUserId().equals(firebaseUserId)) {
            throw new IllegalArgumentException("UserId no body não corresponde ao usuário autenticado.");
        }

        UserModel user = userService.findById(firebaseUserId)
                .orElseThrow(() -> new UserNotFoundException(firebaseUserId));

        Optional<PomodoroModel> existing = pomodoroRepository.findByUserId(firebaseUserId).stream().findFirst();
        PomodoroModel model = existing.orElseGet(PomodoroModel::new);

        PomodoroMapper.updateModel(model, requestDTO);
        model.setUser(user);

        PomodoroModel saved = pomodoroRepository.save(model);
        return PomodoroMapper.toResponseDTO(saved);
    }



    public PomodoroResponseDTO updatePomodoro(Long id, PomodoroRequestDTO dto, String firebaseUserId) {
        if (dto.getUserId() == null || !dto.getUserId().equals(firebaseUserId)) {
            throw new IllegalArgumentException("UserId no body não corresponde ao usuário autenticado.");
        }

        PomodoroModel model = pomodoroRepository.findByIdAndUserId(id, firebaseUserId)
                .orElseThrow(() -> new PomodoroNotFoundException(id));

        PomodoroMapper.updateModel(model, dto);
        return PomodoroMapper.toResponseDTO(pomodoroRepository.save(model));
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

}
