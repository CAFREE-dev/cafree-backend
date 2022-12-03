package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public List<Tag> saveAll(FeedAddRequest feedAddRequest){
        return feedAddRequest.tags().stream()
                .map(this::getSavedTag)
                .collect(Collectors.toList());
    }

    private Tag getSavedTag(String tagName) {
        return findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));

    }

    public List<Tag> findByIds(List<Long> ids){
        return tagRepository.findByIdIn(ids);
    }


    public Tag findById(Long id){
        return tagRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Optional<Tag> findByTagName(String tagName){
        return tagRepository.findByTagName(tagName);
    }

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }
}
