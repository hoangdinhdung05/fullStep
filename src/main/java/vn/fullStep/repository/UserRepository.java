package vn.fullStep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fullStep.controller.response.UserResponse;
import vn.fullStep.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);
}
