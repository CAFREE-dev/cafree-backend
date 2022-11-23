package net.cafree.domain.cafe.repository;

import net.cafree.domain.cafe.entity.CafeAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeAddressRepository extends JpaRepository<CafeAddress, Long> {
}
