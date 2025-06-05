package com.api.crud.dto.response;

public class UserResponseDTO  {
    private Long id;
    private String nombreUser;
    private String email;
    private String genero;

    public UserResponseDTO(Long id, String nombreUser, String email, String genero) {
        this.id = id;
        this.nombreUser = nombreUser;
        this.email = email;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
