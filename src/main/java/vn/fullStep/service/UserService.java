package vn.fullStep.service;

import vn.fullStep.controller.request.UserCreationRequest;
import vn.fullStep.controller.request.UserPasswordRequest;
import vn.fullStep.controller.request.UserUpdateRequest;
import vn.fullStep.controller.response.UserPageResponse;
import vn.fullStep.controller.response.UserResponse;
import java.util.List;

public interface UserService {

    UserPageResponse findAll(String keyWord, String sort, int page, int size);

    UserResponse findById(Long userId);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    long save(UserCreationRequest request);

    void update(UserUpdateRequest request);

    void delete(Long userId);

    void changePassword(UserPasswordRequest request);

}
