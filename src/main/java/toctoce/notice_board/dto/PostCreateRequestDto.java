package toctoce.notice_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toctoce.notice_board.domain.Post;

@Getter @Setter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String author;
    private String title;
    private String content;
    private String password;

    public PostCreateRequestDto(Post post) {
        this.author = post.getAuthor();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.password = post.getPassword();
    }
}

