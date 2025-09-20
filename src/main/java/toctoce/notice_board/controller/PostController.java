package toctoce.notice_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.dto.PostCreateRequestDto;
import toctoce.notice_board.dto.PostUpdateRequestDto;
import toctoce.notice_board.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public String home(Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts", posts);
        return "posts/postList";
    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("PostCreateRequestDto", new PostCreateRequestDto());
        return "posts/createPostForm";
    }

    @PostMapping("/posts/new")
    public String create(PostCreateRequestDto dto) {
        long postId = postService.createPost(dto);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{postId}")
    public String showPost(Model model, @PathVariable long postId) {
        Post post = postService.findOne(postId);
        model.addAttribute("post", post);
        return "posts/postDetail";
    }

    @GetMapping("/posts/{postId}/edit")
    public String editPostForm(Model model, @PathVariable long postId) {
        Post post = postService.findOne(postId);
        model.addAttribute("post", post);
        return "posts/editPostForm";
    }

    @PutMapping("/posts/{postId}/edit")
    public String editPost(PostUpdateRequestDto dto, @PathVariable long postId) {
        postService.updatePost(postId, dto);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/posts/{postId}")
    public String deletePost(String password, @PathVariable long postId) {
        postService.deletePost(postId, password);
        return "redirect:/posts";
    }
}