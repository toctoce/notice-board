package toctoce.notice_board.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostDetailResponseDto {
    private long id;
    private String author;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;
}
