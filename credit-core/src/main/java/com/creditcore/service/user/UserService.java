package com.creditcore.service.user;

import com.creditcore.dto.request.user.UserCreateRequest;
import com.creditcore.dto.response.user.UserResponse;
import com.creditcore.entity.User;
import com.creditcore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.create(
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getPassword()
        );

        User savedUser = userRepository.save(user);

        return UserResponse.of(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserResponse.of(user);
    }

}
