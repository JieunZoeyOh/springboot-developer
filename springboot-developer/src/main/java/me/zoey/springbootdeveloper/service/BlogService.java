package me.zoey.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.zoey.springbootdeveloper.domain.Article;
import me.zoey.springbootdeveloper.dto.AddArticleRequest;
import me.zoey.springbootdeveloper.dto.UpdateArticleRequest;
import me.zoey.springbootdeveloper.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;

    /**
     * 블로그 글 추가 메서드
     */
    public Long save(AddArticleRequest request, String userName) {
        Article article = request.toEntity(userName);
        blogRepository.save(article);
        return article.getId();
    }

    /**
     * 모든 블로그 글 조회
     */
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    /**
     * 하나의 블로그 글 조회
     */
    public Article findById(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    /**
     * 블로그 글 삭제
     */
    @Transactional
    public void delete(Long id) {
        Article article = this.findById(id);
        authorizeArticleAuthor(article);
        blogRepository.deleteById(id);
    }

    /**
     * 블로그 글 수정
     */
    @Transactional
    public void update(Long id, UpdateArticleRequest request) {
        Article article = this.findById(id);
        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
    }

    /**
     * 게시글을 작성한 유저인지 확인
     */
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
