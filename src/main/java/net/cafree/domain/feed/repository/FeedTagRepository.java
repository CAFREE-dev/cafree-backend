package net.cafree.domain.feed.repository;

import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedTagRepository extends JpaRepository<FeedTag, Long> {

    /* 2022.12.01 lcomment : 해당 피드의 태그 id 리스트 가져오기 */
    List<FeedTag> findByFeedId(Long feedId);

    /* 2022.12.01 lcomment : 피드로 Tag 리스트 가져오기 */
    List<FeedTag> findByFeed(Feed feed);
}
