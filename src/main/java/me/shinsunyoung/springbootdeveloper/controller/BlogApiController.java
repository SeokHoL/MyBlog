package me.shinsunyoung.springbootdeveloper.controller;


import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.ArticleResponse;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController//HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final BlogService blogService;

    //HTTP 메서드가 POST일때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    //@RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);
        //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){

        List<ArticleResponse> articles = blogService.findAll()
                //blogService.findAll()은 List<Article>을 반환합니다.
                //이 List<Article>을 stream() 메서드를 사용하여 스트림으로 변환합니다.
                //스트림의 map 메서드를 사용하여 각 Article 객체를 ArticleResponse 객체로 변환합니다.
                //ArticleResponse::new는 각 Article 객체를 ArticleResponse 생성자를 사용하여 변환합니다.
                //최종적으로 변환된 List<ArticleResponse>를 toList()를 통해 리스트로 반환합니다.
                .stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    //URL 경로에서 값 추출
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id){
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                //조회한 Article 객체를 ArticleResponse 객체로 변환하여
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    //엔티티 타입으로 반환해도 문제는 없지만, 보안, API의 유연성, 클라이언트의 요구 사항 등을 고려할 때 DTO로 변환하여 반환하는 것이 더 좋을 수 있습니다.
    // 특히, 프로젝트가 커지거나 복잡해질수록 DTO의 장점이 더 부각됩니다.
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request){
        Article updatedArticle = blogService.update(id,request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }


}
