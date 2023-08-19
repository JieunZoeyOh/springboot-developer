package me.zoey.springbootdeveloper.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zoey.springbootdeveloper.domain.Article;
import me.zoey.springbootdeveloper.dto.AddArticleRequest;
import me.zoey.springbootdeveloper.dto.ArticleResponse;
import me.zoey.springbootdeveloper.dto.UpdateArticleRequest;
import me.zoey.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request,
                                                      Principal principal) {
        Long savedId = blogService.save(request, principal.getName());

        Article savedArticle = blogService.findById(savedId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(savedArticle));
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @RequestBody UpdateArticleRequest request) {
        blogService.update(id, request);
        Article updatedArticle = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(updatedArticle));
    }

}
