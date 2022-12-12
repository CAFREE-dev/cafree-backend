package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import net.cafree.domain.feed.repository.FeedImageRepository;
import net.cafree.s3.util.ImageS3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedImageService {
    private final FeedImageRepository feedImageRepository;
    private final ImageS3Uploader imageS3Uploader;

    @Transactional
    public List<FeedImage> saveAll(FeedAddRequest feedAddRequest, Feed feed) throws IOException {
        return feedImageRepository.saveAll(feedAddRequest.toFeedImageEntity(
                feed,
                imageS3Uploader.upload(feedAddRequest.images(), feed.getId())));
    }

    public FeedImage findById(Long id) {
        return feedImageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<FeedImage> findFirstImage() {
        return feedImageRepository.findBySequence(1);
    }

    public List<FeedImage> findByFeed(Feed feed) {
        return feedImageRepository.findByFeed(feed);
    }

    public List<FeedImage> findByFeedId(Long feedId) {
        return feedImageRepository.findByFeedId(feedId);
    }

    @Transactional
    public void deleteAll(Long feedId) {
        imageS3Uploader.deleteFromS3(findByFeedId(feedId).stream()
                .map(FeedImage::getImageUrl)
                .collect(Collectors.toList()));
        feedImageRepository.deleteAll(findByFeedId(feedId));
    }
}
