package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedTag;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.repository.FeedTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedTagService {
    private final FeedTagRepository feedTagRepository;

    @Transactional
    public List<FeedTag> saveAll(Feed feed, List<Tag> tags) {
        return feedTagRepository.saveAll(tags.stream()
                .map(tag -> new FeedTag(tag, feed))
                .collect(Collectors.toList()));
    }

    public List<FeedTag> findByFeed(Feed feed){
        return feedTagRepository.findByFeed(feed);
    }

    public List<FeedTag> findByFeedId(Long feedId){
        return feedTagRepository.findByFeedId(feedId);
    }

    public FeedTag findById(Long id) {
        return feedTagRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteAll(Long id) {
        feedTagRepository.deleteAll(findByFeedId(id));
    }
}
