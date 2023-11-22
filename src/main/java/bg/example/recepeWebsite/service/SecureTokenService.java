package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.SecureTokenEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.repository.SecureTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SecureTokenService {

    private final SecureTokenRepository secureTokenRepository;
    private final UserService userService;

    public SecureTokenService(SecureTokenRepository secureTokenRepository, UserService userService) {
        this.secureTokenRepository = secureTokenRepository;
        this.userService = userService;
    }

    private SecureTokenEntity createSecureToken(UserEntity user) {
        String tokenValue = UUID.randomUUID().toString();
        SecureTokenEntity secureTokenEntity = new SecureTokenEntity();
        secureTokenEntity.setToken(tokenValue);
        secureTokenEntity.setExpireAt(LocalDateTime.now().plusMinutes(30));
        secureTokenEntity.setUser(user);
        return this.secureTokenRepository.save(secureTokenEntity);
    }


    public String createSecureTokenResetLink(String email){

        UserEntity user = this.userService.findByEmail(email);
        SecureTokenEntity tokenEntity = createSecureToken(user);

        String baseURL = "http://localhost:8080";

        String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/password/change").queryParam("token", tokenEntity.getToken()).toUriString();

        return url;
    }
}
