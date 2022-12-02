package net.cafree.domain.feed.repository;

import net.cafree.domain.feed.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    /* 2022.11.30 lcomment : Tag 이름으로 존재하는 Tag 엔티티 반환 */
    Tag findByTagName(String tagName);

    /* 2022.12.01 lcommet :  IN 쿼리 */
    List<Tag> findByIdIn(List<Long> ids);
}
