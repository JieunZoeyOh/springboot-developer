package me.zoey.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.zoey.springbootdeveloper.domain.Article;
import me.zoey.springbootdeveloper.domain.User;
import me.zoey.springbootdeveloper.dto.AddArticleRequest;
import me.zoey.springbootdeveloper.dto.UpdateArticleRequest;
import me.zoey.springbootdeveloper.repository.BlogRepository;
import me.zoey.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = User.builder().email("user@gmail.com").password("test").build();
        userRepository.save(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest request = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(request);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody)
        );

        //then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticle: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticle() throws Exception {
        //given
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();

        //when
        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        //when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        //when
        mockMvc.perform(delete(url, savedArticle.getId())).andExpect(status().isOk());

        //then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        Long savedId = savedArticle.getId();

        final String newTitle = "new title";
        final String newContent = "new content";

        final UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
        final String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions result = mockMvc.perform(
                put(url, savedId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        );

        //then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedId).orElseThrow();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    private Article createDefaultArticle() {
        Article article = Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build();
        blogRepository.save(article);
        return article;
    }

}
