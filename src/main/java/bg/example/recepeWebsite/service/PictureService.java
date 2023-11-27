package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.PictureEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.model.view.PictureHomePageViewModel;
import bg.example.recepeWebsite.model.view.PictureViewModel;
import bg.example.recepeWebsite.repository.PictureRepository;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.TypeRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import bg.example.recepeWebsite.web.exception.InvalidFileException;
import bg.example.recepeWebsite.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class PictureService {

    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final TypeRepository typeRepository;

    public PictureService(PictureRepository pictureRepository, UserRepository userRepository, RecipeRepository recipeRepository, CloudinaryService cloudinaryService, ModelMapper modelMapper, TypeRepository typeRepository) {
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.typeRepository = typeRepository;
    }

    public Page<PictureViewModel> findAllPictureViewModelsByUsername(String principalName, Pageable pageable) {
        Page<PictureEntity> pictures = this.pictureRepository
                .findAllByAuthor_Username(principalName, pageable);

        return pictures.map(picture -> this.mapToPictureViewModel(picture, principalName));
    }

    public PictureEntity createAndSavePictureEntity(Long userId, MultipartFile file, Long recipeId) {

        try {
            final CloudinaryImage upload = cloudinaryService
                    .uploadImage(file);
            PictureEntity newPicture = new PictureEntity()
                    .setAuthor(userRepository.findById(userId)
                            .orElseThrow(() -> new ObjectNotFoundException("User with id " + userId + " not found!")))
                    .setUrl(upload.getUrl())
                    .setPublicId(upload.getPublicId())
                    .setTitle(file.getOriginalFilename())
                    .setRecipe(recipeRepository.findById(recipeId).orElse(null));

            return pictureRepository.save(newPicture);
        } catch (RuntimeException | IOException e) {
            throw new InvalidFileException("File with name " + file.getOriginalFilename() + " can not be uploaded.");
        }
    }

    @Transactional
    public void deletePicture(Long id) {
        Optional<PictureEntity> picture = pictureRepository.findById(id);

        if (picture.isEmpty()) {
            throw new ObjectNotFoundException("Picture with id " + id + " not found!");
        }

        String publicId = picture.get().getPublicId();
        if (publicId != null) {
            cloudinaryService.delete(publicId);
        }

        pictureRepository.deleteById(id);
    }


    public boolean isOwner(String userName, Long pictureId) {
        boolean isOwner = pictureRepository.
                findById(pictureId).
                filter(picture -> picture.getAuthor().getUsername().equals(userName)).
                isPresent();

        if (isOwner) {
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

    public PictureViewModel mapToPictureViewModel(PictureEntity picture, String principalName) {
        PictureViewModel pictureViewModel = modelMapper.map(picture, PictureViewModel.class);
        pictureViewModel.setRecipeId(picture.getRecipe().getId())
                .setAuthorUsername(picture.getAuthor().getUsername())
                .setCanNotDelete(!isOwner(principalName, picture.getId()));
        return pictureViewModel;
    }

    public PictureHomePageViewModel mapToPictureHomePageViewModel(PictureEntity picture) {
        PictureHomePageViewModel pictureHomePageViewModel = modelMapper.map(picture, PictureHomePageViewModel.class);
        pictureHomePageViewModel.setRecipeId(picture.getRecipe().getId())
                .setAuthorUsername(picture.getAuthor().getUsername());
        return pictureHomePageViewModel;
    }

    public List<PictureHomePageViewModel> getThreeRandomPicturesByRecipeType(TypeNameEnum typeNameEnum) {
        List<PictureEntity> allPictures = pictureRepository.findAllPicturesByRecipeType(
                this.typeRepository.findByName(typeNameEnum)
                        .orElseThrow(() -> new ObjectNotFoundException("TypeEntity with name " + typeNameEnum + " not found!")));

        List<PictureHomePageViewModel> resultPictures = new ArrayList<>();
        Set<Long> selectedRecipeIds = new HashSet<>();

        // Shuffle the list to get a random order
        Collections.shuffle(allPictures);

        for (PictureEntity picture : allPictures) {
            // Check if we already have a picture from the same recipeId
            if (!selectedRecipeIds.contains(picture.getRecipe().getId())) {
                resultPictures.add(this.mapToPictureHomePageViewModel(picture));
                selectedRecipeIds.add(picture.getRecipe().getId());
            }

            // Check if we have collected 3 pictures
            if (resultPictures.size() == 3) {
                break;
            }
        }
        return resultPictures;
    }
}




