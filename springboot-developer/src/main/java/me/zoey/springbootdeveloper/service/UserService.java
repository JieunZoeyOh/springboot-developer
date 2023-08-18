package me.zoey.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.zoey.springbootdeveloper.domain.User;
import me.zoey.springbootdeveloper.dto.AddUserRequest;
import me.zoey.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest request) {
        User joinUser = User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(joinUser);
        return joinUser.getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }

}
