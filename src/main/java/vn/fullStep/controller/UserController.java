package vn.fullStep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fullStep.common.Gender;
import vn.fullStep.controller.request.UserCreationRequest;
import vn.fullStep.controller.request.UserPasswordRequest;
import vn.fullStep.controller.request.UserUpdateRequest;
import vn.fullStep.controller.response.UserPageResponse;
import vn.fullStep.controller.response.UserResponse;
import vn.fullStep.entity.User;
import vn.fullStep.service.UserService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@Slf4j(topic = "USER_CONTROLLER")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Test API", description = "This is a test API to get list users")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyWord,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        log.info("Fetching user list with keyword: {}, sort: {}, page: {}, size: {}", keyWord, sort, page, size);

//        this.userService.findAll(keyWord, sort, page, size);

        UserPageResponse userList = this.userService.findAll(keyWord, sort, page, size);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Success");
        result.put("data", userList);

        return result;
    }

    @Operation(summary = "Get User Detail By ID API", description = "API to get user detail by ID")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserById(@PathVariable Long userId) {

        log.info("Fetching user details for user ID: {}", userId);

        UserResponse user = this.userService.findById(userId);

//        UserResponse userDetail = new UserResponse();
//        userDetail.setId(userId);
//        userDetail.setFirstName("Hoang");
//        userDetail.setLastName("Dung");
//        userDetail.setUsername("admin");
//        userDetail.setEmail("admin@gmail.com");
//        userDetail.setPhone("0123456789");
//        userDetail.setGender("Nam");
//        userDetail.setBirthday(new java.util.Date(222, 1, 1));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "Get User Detail Success");
        result.put("data", user);

        return result;
    }

    @Operation(summary = "Create User API", description = "API to create user by ID")
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserCreationRequest request) {
        log.info("Creating user with request: {}", request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "Create User Success");
        result.put("data", this.userService.save(request));
//        this.userService.save(request);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete User API", description = "API to delete user by ID")
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUserById(@PathVariable Long userId) {

        log.info("Deleting user with ID: {}", userId);
        //Only change to isActive state but not delete from db
        this.userService.delete(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "Delete User Success");
        result.put("data", null);

        return result;
    }

    @Operation(summary = "Update User API", description = "API to update user by ID")
    @PutMapping("/update")
    public Map<String, Object> updateUserById(@RequestBody UserUpdateRequest request) {

        log.info("Updating user with request: {}", request);
        this.userService.update(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "Update User Success");
        result.put("data", "User updated successfully");

        return result;
    }

    @Operation(summary = "Update Password User API", description = "API to update password user by ID")
    @PatchMapping("/update-password")
    public Map<String, Object> updatePasswordUserById(@RequestBody UserPasswordRequest request) {

        log.info("Updating password for user with request: {}", request);

        this.userService.changePassword(request);

        Map<String, Object> result = new LinkedHashMap<>();
        // Here you would typically call a service to update the user's password
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Update Password User Success");
        result.put("data", "Password updated successfully");

        return result;
    }

}
