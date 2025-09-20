package toctoce.notice_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import toctoce.notice_board.dto.CommentCreateRequestDto;
import toctoce.notice_board.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public String createComment(CommentCreateRequestDto dto, @PathVariable long postId) {
        commentService.createComment(dto, postId);
        return "redirect:/posts/" + postId;
    }

}
