package vn.fullStep.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import vn.fullStep.common.Gender;
import vn.fullStep.controller.response.UserPageResponse;
import vn.fullStep.controller.response.UserResponse;
import vn.fullStep.service.JwtService;
import vn.fullStep.service.UserService;
import vn.fullStep.service.UserServiceDetail;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockServiceConfig.class)
class UserControllerTest {

    private final MockMvc mockMvc;
    private final UserService userService;

    UserControllerTest(MockMvc mockMvc, UserService userService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    private UserResponse tayJava;
    private UserResponse johnDoe;

    @BeforeEach
    void setUp() {
        tayJava = new UserResponse();
        tayJava.setId(1L);
        tayJava.setFirstName("Tay");
        tayJava.setLastName("Java");
        tayJava.setGender(Gender.MALE);
        tayJava.setBirthday(new Date());
        tayJava.setEmail("quoctay87@gmail.com");
        tayJava.setPhone("0975118228");
        tayJava.setUsername("tayjava");

        johnDoe = new UserResponse();
        johnDoe.setId(2L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setGender(Gender.FEMALE);
        johnDoe.setBirthday(new Date());
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPhone("0123456789");
        johnDoe.setUsername("johndoe");
    }

    @Test
    @WithMockUser(authorities = {"admin", "manager"})
    void shouldGetUserList() throws Exception {
        List<UserResponse> userListResponses = List.of(tayJava, johnDoe);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setTotalPages(1);
        userPageResponse.setTotalElements(2);
        userPageResponse.setUsers(userListResponses);

        when(userService.findAll(null, null, 0, 20)).thenReturn(userPageResponse);

        mockMvc.perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("user list")))
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.totalElements", is(2)))
                .andExpect(jsonPath("$.data.users[0].id", is(1)))
                .andExpect(jsonPath("$.data.users[0].username", is("tayjava")))
                .andExpect(jsonPath("$.data.users[1].id", is(2)))
                .andExpect(jsonPath("$.data.users[1].username", is("johndoe")));
    }

    @Test
    @WithMockUser(authorities = {"admin", "manager"})
    void shouldGetUserDetail() throws Exception {
        when(userService.findById(anyLong())).thenReturn(tayJava);

        mockMvc.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("user")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.firstName", is("Tay")))
                .andExpect(jsonPath("$.data.lastName", is("Java")))
                .andExpect(jsonPath("$.data.email", is("quoctay87@gmail.com")));
    }

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public UserServiceDetail userServiceDetail() {
            return mock(UserServiceDetail.class);
        }

        @Bean
        public JwtService jwtService() {
            return mock(JwtService.class);
        }
    }
}
