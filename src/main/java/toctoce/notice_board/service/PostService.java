package toctoce.notice_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;
import toctoce.notice_board.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public long createPost(PostCreateRequestDto dto) {
        Post post = new Post(dto);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Post findOne(long id) {
        return postRepository.findOne(id);
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