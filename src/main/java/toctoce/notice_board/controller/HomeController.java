package toctoce.notice_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import toctoce.notice_board.domain.Post;
import toctoce.notice_board.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts", posts);
        return "home";
    }
}
