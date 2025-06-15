package com.api.crud.mapper;

import com.api.crud.PomodoroState;
import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.dto.response.PomodoroResponseDTO;
import com.api.crud.models.PomodoroModel;

import java.util.UUID;

public class PomodoroMapper {

    private PomodoroMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PomodoroModel toModel(PomodoroRequestDTO dto) {
        PomodoroModel model = new PomodoroModel();
        model.setFocusTime(dto.getFocusTime());
        model.setShortBreakTime(dto.getShortBreakTime());
        model.setLongBreakTime(dto.getLongBreakTime());
        model.setRounds(dto.getRounds());
        model.setTotalMinutes(dto.getTotalMinutes());
        model.setCurrentRound(dto.getCurrentRound());
        model.setCurrentState(dto.getCurrentState() != null ?
                PomodoroState.valueOf(dto.getCurrentState()) : null);
        model.setLastUpdatedDate(dto.getLastUpdatedDate());


        if (dto.getRemoteId() != null && !dto.getRemoteId().isBlank()) {
            model.setRemoteId(dto.getRemoteId());
        } else {
            model.setRemoteId(UUID.randomUUID().toString());
        }

        return model;
    }

    public static PomodoroResponseDTO toResponseDTO(PomodoroModel model) {
        PomodoroResponseDTO dto = new PomodoroResponseDTO();
        dto.setId(model.getId());
        dto.setRemoteId(model.getRemoteId());
        dto.setFocusTime(model.getFocusTime());
        dto.setShortBreakTime(model.getShortBreakTime());
        dto.setLongBreakTime(model.getLongBreakTime());
        dto.setRounds(model.getRounds());
        dto.setTotalMinutes(model.getTotalMinutes());
        dto.setCurrentRound(model.getCurrentRound());
        dto.setCurrentState(model.getCurrentState().name());
        dto.setLastUpdatedDate(model.getLastUpdatedDate());
        dto.setUserId(model.getUser().getId());
        return dto;
    }


    public static void updateModel(PomodoroModel model, PomodoroRequestDTO dto) {
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
