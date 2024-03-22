package com.estsoft.blogjpa.controller;

import com.estsoft.blogjpa.domain.Article;
import com.estsoft.blogjpa.dto.AddArticleRequest;
import com.estsoft.blogjpa.repository.BlogRepository;
import com.estsoft.blogjpa.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @Test
    void addArticle() throws Exception {
        //given : 저장하고 싶은 블로그 정보
        AddArticleRequest articleRequest = new AddArticleRequest("제목", "내용");
        String json = objectMapper.writeValueAsString(articleRequest);
        // when : POST /api/articles
        ResultActions resultActions = mockMvc.perform(post("/api/articles").
                content(json).contentType(MediaType.APPLICATION_JSON));

        // then : 저장이 잘 되었는지 확인, HttpStatusCode 201 검증
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(articleRequest.getTitle()))
                .andExpect(jsonPath("content").value(articleRequest.getContent()))
        ;
        //저장이 잘 되었는지 확인

    }

    @Test
    void showArticle() throws Exception {
        // given : blogRepository.save
        List<Article> articleList = new ArrayList<>();
        Article article1 = new Article("title1", "content1");
        Article article2 = new Article("title2", "content2");
        articleList.add(article1);
        articleList.add(article2);
        blogRepository.saveAll(articleList);

        // when : GET /api/articles
        ResultActions resultActions = mockMvc.perform(get("/api/articles"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(articleList.get(0).getTitle()))
                .andExpect(jsonPath("$[0].content").value(articleList.get(0).getContent()))
                .andExpect(jsonPath("$[1].title").value(articleList.get(1).getTitle()))
                .andExpect(jsonPath("$[1].content").value(articleList.get(1).getContent()))

        ;
    }

    @Test
    void deleteOneArticle() throws Exception {
        // given : 삭제할 대상 데이터 save
        Article article = blogRepository.save(new Article("title", "content"));
        Long id = article.getId();

        // when: DELETE /api/articles/{id}
        ResultActions resultActions = mockMvc.perform(delete("/api/articles/{id}", id));

        // then : 삭제 결과 확인
        resultActions.andExpect(status().isOk());

        //삭제결과
        Optional<Article> byid = blogRepository.findById(id);
        Assertions.assertFalse(byid.isPresent());
    }

    @Test
    void update() throws Exception {
        // given : 저장할 데이터
        Article article = blogRepository.save(new Article("제목", "내용"));
        Long id = article.getId();

        AddArticleRequest articleRequest = new AddArticleRequest("a", "a");
        // when : UPDATE /api/articles/{id}
        ResultActions resultActions = mockMvc.perform(put("/api/articles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleRequest)));

        Article updateTest = blogRepository.findById(id).orElseThrow();
        //수정 후 다시 조회
        // then : 삭제 결과 확인
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("title").value(updateTest.getTitle()))
                .andExpect(jsonPath("content").value(updateTest.getContent()))
        ;
        // 삭제 결과
//        Article updateTest = blogRepository.findById(id).orElseThrow();
        assertThat(updateTest.getTitle()).isEqualTo("a");
        assertThat(updateTest.getContent()).isEqualTo("a");
    }

    @Test
    public void updateTest() {
        //업데잍 테스트 코드 추가
    }
}