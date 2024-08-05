package me.shinsunyoung.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor// final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service //빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;

    //블로그 글 추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    //블로그 글 목목 조회(전체)
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    //블로그 글 조회(하나)
    public  Article findById(Long id){
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));
    }

    //블로그 글 삭제
    public void delete(long id){
        blogRepository.deleteById(id);
    }

    //블로글 수정
    @Transactional //트랜잭션 메서드 , 데이터베이스의 데이터를 바꾸기 위해 두작업을 하나의단위로 묶음작업
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " +id));
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    }


