package net.cafree.domain.cafe.repository;

import net.cafree.domain.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    /* 2022.11.16 - lcomment : 피드 카페 입력 과정에서 검색어에 맞는 카페를 보여주기 위해 추가 */
    List<Cafe> findByTitleContains(String title);
}
