package net.cafree.global.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class AuditEntity {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@LastModifiedDate
	private LocalDateTime updatedAt;
}