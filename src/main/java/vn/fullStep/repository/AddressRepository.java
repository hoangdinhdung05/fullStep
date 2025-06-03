package vn.fullStep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fullStep.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Additional query methods can be defined here if needed
    Address findByUserIdAndAddressType(Long userId, Integer addressType);
}
