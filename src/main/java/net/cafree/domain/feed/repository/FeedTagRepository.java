package net.cafree.domain.feed.repository;

import net.cafree.domain.feed.entity.FeedTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedTagRepository extends JpaRepository<FeedTag, Long> {
}
