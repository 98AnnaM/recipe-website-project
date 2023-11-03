package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.errors.ObjectNotFoundException;
import bg.example.recepeWebsite.model.dto.CommentServiceModel;
import bg.example.recepeWebsite.model.entity.CommentEntity;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.view.CommentViewModel;
import bg.example.recepeWebsite.repository.CommentRepository;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentService(RecipeRepository recipeRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public List<CommentViewModel> getComments(Long recipeId, String principalName) {
        Optional<RecipeEntity> recipeOptional = this.recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            throw new ObjectNotFoundException("Recipe with id " + recipeId + " was not found!");
        }

        return recipeOptional.get()
                .getComments().stream()
                .map(comment -> mapAsComment(comment, principalName))
                .collect(Collectors.toList());
    }

    private CommentViewModel mapAsComment(CommentEntity commentEntity, String principalName) {
        CommentViewModel commentViewModel = new CommentViewModel();

        commentViewModel.setCommentId(commentEntity.getId())
                .setMessage(commentEntity.getTextContent())
                .setUser(commentEntity.getAuthor().getFirstName() + " " + commentEntity.getAuthor().getLastName())
                .setCreated(commentEntity.getCreated())
                .setCanDelete(principalName != null && isAuthorOrAdmin(principalName, commentEntity.getId()));

        return commentViewModel;
    }

    public CommentViewModel createComment(CommentServiceModel commentServiceModel) {
        RecipeEntity recipe = recipeRepository.findById(commentServiceModel.getRecipeId())
                .orElseThrow(() -> new UnsupportedOperationException("Recipe with id " + commentServiceModel.getRecipeId() + " not found!"));

        UserEntity creator = userRepository.findByUsername(commentServiceModel.getCreator())
                .orElseThrow(() -> new UnsupportedOperationException("User with username " + commentServiceModel.getCreator() + " not found!"));

        CommentEntity newComment = new CommentEntity();
        newComment.setTextContent(commentServiceModel.getMessage());
        newComment.setCreated(LocalDateTime.now());
        newComment.setAuthor(creator);
        newComment.setRecipe(recipe);

        CommentEntity savedComment = commentRepository.save(newComment);
        return mapAsComment(savedComment, creator.getUsername());
    }

    public CommentViewModel deleteComment(Long commentId, String principalName) {
        CommentEntity deleted = commentRepository.findById(commentId)
                .orElseThrow(() -> new UnsupportedOperationException("Comment with id " + commentId + " not found!"));

        commentRepository.deleteById(commentId);
        return mapAsComment(deleted, principalName);
    }

    public boolean isAuthorOrAdmin(String userName, Long commentId) {
        boolean isOwner = commentRepository.
                findById(commentId).
                filter(c -> c.getAuthor().getUsername().equals(userName)).
                isPresent();

        if (isOwner){
            return true;
        }

        return userRepository
                .findByUsername(userName)
                .filter(this::isAdmin)
                .isPresent();
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRoles().
                stream().
                anyMatch(r -> r.getRole() == RoleNameEnum.ADMIN);
    }
}
