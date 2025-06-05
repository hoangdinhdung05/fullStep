package vn.fullStep.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fullStep.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.status = 'ACTIVE' " +
            "AND (" +
            "LOWER(u.firstName) LIKE :keyword OR " +
            "LOWER(u.lastName) LIKE :keyword OR " +
            "LOWER(u.username) LIKE :keyword OR " +
            "LOWER(u.email) LIKE :keyword OR " +
            "LOWER(u.phone) LIKE :keyword" +
            ")")
    Page<UserEntity> searchByKeyword(String keyword, Pageable pageable);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

}
