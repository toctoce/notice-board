package toctoce.notice_board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {
    @Id @GeneratedValue
    @Column(name = "post_id")
    private long id;

    private String author;
    private String title;
    private String content;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments =  new ArrayList<>();

    protected Post() {}

    public Post(PostCreateRequestDto dto) {
        this.author = dto.getAuthor();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.password = dto.getPassword();
        this.createdAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void update(PostUpdateRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.updatedAt = LocalDateTime.now();
    }
}
