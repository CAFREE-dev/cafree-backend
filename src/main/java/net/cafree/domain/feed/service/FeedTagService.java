package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.feed.dto.request.FeedUpdateRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedTag;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.repository.FeedTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedTagService {
    private final FeedTagRepository feedTagRepository;

    @Transactional
    public List<FeedTag> saveAll(Feed feed, List<Tag> tags) {
        List<FeedTag> feedTags = new ArrayList<>();

        for(Tag tag : tags){
            feedTags.add(new FeedTag(tag, feed));
        }

        return feedTagRepository.saveAll(feedTags);
    }

    public List<FeedTag> findByFeed(Feed feed){
        return feedTagRepository.findByFeed(feed);
    }

    public List<FeedTag> findByFeedId(Long feedId){
        return feedTagRepository.findByFeed_Id(feedId);
    }

    public FeedTag findById(Long id) {
        return feedTagRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteAll(Long id) {
        feedTagRepository.deleteAll(findByFeedId(id));
    }
}
