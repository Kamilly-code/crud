package com.api.crud.dto.response;

import com.api.crud.models.TaskModel;

import java.time.LocalDate;

public class TaskResponseDTO {
    private String remoteId;
    private String tarea;
    private Boolean isCompleted;
    private LocalDate date;

    public TaskResponseDTO(TaskModel task) {
        this.remoteId = task.getRemoteId();
        this.tarea = task.getTarea();
        this.isCompleted = task.getIsCompleted();
        this.date = task.getDate();
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}