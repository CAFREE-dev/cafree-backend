package net.cafree.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.cafe.dto.request.CafeRegisterRequest;
import net.cafree.domain.cafe.dto.request.CafeUpdateRequest;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.entity.CafeAddress;
import net.cafree.domain.cafe.repository.CafeAddressRepository;
import net.cafree.domain.cafe.repository.CafeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final CafeAddressRepository cafeAddressRepository;

    @Transactional
    public Cafe save(CafeRegisterRequest cafeRegisterRequest) {
        return cafeRepository.save(cafeRegisterRequest.toCafeEntity(
                cafeAddressRepository.save(cafeRegisterRequest.toCafeAddressEntity())));
    }

    public Cafe findById(Long id) {
        return cafeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    @Transactional
    public Cafe update(Long id, CafeUpdateRequest cafeUpdateRequest) {
        Cafe cafe = updateCafe(id, cafeUpdateRequest);
        updateCafeAddress(cafe.getCafeAddress(), cafeUpdateRequest);
        return cafe;
    }

    private Cafe updateCafe(Long id, CafeUpdateRequest cafeUpdateRequest) {
        Cafe cafe = findById(id);
        cafe.updateCafe(
                cafeUpdateRequest.title(),
                cafeUpdateRequest.likeCount(),
                cafeUpdateRequest.mapUrl());
        return cafe;
    }

    private void updateCafeAddress(CafeAddress cafeAddress, CafeUpdateRequest cafeUpdateRequest) {
        cafeAddress.updateCafeAddress(
                cafeUpdateRequest.sido(),
                cafeUpdateRequest.sigungu(),
                cafeUpdateRequest.eupmyun(),
                cafeUpdateRequest.dong(),
                cafeUpdateRequest.doro(),
                cafeUpdateRequest.buildNo(),
                cafeUpdateRequest.branch(),
                cafeUpdateRequest.latitude(),
                cafeUpdateRequest.longitude());
    }

    @Transactional
    public void delete(Long id) {
        cafeRepository.delete(findById(id));
    }
}
