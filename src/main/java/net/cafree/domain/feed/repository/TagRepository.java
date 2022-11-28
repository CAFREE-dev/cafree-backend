package net.cafree.domain.feed.repository;

import net.cafree.domain.feed.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
