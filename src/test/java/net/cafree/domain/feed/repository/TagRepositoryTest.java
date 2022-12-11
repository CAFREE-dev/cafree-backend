package net.cafree.domain.feed.repository;

import net.cafree.domain.feed.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @AfterEach
    public void cleanup(){
        tagRepository.deleteAll();
    }

    @Test
    @DisplayName("피드 태그 등록을 요청하면 새로운 피드 태그가 등록된다")
    public void registerNewTag(){
        // given
        String tagName = "태그입니다.";
        Tag tag = tagRepository.save(new Tag(tagName, 0));

        // then
        assertThat(tag.getTagName()).isEqualTo(tagName);
    }

    @Test
    @DisplayName("모든 태그를 반환한다")
    public void getAllTag() {
        // given
        String tagName = "태그입니다.";
        tagRepository.save(new Tag(tagName, 0));
        tagRepository.save(new Tag(tagName, 0));

        // when
        List<Tag> tags = tagRepository.findAll();

        // then
        assertThat(tags).hasSize(2);
    }
}
