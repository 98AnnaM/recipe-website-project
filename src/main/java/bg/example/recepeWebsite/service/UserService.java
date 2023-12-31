package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.dto.UserEditDto;
import bg.example.recepeWebsite.model.dto.UserRegisterDto;
import bg.example.recepeWebsite.model.email.AccountVerificationEmailContext;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.SecureTokenEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.view.UserView;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.RoleRepository;
import bg.example.recepeWebsite.repository.SecureTokenRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import bg.example.recepeWebsite.web.exception.InvalidTokenException;
import bg.example.recepeWebsite.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final RecipeRepository recipeRepository;
    private final SecureTokenRepository secureTokenRepository;
    private final SecureTokenService secureTokenService;
    @Value("${site.base.url}")
    private String baseURL;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService, RecipeRepository recipeRepository, SecureTokenRepository secureTokenRepository, SecureTokenService secureTokenService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.recipeRepository = recipeRepository;
        this.secureTokenRepository = secureTokenRepository;
        this.secureTokenService = secureTokenService;
    }

    public void register(UserRegisterDto userRegisterDto, Locale preferedLocale) {
        UserEntity newUser = modelMapper.map(userRegisterDto, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        newUser.setRoles(roleRepository.findAll()
                .stream()
                .filter(r -> r.getRole() == (RoleNameEnum.USER))
                .collect(Collectors.toList()));

        newUser.setAccountVerified(false);
        sendVerificationMail(this.userRepository.save(newUser), preferedLocale);
    }

    public void sendVerificationMail(UserEntity newUser, Locale preferedLocale){
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        SecureTokenEntity token = this.secureTokenService.createSecureToken(newUser);

        emailContext.setToken(token.getToken());
        emailContext.setLocale(preferedLocale);
        emailContext.setBaseUrl(baseURL);
        emailContext.initContext(newUser);

        emailService.sendEmail(emailContext);
    }

    public UserView findById(Long id) {
        return this.userRepository.findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserView.class))
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found!"));
    }

    public void updateUserProfile(UserEditDto userEditDto) {
        UserEntity user = this.userRepository.findById(userEditDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + userEditDto.getId() + " was not found!"));

        user.setFirstName(userEditDto.getFirstName())
                .setLastName(userEditDto.getLastName())
                .setUsername(userEditDto.getUsername())
                .setEmail(userEditDto.getEmail());

        this.userRepository.save(user);
    }

    public UserEditDto getUserEditDetails(Long id) {
        return this.userRepository.findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserEditDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("User with ID " + id + " not found!"));
    }

    public boolean usernameExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Transactional
    public boolean addOrRemoveRecipeFromFavorites(String username, Long id) {
        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User with username " + username + " was not found!"));

        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with id " + id + " not found!"));

        if (!user.getFavorites().contains(recipe)) {
            user.getFavorites().add(recipe);
        } else {
            user.getFavorites().remove(recipe);
        }
        userRepository.save(user);
        return user.getFavorites().contains(recipe);
    }

    public List<String> getAdminsEmails() {
        return this.userRepository.findByRole(RoleNameEnum.ADMIN).stream().map(UserEntity::getEmail).collect(Collectors.toList());
    }

    public long getCountRegisteredUsers() {
        return this.userRepository.count();
    }

    public UserEntity findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email " + email + "not found!"));
    }

    public void verifyAccount(String token) {
        Optional<SecureTokenEntity> tokenOpt = secureTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired() || tokenOpt.get().getUser() == null) {
            throw new InvalidTokenException("Token is invalid.");
        }

        SecureTokenEntity secureToken = tokenOpt.get();
        UserEntity user = secureToken.getUser();

        user.setAccountVerified(true);
        userRepository.save(user);
        secureTokenRepository.delete(secureToken);
    }

    public boolean notVerifiedProfile(String username) {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        return user != null && !user.isAccountVerified();
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User with username " + username + "not found!"));
    }

    public void cleanUpNotVerifiedUsers(){
        List<UserEntity> usersToDelete = userRepository
                .findAllByAccountVerifiedEqualsAndCreatedOnBefore(false,
                        Timestamp.valueOf(LocalDateTime.now().minusMinutes(25)));

        userRepository.deleteAll(usersToDelete);
    }
}
