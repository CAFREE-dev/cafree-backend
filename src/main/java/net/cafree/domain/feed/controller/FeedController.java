package net.cafree.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.service.CafeService;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.dto.request.FeedUpdateRequest;
import net.cafree.domain.feed.dto.response.FeedResponse;
import net.cafree.domain.feed.dto.response.SimpleFeedResponse;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.FeedImage;
import net.cafree.domain.feed.entity.FeedTag;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.service.FeedImageService;
import net.cafree.domain.feed.service.FeedService;
import net.cafree.domain.feed.service.FeedTagService;
import net.cafree.domain.feed.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedController {
    private final CafeService cafeService;
    private final FeedService feedService;
    private final FeedImageService feedImageService;
    private final TagService tagService;
    private final FeedTagService feedTagService;

    @GetMapping("/{id}")
    public ResponseEntity<FeedResponse> feedDetails(@PathVariable Long id){
        Feed feed = feedService.findById(id);
        List<FeedImage> feedImages = feedImageService.findByFeedId(id);
        List<Tag> tags = feedTagService.findByFeedId(id).stream()
                .map(FeedTag::getTag).toList();

        return ResponseEntity.ok(feed.toFeedResponse(
                getImageUrls(feedImages),
                getImageSequences(feedImages),
                getTagNames(tags)));
    }

    @GetMapping
    public ResponseEntity<List<SimpleFeedResponse>> feedList() {
        return ResponseEntity.ok(feedImageService.findFirstImage().stream()
                .map(FeedImage::toSimpleFeedResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<FeedResponse> feedAdd(@RequestBody @Validated FeedAddRequest feedAddRequest){
        Feed feed = feedService.save(feedAddRequest, cafeService.findById(feedAddRequest.cafeId()), null);
        List<FeedImage> feedImages = feedImageService.saveAll(feedAddRequest, feed);
        List<Tag> tags = tagService.saveAll(feedAddRequest);

        feedTagService.saveAll(feed, tags);

        return ResponseEntity.ok(feed.toFeedResponse(
                getImageUrls(feedImages),
                getImageSequences(feedImages),
                getTagNames(tags)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedResponse> feedModify(@PathVariable Long id,
                                                   @RequestBody @Validated FeedUpdateRequest feedUpdateRequest) {
        Cafe cafe = cafeService.findById(feedUpdateRequest.cafeId());

        Feed feed = feedService.update(id, feedUpdateRequest, cafe);
        List<FeedImage> feedImages = feedImageService.findByFeedId(id);
        List<Tag> tags = feedTagService.findByFeedId(id).stream()
                .map(FeedTag::getTag).toList();

        return ResponseEntity.ok(feed.toFeedResponse(
                getImageUrls(feedImages),
                getImageSequences(feedImages),
                getTagNames(tags)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> feedRemove(@PathVariable Long id) {
        feedImageService.deleteAll(id);
        tagService.downTaggedCountOrDelete(feedTagService.deleteAll(id));
        feedService.delete(feedService.findById(id));

        return ResponseEntity.ok().build();
    }

    private List<String> getImageUrls(List<FeedImage> feedImages) {
        return feedImages.stream()
                .map(FeedImage::getImageUrl)
                .collect(Collectors.toList());
    }

    private List<Integer> getImageSequences(List<FeedImage> feedImages) {
        return feedImages.stream()
                .map(FeedImage::getSequence)
                .collect(Collectors.toList());
    }

    private List<String> getTagNames(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
    }
}
