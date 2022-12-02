package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import net.cafree.domain.feed.repository.FeedImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedImageService {
    private final FeedImageRepository feedImageRepository;

    @Transactional
    public List<FeedImage> saveAll(FeedAddRequest feedAddRequest, Feed feed){
        return feedImageRepository.saveAll(feedAddRequest.toFeedImageEntity(feed));
    }

    public FeedImage findById(Long id){
        return feedImageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<FeedImage> findFirstImage() {
        return feedImageRepository.findBySequence(1);
    }

    public List<FeedImage> findByFeed(Feed feed){
        return feedImageRepository.findByFeed(feed);
    }

    public List<FeedImage> findByFeedId(Long feedId){
        return feedImageRepository.findByFeed_Id(feedId);
    }

    @Transactional
    public void deleteAll(Long id) {
        feedImageRepository.deleteAll(findByFeedId(id));
    }
}
