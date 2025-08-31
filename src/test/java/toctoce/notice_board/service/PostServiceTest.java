package toctoce.notice_board.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class PostServiceTest {

    @Autowired EntityManager em;
    @Autowired PostService postService;

    @Test
    public void 게시글_생성() {
        // given
        // when
        long postId = createPost();

        // then
        Post post = postService.findOne(postId);
        assertNotNull(post);
        Assertions.assertThat(post.getAuthor()).isEqualTo("author1");
        Assertions.assertThat(post.getTitle()).isEqualTo("title1");
        Assertions.assertThat(post.getContent()).isEqualTo("content1");
        Assertions.assertThat(post.getPassword()).isEqualTo("password1");

    }

    @Test
    public void 게시글_삭제_성공() {
        // given
        long postId = createPost();

        // when
        String password = "password1";
        postService.deletePost(postId, password);

        // then
        Post post = postService.findOne(postId);
        assertNotNull(post.getDeletedAt());
        assertNotNull(post);

        List<Post> posts = postService.findPosts();
        Assertions.assertThat(posts.size()).isEqualTo(0);
    }

    @Test
    public void 게시글_삭제_실패() {
        // given
        long postId = createPost();

        // when
        // then
        String password = "wrong password";
        assertThrows(IllegalArgumentException.class,
                () -> postService.deletePost(postId, password));

        Post post = postService.findOne(postId);
        assertNull(post.getDeletedAt());
        assertNotNull(post);

        List<Post> posts = postService.findPosts();
        Assertions.assertThat(posts.size()).isEqualTo(1);
    }

    @Test
    public void 게시글_수정_성공() {
        // given
        long postId = createPost();

        // when
        PostUpdateRequestDto dto = new PostUpdateRequestDto();
        dto.setContent("content2");
        dto.setTitle("title2");
        dto.setPassword("password1");
        postService.updatePost(postId, dto);

        // then
        Post post = postService.findOne(postId);
        assertEquals("content2", post.getContent());
        assertEquals("title2", post.getTitle());
        assertEquals("password1", post.getPassword());
        assertNotNull(post.getUpdatedAt());
    }

    @Test
    public void 게시글_수정_실패() {
        // given
        long postId = createPost();

        // when
        PostUpdateRequestDto dto = new PostUpdateRequestDto();
        dto.setContent("content2");
        dto.setTitle("title2");
        dto.setPassword("password2");
        assertThrows(IllegalArgumentException.class,
                () -> postService.updatePost(postId, dto));

        // then
        Post post = postService.findOne(postId);
        assertEquals("content1", post.getContent());
        assertEquals("title1", post.getTitle());
        assertEquals("password1", post.getPassword());
        assertNull(post.getUpdatedAt());
    }

    private long createPost() {
        PostCreateRequestDto dto = new PostCreateRequestDto();
        dto.setAuthor("author1");
        dto.setPassword("password1");
        dto.setTitle("title1");
        dto.setContent("content1");

        long postId = postService.createPost(dto);
        return postId;
    }


}