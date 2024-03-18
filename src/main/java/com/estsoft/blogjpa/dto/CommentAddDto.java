package com.estsoft.blogjpa.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentAddDto {
    private String body;

    @Builder
    public CommentAddDto(String body) {
        this.body = body;
    }
}
