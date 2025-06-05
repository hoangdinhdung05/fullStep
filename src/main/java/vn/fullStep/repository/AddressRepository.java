package vn.fullStep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fullStep.entity.AddressEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    // Additional query methods can be defined here if needed
    AddressEntity findByUserIdAndAddressType(Long userId, Integer addressType);
}
