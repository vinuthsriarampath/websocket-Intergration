package edu.vinu.springboot_websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vinu.springboot_websocket.entity.UserEntity;
import edu.vinu.springboot_websocket.model.User;
import edu.vinu.springboot_websocket.repository.UserRepository;
import edu.vinu.springboot_websocket.service.UserService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void registerUser(User user) {;
        userRepository.save(objectMapper.convertValue(user, UserEntity.class));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> objectMapper.convertValue(userEntity, User.class))
                .toList();
    }

    @Override
    public User getUserByUsername(String userName) {
        return objectMapper.convertValue(userRepository.findByUsername(userName), User.class);
    }

    @Override
    public void userJoin(String username) {
        if (!userRepository.existsByUsername(username)){
            throw new NoResultException("User Not Found");
        }
    }

    @Override
    public List<User> getAllOtherUsers(String username) {
        return getAllUsers().stream()
                .filter(user -> !user.getUsername().equals(username))
                .toList();
    }
}
