package vn.fullStep.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fullStep.controller.response.UserResponse;
import vn.fullStep.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.status = 'ACTIVE' " +
            "AND (" +
            "LOWER(u.firstName) LIKE :keyword OR " +
            "LOWER(u.lastName) LIKE :keyword OR " +
            "LOWER(u.username) LIKE :keyword OR " +
            "LOWER(u.email) LIKE :keyword OR " +
            "LOWER(u.phone) LIKE :keyword" +
            ")")
    Page<User> searchByKeyword(String keyword, Pageable pageable);

}
