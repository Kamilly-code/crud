package com.api.crud.dto.response;

public class UserResponseDTO  {
    private String id;
    private String nombreUser;
    private String email;
    private String genero;

    public UserResponseDTO(String id, String nombreUser, String email, String genero) {
        this.id = id;
        this.nombreUser = nombreUser;
        this.email = email;
        this.genero = genero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
