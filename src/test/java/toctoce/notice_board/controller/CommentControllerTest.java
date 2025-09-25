package toctoce.notice_board.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Comment;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.CommentCreateRequestDto;
import toctoce.notice_board.dto.CommentUpdateRequestDto;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;
import toctoce.notice_board.service.CommentService;
import toctoce.notice_board.service.PostService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean private PostService postService;
    @MockBean private CommentService commentService;

    @Test
    void 댓글_생성() throws Exception {
        // given: createPost() 스태틱 메서드를 사용해 mockPost 객체를 생성합니다.
        Post mockPost = Post.createPost(new PostCreateRequestDto());
        given(postService.findOne(anyLong()))
                .willReturn(mockPost);

        // given: commentService.createComment()가 호출되면 1L을 반환하도록 설정
        given(commentService.createComment(any(CommentCreateRequestDto.class), anyLong()))
                .willReturn(1L);

        // when & then:
        mockMvc.perform(post("/posts/1/comments")
                        .param("author", "댓글 작성자")
                        .param("password", "1234")
                        .param("content", "새로운 댓글입니다."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1")); // postId가 1인 게시글로 리다이렉션

        // verify: commentService.createComment()가 호출되었는지 검증
        verify(commentService).createComment(any(CommentCreateRequestDto.class), anyLong());
    }

    @Test
    void 댓글_수정() throws Exception {
        // given: commentService.updateComment가 아무것도 하지 않도록 설정
        willDoNothing().given(commentService).updateComment(any(CommentUpdateRequestDto.class), anyLong());

        // given: commentService.findOne()이 호출될 때 반환할 가짜 Comment 객체를 설정
        // 1. 가짜 Post 객체를 만듭니다.
        Post mockPost = mock(Post.class);
        // 2. 가짜 Post 객체의 getId() 메서드가 1L을 반환하도록 설정합니다.
        given(mockPost.getId()).willReturn(1L);

        // 3. 가짜 Comment 객체를 만듭니다.
        Comment mockComment = mock(Comment.class);
        // 4. 가짜 Comment 객체의 getPost() 메서드가 가짜 Post 객체를 반환하도록 설정합니다.
        given(mockComment.getPost()).willReturn(mockPost);

        // 5. commentService.findOne()이 호출되면 이 가짜 Comment 객체를 반환하도록 설정합니다.
        given(commentService.findOne(anyLong()))
                .willReturn(mockComment);

        // when & then:
        mockMvc.perform(put("/comments/1")
                        .param("password", "1234")
                        .param("content", "수정된 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        // verify: commentService.updateComment 메서드가 호출되었는지 확인합니다.
        verify(commentService).updateComment(any(CommentUpdateRequestDto.class), anyLong());
    }

    @Test
    void 댓글_삭제() throws Exception {
        willDoNothing().given(commentService).deleteComment(anyLong(), anyString());
        Post mockPost = mock(Post.class);
        given(mockPost.getId()).willReturn(1L);
        Comment mockComment = mock(Comment.class);
        given(mockComment.getPost()).willReturn(mockPost);
        given(commentService.findOne(anyLong()))
                .willReturn(mockComment);


        // when & then:
        mockMvc.perform(delete("/comments/1")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService).deleteComment(anyLong(), anyString());

    }
}