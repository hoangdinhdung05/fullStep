package vn.fullStep.service;

import vn.fullStep.controller.request.UserCreationRequest;
import vn.fullStep.controller.request.UserPasswordRequest;
import vn.fullStep.controller.request.UserUpdateRequest;
import vn.fullStep.controller.response.UserResponse;
import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long userId);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    long save(UserCreationRequest request);

    void update(UserUpdateRequest request);

    void delete(Long userId);

    void changePassword(UserPasswordRequest request);

}
