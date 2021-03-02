package com.junho.book.springboot.web.domain.posts;


import com.junho.book.springboot.domain.posts.Posts;
import com.junho.book.springboot.domain.posts.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest // 별다른 설정 없이 사용할 경우 H2 DB
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @AfterEach // @After -> @AfterEach
    public void cleanup() {
        postsRepository.deleteAll(); // JUnit의 단위테스트가 끝날 때마다 수행되는 메소드 지정하였고,
    }

    @Test
    public void testRead() {
        //give
        String title = "테스트 게시글";
        String content = "테스트 본문";
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("a@naver.com")
                .build()
        );

        //when
        List<Posts> postsList = postsRepository.findAll(); //3

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

    }

}


