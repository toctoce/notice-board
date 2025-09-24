package toctoce.notice_board.controller;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;
import toctoce.notice_board.service.CommentService;
import toctoce.notice_board.service.PostService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PostService postService;
    @MockBean private CommentService commentService;

    @Test
    void 홈화면() throws Exception {
        mockMvc.perform(get("/"))
                // 응답 상태 코드가 200 (OK)인지 검증합니다.
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_목록_조회() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk()) // HTTP 상태코드 검사
                .andExpect(view().name("posts/postList")) // 뷰 이름 검사
                .andExpect(model().attributeExists("posts")) // 뷰로 전달되는 모델에 저장된 데이터 검사
                .andExpect(model().attribute("posts", IsEmptyCollection.empty())); // posts 속성이 비어있는 컬렉션인지 확인
    }

    @Test
    void 게시글_생성() throws Exception {

        given(postService.createPost(any(PostCreateRequestDto.class))).willReturn(1L);

        mockMvc.perform(post("/posts/new")
                .param("title", "테스트 제목")
                .param("author", "테스트 작성자")
                .param("password", "1234")
                .param("content", "테스트 내용입니다."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void 게시글_상세_조회_성공() throws Exception {
        // given: PostCreateRequestDto 객체를 생성합니다.
        PostCreateRequestDto dto = new PostCreateRequestDto();
        dto.setTitle("테스트 제목");
        dto.setAuthor("작성자");
        dto.setPassword("1234");
        dto.setContent("내용");

        // createPost() 스태틱 메서드를 사용해 mockPost 객체를 생성합니다.
        Post mockPost = Post.createPost(dto);

        given(postService.findOne(anyLong()))
                .willReturn(mockPost);

        // when & then:
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/postDetail"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", mockPost));
    }

    @Test
    void 게시글_수정() throws Exception {
        // given: postService.updatePost가 어떤 인자로 호출되든 아무것도 하지 않도록 설정합니다.
        willDoNothing().given(postService).updatePost(anyLong(), any(PostUpdateRequestDto.class));

        // when & then:
        mockMvc.perform(put("/posts/1/edit")
                        .param("title", "수정된 제목")
                        .param("password", "1234")
                        .param("content", "수정된 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        // verify: postService의 updatePost 메서드가 올바른 인자와 함께 호출되었는지 확인합니다.
        verify(postService).updatePost(anyLong(), any(PostUpdateRequestDto.class));
    }

    @Test
    void 게시글_삭제() throws Exception {
        // given: postService.deletePost가 어떤 인자로 호출되든 아무것도 하지 않도록 설정합니다.
        willDoNothing().given(postService).deletePost(anyLong(), anyString());

        // when & then:
        mockMvc.perform(delete("/posts/1")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        // verify: postService의 deletePost 메서드가 올바른 인자와 함께 호출되었는지 확인합니다.
        verify(postService).deletePost(anyLong(), anyString());
    }
}
