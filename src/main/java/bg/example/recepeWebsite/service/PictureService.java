package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.errors.ObjectNotFoundException;
import bg.example.recepeWebsite.model.entity.PictureEntity;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.repository.PictureRepository;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PictureService {

    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;

    public PictureService(PictureRepository pictureRepository, UserRepository userRepository, RecipeRepository recipeRepository, CloudinaryService cloudinaryService) {
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public List<String> findAllUrls() {
        return this.pictureRepository.findAllUrls();
    }

    public PictureEntity createAndSavePictureEntity(Long userId, MultipartFile file) throws IOException {

        final CloudinaryImage upload = cloudinaryService
                .uploadImage(file);

        PictureEntity newPicture = new PictureEntity()
                .setAuthor(userRepository.findById(userId).get())
                .setUrl(upload.getUrl())
                .setPublicId(upload.getPublicId())
                .setTitle(file.getOriginalFilename());


        return pictureRepository.save(newPicture);
    }

    @Transactional
    public void deletePicture(Long id){
        Optional<PictureEntity> picture = pictureRepository.findById(id);

        if (picture.isEmpty()){
            throw new ObjectNotFoundException("Picture with id " + id + "not found!");
        }

        String publicId = picture.get().getPublicId();
        if (publicId != null) {
            cloudinaryService.delete(publicId);
        }

        pictureRepository.delete(picture.get());

    }


    public boolean isOwner(String userName, Long pictureId) {
        boolean isOwner = pictureRepository.
                findById(pictureId).
                filter(picture -> picture.getAuthor().getUsername().equals(userName)).
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

    @Transactional
    public void uploadPicture(Long ownerId, Long recipeId, MultipartFile file) throws IOException {
        RecipeEntity recipe = recipeRepository.findById(recipeId).get();
        recipe.getPictures().add(this.createAndSavePictureEntity(ownerId, file));
        this.recipeRepository.save(recipe);

    }


}
