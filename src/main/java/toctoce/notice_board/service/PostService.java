package toctoce.notice_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Comment;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;
import toctoce.notice_board.repository.CommentRepository;
import toctoce.notice_board.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public long createPost(PostCreateRequestDto dto) {
        Post post = Post.createPost(dto);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Post findOne(long id) {
        Post post = postRepository.findOne(id);

        List<Comment> activeComments = commentRepository.findByPostId(id);

        post.setComments(activeComments);

        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public void deletePost(long postId, String password) {
        Post post = postRepository.findOne(postId);
        if (!post.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        post.softDelete();
    }

    public void updatePost(long postId, PostUpdateRequestDto dto) {
        Post post = postRepository.findOne(postId);
        if (!post.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        post.update(dto);
    }
}