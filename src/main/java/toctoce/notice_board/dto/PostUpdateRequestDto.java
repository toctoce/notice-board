package toctoce.notice_board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String password;
}
