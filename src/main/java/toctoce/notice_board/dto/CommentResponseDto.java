package toctoce.notice_board.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private String author;
    private String content;
    private LocalDateTime createdAt;
}
