package toctoce.notice_board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDto {
    private String author;
    private String password;
    private String content;
}
