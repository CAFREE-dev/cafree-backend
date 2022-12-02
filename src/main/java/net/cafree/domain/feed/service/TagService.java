package net.cafree.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.feed.dto.request.FeedAddRequest;
import net.cafree.domain.feed.entity.Feed;
import net.cafree.domain.feed.entity.Tag;
import net.cafree.domain.feed.repository.FeedTagRepository;
import net.cafree.domain.feed.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public List<Tag> saveAll(FeedAddRequest feedAddRequest){
        List<Tag> tags = new ArrayList<>();

        for(String tagName : feedAddRequest.tags()){
            Tag tag = findByTagName(tagName);

            if(tag != null){
                tags.add(tag);
            } else {
                tags.add(tagRepository.save(feedAddRequest.toTagEntity(tagName)));
            }
        }

        return tags;
    }

    public List<Tag> findByIds(List<Long> ids){
        return tagRepository.findByIdIn(ids);
    }


    public Tag findById(Long id){
        return tagRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Tag findByTagName(String tagName){
        return tagRepository.findByTagName(tagName);
    }

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }
}
