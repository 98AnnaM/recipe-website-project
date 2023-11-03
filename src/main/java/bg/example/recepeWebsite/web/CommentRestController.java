package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.AddCommentDto;
import bg.example.recepeWebsite.model.dto.CommentServiceModel;
import bg.example.recepeWebsite.model.validation.ApiError;
import bg.example.recepeWebsite.model.view.CommentViewModel;
import bg.example.recepeWebsite.service.CommentService;
import bg.example.recepeWebsite.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class CommentRestController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public CommentRestController(CommentService commentService, ModelMapper modelMapper, UserService userService) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/api/{recipeId}/comments")
    public ResponseEntity<List<CommentViewModel>> getComments(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(commentService.getComments(recipeId, principal != null ? principal.getUsername() : null));

    }


    @PostMapping("/api/{recipeId}/comments")
    public ResponseEntity<CommentViewModel> newComment(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long recipeId,
            @RequestBody @Valid AddCommentDto addCommentDto) throws AccessDeniedException {

        if (principal == null) {
           throw new AccessDeniedException("Only logged users can post comments.");

        }

        CommentServiceModel commentServiceModel = modelMapper.map(addCommentDto, CommentServiceModel.class);
        commentServiceModel.setCreator(principal.getUsername());
        commentServiceModel.setRecipeId(recipeId);

        CommentViewModel newComment = commentService.createComment(commentServiceModel);
        URI locationOfNewComment =
                URI.create(String.format("/api/%s/comments/%s", recipeId, newComment.getCommentId()));

        return ResponseEntity
                .created(locationOfNewComment)
                .body(newComment);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> onValidationFailure(MethodArgumentNotValidException exc) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        exc.getFieldErrors().forEach(fe -> apiError.addFieldWithError(fe.getField()));

        return ResponseEntity.badRequest().body(apiError);
    }


    @DeleteMapping("/api/{recipeId}/comments/{commentId}")
    public ResponseEntity<CommentViewModel> deleteComment(
            @PathVariable("commentId") Long commentId, @PathVariable("recipeId") Long recipeId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal != null && commentService.isAuthorOrAdmin(principal.getUsername(), commentId)) {
            CommentViewModel deleted = commentService.deleteComment(commentId, principal.getUsername());
            return ResponseEntity.ok(deleted);
        }

        return ResponseEntity.status(403).build();
    }

}
