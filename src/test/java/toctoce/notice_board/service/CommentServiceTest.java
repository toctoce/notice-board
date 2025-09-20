package toctoce.notice_board.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Comment;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.CommentCreateRequestDto;
import toctoce.notice_board.dto.CommentUpdateRequestDto;
import toctoce.notice_board.dto.PostCreateRequestDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired PostService postService;
    @Autowired CommentService commentService;

    @Test
    public void 댓글_생성() {
        // given
        //when
        long postId = createPost();
        long commentId = createComment(postId);

        //then
        Comment comment = commentService.findOne(commentId);
        assertEquals("content1", comment.getContent());
        assertEquals("author1", comment.getAuthor());
        assertEquals("password1", comment.getPassword());
    }
    @Test
    public void 댓글_수정_성공() {
        //given
        long postId = createPost();
        long commentId = createComment(postId);

        //when
        Comment comment = commentService.findOne(commentId);
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto();
        dto.setContent("content2");
        dto.setPassword("password1");
        comment.update(dto);

        //then
        assertEquals("content2", comment.getContent());
        assertEquals("password1", comment.getPassword());
        assertNotNull(comment.getUpdatedAt());
    }
    @Test
    public void 댓글_수정_실패() {
        //given
        long postId = createPost();
        long commentId = createComment(postId);

        //when
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto();
        dto.setContent("content2");
        dto.setPassword("password2");
        assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(dto,commentId));

        //then
        Comment comment = commentService.findOne(commentId);
        assertEquals("content1", comment.getContent());
        assertEquals("password1", comment.getPassword());
        assertNull(comment.getUpdatedAt());

    }

    @Test
    public void 댓글_삭제_성공() {
        //given
        long postId = createPost();
        long commentId = createComment(postId);

        //when
        commentService.deleteComment(commentId, "password1");

        //then
        Comment comment = commentService.findOne(commentId);
        assertEquals("content1", comment.getContent());
        assertNotNull(comment.getDeletedAt());

        Post post = postService.findOne(postId);
        assertEquals(0, post.getComments().size());
    }
    @Test
    public void 댓글_삭제_실패() {
        //given
        long postId = createPost();
        long commentId = createComment(postId);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> commentService.deleteComment(commentId, "wrong password"));

        //then
        Comment comment = commentService.findOne(commentId);
        assertEquals("content1", comment.getContent());
        assertNull(comment.getDeletedAt());

        Post post = postService.findOne(postId);
        assertEquals(1, post.getComments().size());
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
    private long createComment(long postId) {
        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        dto.setAuthor("author1");
        dto.setPassword("password1");
        dto.setContent("content1");

        long commentId = commentService.createComment(dto, postId);
        return commentId;
    }
}
