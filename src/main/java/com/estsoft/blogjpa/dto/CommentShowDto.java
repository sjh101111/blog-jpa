package com.estsoft.blogjpa.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentShowDto {
    private Long id;
    private String body;
    private LocalDateTime created_at;
}
