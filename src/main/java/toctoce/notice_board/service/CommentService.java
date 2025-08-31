package toctoce.notice_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Comment;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.CommentCreateRequestDto;
import toctoce.notice_board.dto.CommentUpdateRequestDto;
import toctoce.notice_board.repository.CommentRepository;
import toctoce.notice_board.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Comment findOne(long id) {
        return commentRepository.findOne(id);
    }
    @Transactional(readOnly = true)
    public List<Comment> findPostComments(long postId) {
        return commentRepository.findByPostId(postId);
    }
    public long createComment(CommentCreateRequestDto dto, long postId) {
        Post post = postRepository.findOne(postId);
        Comment comment = Comment.createComment(post, dto);
        commentRepository.save(comment);
        return comment.getId();
    }
    public void updateComment(CommentUpdateRequestDto dto, long commentId) {
        Comment comment = commentRepository.findOne(commentId);
        if (!comment.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        comment.update(dto);
    }

    public void deleteComment(long commentId, String password) {
        Comment comment = commentRepository.findOne(commentId);
        if (!comment.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        comment.softDelete();
    }
}
