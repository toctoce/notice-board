package toctoce.notice_board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import toctoce.notice_board.dto.CommentCreateRequestDto;
import toctoce.notice_board.dto.CommentUpdateRequestDto;

import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {
    protected Comment() {}

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String auther;
    private String content;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public static Comment createComment(Post post, CommentCreateRequestDto dto) {
        Comment comment = new Comment();
        comment.setPost(post);

        comment.auther = dto.getAuthor();
        comment.content = dto.getContent();
        comment.password = dto.getPassword();
        comment.createdAt = LocalDateTime.now();

        return comment;
    }

    public void update(CommentUpdateRequestDto dto) {
        this.content = dto.getContent();
        this.updatedAt = LocalDateTime.now();
    }
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
