package toctoce.notice_board.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostListResponseDto {
    private long id;
    private String author;
    private String title;
    private LocalDateTime createdAt;
}
