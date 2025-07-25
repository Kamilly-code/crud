package com.api.crud.services;

import com.api.crud.dto.request.FirebaseUserSyncRequest;
import com.api.crud.manejar_errores.UserNotFoundException;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    IUserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", userId);
                    return new UserNotFoundException(userId);
                });
    }


    /*Retornar lista con todos los registros de la tabla UserModel*/
    public List<UserModel> getUsers(){
        return  userRepository.findAll();
    }

    public Optional<UserModel> findById(String userId) {
        return userRepository.findById(userId);
    }

    public UserModel saveUser(UserModel user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            throw new IllegalArgumentException("User ID no puede ser nulo");
        }
        return userRepository.save(user);
    }


    public UserModel getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserModel updateById (UserModel request ,String id){
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setNombreUser(request.getNombreUser());
        user.setEmail(request.getEmail());
        user.setGenero(request.getGenero());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserModel syncUser(FirebaseUserSyncRequest request, String firebaseUid) {
        if (firebaseUid == null || firebaseUid.isEmpty()) {
            throw new IllegalArgumentException("Firebase UID no puede ser nulo");
        }

        UserModel user = userRepository.findById(firebaseUid).orElse(new UserModel());
        user.setId(firebaseUid);
        user.setEmail(request.getEmail());
        user.setNombreUser(request.getNombreUser());
        user.setGenero(request.getGenero());
        return userRepository.save(user);
    }

    public UserModel getOrCreateUser(String firebaseUserId, String email) {
        return userRepository.findById(firebaseUserId)
                .orElseGet(() -> {
                    UserModel newUser = new UserModel();
                    newUser.setId(firebaseUserId);
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });
    }

    public UserModel updateUser(UserModel request, String id) {
        UserModel user = getUserById(id);
        user.setNombreUser(request.getNombreUser());
        user.setEmail(request.getEmail());
        user.setGenero(request.getGenero());
        return userRepository.save(user);
    }
}
