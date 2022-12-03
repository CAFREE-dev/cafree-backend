package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.dto.request.FeedUpdateRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.repository.FeedRepository;
import net.cafree.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;

    @Transactional
    public Feed save(FeedAddRequest feedAddRequest, Cafe cafe, Member member) {
        return feedRepository.save(feedAddRequest.toFeedEntity(cafe, member));
    }

    @Transactional
    public Feed update(Long id, FeedUpdateRequest feedUpdateRequest, Cafe cafe){
        Feed feed = findById(id);
        feed.updateFeed(
                feedUpdateRequest.contents(),
                feedUpdateRequest.rating(),
                cafe);
        return feed;
    }

    @Transactional
    public void delete(Long id) {
        feedRepository.delete(findById(id));
    }

    public Feed findById(Long id) {
        return feedRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Feed> findAll() {
        return feedRepository.findAll();
    }
}
