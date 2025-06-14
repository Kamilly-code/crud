package com.api.crud.dto.response;

import com.api.crud.models.TaskModel;


public class TaskResponseDTO {
    private Long id;
    private String remoteId;
    private String tarea;
    private Boolean isCompleted;
    private String date;
    private String userId;

    public TaskResponseDTO(TaskModel task) {
        this.id = task.getId();
        this.remoteId = task.getRemoteId();
        this.tarea = task.getTarea();
        this.isCompleted = task.getIsCompleted();
        this.date = task.getDate().toString(); // ISO-8601
        this.userId = task.getUser().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}