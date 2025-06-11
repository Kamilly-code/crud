package com.api.crud.models;

import com.api.crud.PomodoroState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pomodoros")
public class PomodoroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tiempo de enfoque es obligatorio.")
    @Min(value = 1,message = "El tiempo de enfoque debe ser mayor que 0")
    private Integer focusTime;

    @NotNull(message = "El tiempo de descanso corto es obligatorio")
    @Min(value = 1,message = "El tiempo de descanso corto debe ser mayor que 0")
    private Integer shortBreakTime;

    @NotNull(message = "El tiempo de descanso largo es obligatorio")
    @Min(value = 1,message = "El tiempo de descanso largo debe ser mayor que 0")
    private Integer longBreakTime;

    @NotNull
    @Min(value = 1,message = "Debe haber al menos una ronda")
    private Integer rounds;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int totalMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state")
    private PomodoroState currentState;

    private String lastUpdatedDate;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int currentRound;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private UserModel user;


    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(Integer focusTime) {
        this.focusTime = focusTime;
    }

    public Integer getShortBreakTime() {
        return shortBreakTime;
    }

    public void setShortBreakTime(Integer shortBreakTime) {
        this.shortBreakTime = shortBreakTime;
    }

    public Integer getLongBreakTime() {
        return longBreakTime;
    }

    public void setLongBreakTime(Integer longBreakTime) {
        this.longBreakTime = longBreakTime;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public PomodoroState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PomodoroState currentState) {
        this.currentState = currentState;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
