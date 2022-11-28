package net.cafree.global;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Getter
@Embeddable
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {
    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updateAt;
}
