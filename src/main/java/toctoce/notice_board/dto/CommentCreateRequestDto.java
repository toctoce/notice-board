package toctoce.notice_board.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private String author;
    private String password;
    private String content;
}
