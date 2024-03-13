package com.estsoft.blogjpa.dto;

import com.estsoft.blogjpa.domain.Article;
import lombok.*;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse {
    private String title;
    private String content;

    @Builder
    public ArticleResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleResponse(Article article) {
        title = article.getTitle();
        content = article.getContent();
    }
}
