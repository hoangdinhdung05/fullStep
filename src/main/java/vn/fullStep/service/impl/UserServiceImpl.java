package vn.fullStep.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.fullStep.common.UserStatus;
import vn.fullStep.controller.request.UserCreationRequest;
import vn.fullStep.controller.request.UserPasswordRequest;
import vn.fullStep.controller.request.UserUpdateRequest;
import vn.fullStep.controller.response.UserPageResponse;
import vn.fullStep.controller.response.UserResponse;
import vn.fullStep.entity.Address;
import vn.fullStep.entity.User;
import vn.fullStep.exception.ResourceNotFoundException;
import vn.fullStep.repository.AddressRepository;
import vn.fullStep.repository.UserRepository;
import vn.fullStep.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse findAll(String keyWord, String sort, int page, int size) {

        //Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if(StringUtils.hasLength(sort)) {
            //call search method in repository
            Pattern pattern = Pattern.compile("(\\w+)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()) {
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        //Page number starts from 0, so if page is 1, we need to convert it to 0
        int pageNo = 0;
        if(page > 0) {
            pageNo = page - 1; //convert to zero-based index
        }

        //Pageable object
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<User> entityPage;

        if(StringUtils.hasLength(keyWord)) {
            keyWord = "%" + keyWord.toLowerCase() + "%";
            entityPage = this.userRepository.searchByKeyword(keyWord, pageable);
        } else {
            entityPage = this.userRepository.findAll(pageable);
        }

        //return page no, page size, total elements, total pages
        //list of UserResponse
        return getUserPageResponse(page, size, entityPage);
    }

    @Override
    public UserResponse findById(Long userId) {
        log.info("Finding user by ID: {}", userId);

        User user = getUserById(userId);

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserCreationRequest request) {

        log.info("Saving user with username: {}", request.getUsername());
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        user.setType(request.getType());
        user.setStatus(UserStatus.NONE);
        this.userRepository.save(user);

        if(user.getId() != null) {
            log.info("User saved with ID: {}", user.getId());
            List<Address> addresses = new ArrayList<>();
            request.getAddresses().forEach(address -> {
                Address addressEntity = new Address();
                addressEntity.setApartmentNumber(address.getApartmentNumber());
                addressEntity.setFloor(address.getFloor());
                addressEntity.setBuilding(address.getBuilding());
                addressEntity.setStreetNumber(address.getStreetNumber());
                addressEntity.setStreet(address.getStreet());
                addressEntity.setCity(address.getCity());
                addressEntity.setCountry(address.getCountry());
                addressEntity.setAddressType(address.getAddressType());
                addressEntity.setUserId(user.getId());
                addresses.add(addressEntity);
            });
            this.addressRepository.saveAll(addresses);
            log.info("Saved {} addresses for user with ID: {}", addresses.size(), user.getId());
        }

        return user.getId() != null ? user.getId() : -1L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest request) {
        log.info("Updating user with ID: {}", request.getId());
        //get user by ID
        User user = getUserById(request.getId());

        //set data to user
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        this.userRepository.save(user);

        log.info("User with ID: {} updated successfully", user.getId());

        //save user addresses
        List<Address> addresses = new ArrayList<>();

        request.getAddresses().forEach(address -> {
            //check if address exists for user
            Address addressEntity = this.addressRepository.findByUserIdAndAddressType(user.getId(), address.getAddressType());

            if(addressEntity == null) {
                addressEntity = new Address();
            }

            addressEntity.setApartmentNumber(address.getApartmentNumber());
            addressEntity.setFloor(address.getFloor());
            addressEntity.setBuilding(address.getBuilding());
            addressEntity.setStreetNumber(address.getStreetNumber());
            addressEntity.setStreet(address.getStreet());
            addressEntity.setCity(address.getCity());
            addressEntity.setCountry(address.getCountry());
            addressEntity.setAddressType(address.getAddressType());
            addressEntity.setUserId(user.getId());

            addresses.add(addressEntity);
        });

        //save user to database
        this.addressRepository.saveAll(addresses);
        log.info("Updated {} addresses for user with ID: {}", addresses.size(), user.getId());
    }

    @Override
    public void delete(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        //get user by ID
        User user = getUserById(userId);
        //set user status to INACTIVE
        user.setStatus(UserStatus.INACTIVE);
        log.info("User with ID: {} deleted successfully", user.getId());
        //save user to database
        this.userRepository.save(user);
        log.info("User with ID: {} status changed to INACTIVE", user.getId());
    }

    @Override
    public void changePassword(UserPasswordRequest request) {
        log.info("Changing password for user with ID: {}", request);
        //get user by ID
        User user = getUserById(request.getId());

        if(request.getPassword().equals(request.getConfirmPassword())) {
            user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        }

        log.info("Password changed successfully for user with ID: {}", user.getId());
        //save user to database
        this.userRepository.save(user);

    }

    /**
     * Helper method to get user by ID
     *
     * @param userId the ID of the user
     * @return User object
     */
    private User getUserById(Long userId) {

        return this.userRepository.findById(userId).orElseThrow(() -> {
//            log.error("User with ID {} not found", userId);
            return new ResourceNotFoundException("User not found");
        });
    }

    /**
     * Helper method to get UserPageResponse from Page<User>
     *
     * @param page the current page number
     * @param size the size of the page
     * @return UserPageResponse object
     */
    private static UserPageResponse getUserPageResponse(int page, int size, Page<User> users) {
        List<UserResponse> userList = users.stream()
                .map(user -> {
                    return UserResponse.builder()
                            .id(user.getId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .gender(user.getGender())
                            .birthday(user.getBirthday())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .phone(user.getPhone())
                            .build();
                })
                .toList();

        UserPageResponse response = new UserPageResponse();
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setUsers(userList);
        return response;
    }
}
