package com.estsoft.blogjpa.service;

import com.estsoft.blogjpa.domain.Article;
import com.estsoft.blogjpa.domain.Comment;
import com.estsoft.blogjpa.dto.AddArticleRequest;
import com.estsoft.blogjpa.external.ExampleAPIClient;
import com.estsoft.blogjpa.repository.BlogRepository;
import com.estsoft.blogjpa.repository.CommentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
//@NoArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final ExampleAPIClient apiClient;

    private final CommentRepository commentRepository;
    @Autowired
    public BlogService(BlogRepository blogRepository, ExampleAPIClient apiClient, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.apiClient = apiClient;
        this.commentRepository = commentRepository;
    }
    public Article save(AddArticleRequest request) {

        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
          blogRepository.findById(id).orElseThrow(
                  () -> new IllegalArgumentException("not found id" + id));
        return blogRepository.findById(id).orElse(new Article());
    }

    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, AddArticleRequest request) {
        Article article = findById(id);
        article.update(request.getTitle(),request.getContent());
        return article;
    }
    @Transactional
    public Article updateTitle(Long id, AddArticleRequest request) {
        Article article = findById(id);
        blogRepository.updateTitle(id, request.getTitle());
        return article;
    }

    public List<Article> saveBulkArticles() {
        List<Article> articles = parseArticles();
        return blogRepository.saveAll(articles);
    }
    private List<Article> parseArticles() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        String responseJson = apiClient.fetchDataFromApi(url);

        ObjectMapper objectMapper = new ObjectMapper();
        List<LinkedHashMap<String, String>> jsonMapList = new ArrayList<>();

        try {
            jsonMapList = objectMapper.readValue(responseJson, List.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json", e.getMessage());
        }

        return jsonMapList.stream()
                .map(hashMap -> new Article(hashMap.get("title"), hashMap.get("body")))
                .toList();
    }

    public Comment addCommentToArticle(Long articleId, String commentBody) {
        Article article = blogRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + articleId));

        Comment newComment = new Comment(article, commentBody);
        return commentRepository.save(newComment);
    }

    public Comment showComment(Long id){
        return commentRepository.findById(id).orElse(new Comment());
    }
}
