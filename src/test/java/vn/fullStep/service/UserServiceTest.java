package vn.fullStep.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.fullStep.common.Gender;
import vn.fullStep.common.UserStatus;
import vn.fullStep.common.UserType;
import vn.fullStep.controller.request.AddressRequest;
import vn.fullStep.controller.request.UserCreationRequest;
import vn.fullStep.controller.request.UserPasswordRequest;
import vn.fullStep.controller.request.UserUpdateRequest;
import vn.fullStep.controller.response.UserPageResponse;
import vn.fullStep.controller.response.UserResponse;
import vn.fullStep.entity.UserEntity;
import vn.fullStep.exception.ResourceNotFoundException;
import vn.fullStep.repository.AddressRepository;
import vn.fullStep.repository.UserRepository;
import vn.fullStep.service.impl.UserServiceImpl;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    private static UserEntity hoangdung;
    private static UserEntity test;
    private static UserEntity tayJava;
    private static UserEntity johnDoe;

    @BeforeAll
    static void beforeAll() {
        hoangdung = new UserEntity();
        hoangdung.setId(1L);
        hoangdung.setFirstName("Hoang");
        hoangdung.setLastName("Dung");
        hoangdung.setGender(Gender.MALE);
        hoangdung.setBirthday(new Date());
        hoangdung.setEmail("hoangdinhdung0205@gmail.com");
        hoangdung.setUsername("hoangdung");
        hoangdung.setPassword("123456789");
        hoangdung.setType(UserType.USER);
        hoangdung.setStatus(UserStatus.ACTIVE);

        test = new UserEntity();
        test.setId(2L);
        test.setFirstName("Test");
        test.setLastName("User");
        test.setGender(Gender.MALE);
        test.setBirthday(new Date());
        test.setEmail("testuser@gmail.com");
        test.setUsername("testuser");
        test.setPassword("987654321");
        test.setType(UserType.USER);
        test.setStatus(UserStatus.ACTIVE);

        tayJava = new UserEntity();
        tayJava.setId(3L);
        tayJava.setFirstName("Tay");
        tayJava.setLastName("Java");
        tayJava.setGender(Gender.MALE);
        tayJava.setBirthday(new Date());
        tayJava.setEmail("quoctay87@gmail.com");
        tayJava.setPhone("0975118228");
        tayJava.setUsername("tayjava");
        tayJava.setPassword("123456789");
        tayJava.setType(UserType.USER);
        tayJava.setStatus(UserStatus.ACTIVE);

        johnDoe = new UserEntity();
        johnDoe.setId(4L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setGender(Gender.MALE);
        johnDoe.setBirthday(new Date());
        johnDoe.setEmail("john.doe@gmail.com");
        johnDoe.setPhone("0123456789");
        johnDoe.setUsername("johndoe");
        johnDoe.setPassword("password");
        johnDoe.setType(UserType.USER);
        johnDoe.setStatus(UserStatus.ACTIVE);
    }

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @Test
    void testGetListUsers_Success() {
        Page<UserEntity> userPage = new PageImpl<>(Arrays.asList(hoangdung, test));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        UserPageResponse result = userService.findAll(null, null, 0, 10);
        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
    }

    @Test
    void testSearchUsers_Success() {
        Page<UserEntity> userPage = new PageImpl<>(Collections.singletonList(hoangdung));
        when(userRepository.searchByKeyword(any(String.class), any(Pageable.class))).thenReturn(userPage);
        UserPageResponse result = userService.findAll("hoang", null, 0, 10);
        assertNotNull(result);
        assertEquals(1, result.getUsers().size());
    }

    @Test
    void testGetUserList_Empty() {
        Page<UserEntity> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        UserPageResponse result = userService.findAll(null, null, 0, 20);
        assertNotNull(result);
        assertEquals(0, result.getUsers().size());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(tayJava));
        UserResponse result = userService.findById(3L);
        assertNotNull(result);
        assertEquals("tayjava", result.getUsername());
    }

    @Test
    void testGetUserById_Failure() {
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(999L));
    }

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByUsername("tayjava")).thenReturn(tayJava);
        UserResponse result = userService.findByUsername("tayjava");
        assertNotNull(result);
        assertEquals("tayjava", result.getUsername());
    }

    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail("quoctay87@gmail.com")).thenReturn(tayJava);
        UserResponse result = userService.findByEmail("quoctay87@gmail.com");
        assertNotNull(result);
        assertEquals("quoctay87@gmail.com", result.getEmail());
    }

    @Test
    void testSave_Success() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(tayJava);
        UserCreationRequest request = new UserCreationRequest();
        request.setFirstName("Tay");
        request.setLastName("Java");
        request.setGender(Gender.MALE);
        request.setBirthday(new Date());
        request.setEmail("quoctay87@gmail.com");
        request.setPhone("0975118228");
        request.setUsername("tayjava");
        AddressRequest address = new AddressRequest();
        address.setCity("City");
        address.setCountry("Country");
        request.setAddresses(List.of(address));
        long result = userService.save(request);
        assertEquals(3L, result);
    }

    @Test
    void testUpdate_Success() {
        Long userId = 4L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(johnDoe));
        when(userRepository.save(any(UserEntity.class))).thenReturn(johnDoe);
        UserUpdateRequest request = new UserUpdateRequest();
        request.setId(userId);
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setGender(Gender.FEMALE);
        request.setBirthday(new Date());
        request.setEmail("janesmith@gmail.com");
        request.setPhone("0123456789");
        request.setUsername("janesmith");
        AddressRequest address = new AddressRequest();
        address.setCity("City");
        address.setCountry("Country");
        request.setAddresses(List.of(address));
        userService.update(request);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testChangePassword_Success() {
        Long userId = 4L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(johnDoe));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        UserPasswordRequest request = new UserPasswordRequest();
        request.setId(userId);
        request.setPassword("newPassword");
        request.setConfirmPassword("newPassword");
        userService.changePassword(request);
        assertEquals("encodedNewPassword", johnDoe.getPassword());
        verify(userRepository).save(johnDoe);
    }

    @Test
    void testDeleteUser_Success() {
        Long userId = 3L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(tayJava));
        userService.delete(userId);
        assertEquals(UserStatus.INACTIVE, tayJava.getStatus());
        verify(userRepository).save(tayJava);
    }

    @Test
    void testUserNotFound_ThrowsException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.delete(userId));
        verify(userRepository, never()).save(any());
    }
}