package vn.fullStep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.fullStep.controller.request.UserCreationRequest;
import vn.fullStep.controller.request.UserPasswordRequest;
import vn.fullStep.controller.request.UserUpdateRequest;
import vn.fullStep.controller.response.UserResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mockup/user")
@Tag(name = "Mockup User Controller")
public class MockupUserController {

    @Operation(summary = "Test API", description = "This is a test API to get list users")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyWord,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {

        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(1L);
        userResponse1.setFirstName("Hoang");
        userResponse1.setLastName("Dung");
        userResponse1.setUsername("admin");
        userResponse1.setEmail("admin@gmail.com");
        userResponse1.setPhone("0123456789");
        userResponse1.setGender("Nam");
        userResponse1.setBirthday(new java.util.Date(222, 1, 1));

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2L);
        userResponse2.setFirstName("Test");
        userResponse2.setLastName("HD");
        userResponse2.setUsername("test");
        userResponse2.setEmail("test@gmail.com");
        userResponse2.setPhone("1111");
        userResponse2.setGender("Nam");
        userResponse2.setBirthday(new java.util.Date(22, 1, 1));

        List<UserResponse> userList = List.of(userResponse1, userResponse2);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Success");
        result.put("data", userList);

        return result;
    }

    @Operation(summary = "Get User Detail By ID API", description = "API to get user detail by ID")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserById(@PathVariable Long userId) {
        UserResponse userDetail = new UserResponse();
        userDetail.setId(userId);
        userDetail.setFirstName("Hoang");
        userDetail.setLastName("Dung");
        userDetail.setUsername("admin");
        userDetail.setEmail("admin@gmail.com");
        userDetail.setPhone("0123456789");
        userDetail.setGender("Nam");
        userDetail.setBirthday(new java.util.Date(222, 1, 1));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Get User Detail Success");
        result.put("data", userDetail);

        return result;
    }

    @Operation(summary = "Create User API", description = "API to create user by ID")
    @PostMapping("/create")
    public Map<String, Object> createUser(UserCreationRequest request) {

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Get User Detail Success");
        result.put("data", 3L); // Assuming the new user ID is 3

        return result;
    }

    @Operation(summary = "Delete User API", description = "API to delete user by ID")
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUserById(@PathVariable Long userId) {

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "Delete User Success");
        result.put("data", null);

        return result;
    }

    @Operation(summary = "Update User API", description = "API to update user by ID")
    @PutMapping("/update")
    public Map<String, Object> updateUserById(UserUpdateRequest request) {

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "Update User Success");
        result.put("data", "User updated successfully");

        return result;
    }

    @Operation(summary = "Update Password User API", description = "API to update password user by ID")
    @PatchMapping("/update-password")
    public Map<String, Object> updatePasswordUserById(UserPasswordRequest request) {

        Map<String, Object> result = new LinkedHashMap<>();
        // Here you would typically call a service to update the user's password
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Update Password User Success");
        result.put("data", "Password updated successfully");

        return result;
    }

}