package net.cafree.domain.feed.repository;

import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

    /* 2022.12.01 lcomment : 해당 피드의 이미지 리스트 가져오기 */
    List<FeedImage> findByFeed(Feed feed);

    /* 2022.12.01 lcomment : 해당 피드의 이미지 리스트 가져오기 */
    List<FeedImage> findByFeed_Id(Long feedId);

    /* 2022.12.01 lcomment : sequence에 해당하는 이미지 리스트 반환 */
    List<FeedImage> findBySequence(Integer sequence);
}
