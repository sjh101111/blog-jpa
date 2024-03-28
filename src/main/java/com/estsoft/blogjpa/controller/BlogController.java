package com.estsoft.blogjpa.controller;

import com.estsoft.blogjpa.domain.Article;
import com.estsoft.blogjpa.domain.Comment;
import com.estsoft.blogjpa.dto.AddArticleRequest;
import com.estsoft.blogjpa.dto.ArticleResponse;
import com.estsoft.blogjpa.dto.CommentAddDto;
import com.estsoft.blogjpa.dto.CommentShowDto;
import com.estsoft.blogjpa.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "블로그 CRUD")
@RestController
public class BlogController {
    private final BlogService blogService;

    //            = new BlogService();
    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/api/articles")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request) {
        Article article = blogService.save(request);
        ArticleResponse savedResponse = article.toResponse();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(article.toResponse());
    }

    @RequestMapping(value = "/api/articles", method = RequestMethod.GET)
    @Operation(summary = "블로그 전체 목록 보기", description = "블로그 메인 화면에서 보여주는 전체 목록")
    @ApiResponse(responseCode = "100", description = "요청 성공", content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<ArticleResponse>> showArticle() {
        List<Article> articles = blogService.findAll();
        List<ArticleResponse> responseList = articles.stream().map(ArticleResponse::new).toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/api/articles/{id}")
    @Parameter(name = "id", description = "블로그 글 ID", example = "45")
    public ResponseEntity<ArticleResponse> showOneArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok(article.toResponse());
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteOneArticle(@PathVariable Long id) {
        blogService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id,
                                          @RequestBody AddArticleRequest request) {
//        Article article = blogService.update(id, request);
        Article article = blogService.update(id, request);

        return ResponseEntity.ok(article);
    }

    @PostMapping("/comments/{articleId}")
    public ResponseEntity<CommentAddDto> addComment(@PathVariable Long articleId,
                                                    @RequestBody Comment comment) {
        Comment comments = blogService.addCommentToArticle(articleId, comment.getBody());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comments.toDto());
    }

    @GetMapping("/comments/{articleId}/{commentId}")
    public ResponseEntity<CommentShowDto> showComment(@PathVariable Long articleId,
                                                      @PathVariable Long commentId) {
        Comment comment = blogService.showComment(commentId);
        return ResponseEntity.ok(comment.toShowDto());
    }

}
