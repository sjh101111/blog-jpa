package com.estsoft.blogjpa.controller;

import com.estsoft.blogjpa.domain.Article;
import com.estsoft.blogjpa.dto.AddArticleRequest;
import com.estsoft.blogjpa.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BlogControllerUnitTest {
    @InjectMocks
    BlogController blogController;

    @Mock
    BlogService blogService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }

    @DisplayName("블로그 Article 생성")
    @Test
    void testArticle() throws Exception {
        AddArticleRequest request = new AddArticleRequest("title", "content");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        Mockito.doReturn(new Article("title", "content"))
                .when(blogService).save(any(AddArticleRequest.class));

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/articles")
                        .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("title").value(request.getTitle()));
        resultActions.andExpect(jsonPath("content").value(request.getContent()));
    }

//    @DisplayName() 실패할경우 테스트 코드
}