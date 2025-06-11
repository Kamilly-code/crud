package com.api.crud.controllers;


import com.api.crud.dto.request.FirebaseUserSyncRequest;
import com.api.crud.dto.request.UserRequestDTO;
import com.api.crud.dto.response.UserResponseDTO;
import com.api.crud.models.UserModel;
import com.api.crud.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sync")
    public ResponseEntity<UserResponseDTO> syncUser(
            @RequestHeader("Authorization") String token,
            @RequestBody FirebaseUserSyncRequest request,
            HttpServletRequest httpRequest
    ) {
        log.info("Token recebido: {}", token);
        log.info("Token recebido: {}", token);

        String firebaseUid = (String) httpRequest.getAttribute("firebaseUserId");
        if (firebaseUid == null) {
            log.info("Firebase UID não encontrado no request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Firebase UID: {}" , firebaseUid);

        UserModel user = userService.syncUser(request, firebaseUid);
        return ResponseEntity.ok(new UserResponseDTO(
                user.getId(),
                user.getNombreUser(),
                user.getEmail(),
                user.getGenero()
        ));
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getUsers() {
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
    public ResponseEntity<UserModel> getUserById(@PathVariable String id) {
        UserModel user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUserById(@RequestBody UserModel request, @PathVariable("id") String id) {
        UserModel updatedUser = userService.updateById(request, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuário com id " + id + " foi deletado com sucesso!");
    }

}