package net.cafree.domain.cafe.dao;

import net.cafree.domain.cafe.domain.CafeAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeAddressRepository extends JpaRepository<CafeAddress, Long> {
}
