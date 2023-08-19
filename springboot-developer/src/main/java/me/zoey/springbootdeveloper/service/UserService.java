package me.zoey.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.zoey.springbootdeveloper.domain.User;
import me.zoey.springbootdeveloper.dto.AddUserRequest;
import me.zoey.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(AddUserRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User joinUser = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();

        userRepository.save(joinUser);
        return joinUser.getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }

}
