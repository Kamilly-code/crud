package com.api.crud.controllers;


import com.api.crud.dto.request.UserRequestDTO;
import com.api.crud.dto.response.UserResponseDTO;
import com.api.crud.models.UserModel;
import com.api.crud.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<ArrayList<UserModel>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserRequestDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        // Convertir DTO a entidad
        UserModel user = new UserModel();
        user.setNombreUser(userDTO.getNombreUser());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setGenero(userDTO.getGenero());

        UserModel savedUser = userService.saveUser(user);

        UserResponseDTO response = new UserResponseDTO(
                savedUser.getId(),
                savedUser.getNombreUser(),
                savedUser.getEmail(),
                savedUser.getGenero()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        UserModel user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUserById(@RequestBody UserModel request, @PathVariable("id") Long id) {
        UserModel updatedUser = userService.updateById(request, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuário com id " + id + " foi deletado com sucesso!");
    }
}