package com.estsoft.blogjpa.domain;

import com.estsoft.blogjpa.dto.CommentAddDto;
import com.estsoft.blogjpa.dto.CommentShowDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;


    @OneToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime created_at;

    public Comment(Article article, String body) {
        this.article = article;
        this.body = body;
    }

    public CommentAddDto toDto() {
        return CommentAddDto.builder().body(body).build();
    }

    public CommentShowDto toShowDto() {
        return CommentShowDto.builder().body(body).id(id).created_at(created_at).build();
    }
}
