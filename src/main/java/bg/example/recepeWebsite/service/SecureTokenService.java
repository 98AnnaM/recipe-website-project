package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.SecureTokenEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.repository.SecureTokenRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SecureTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    private final SecureTokenRepository secureTokenRepository;
    private final UserService userService;

    public SecureTokenService(SecureTokenRepository secureTokenRepository, UserService userService) {
        this.secureTokenRepository = secureTokenRepository;
        this.userService = userService;
    }

    private SecureTokenEntity createSecureToken(UserEntity user) {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
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
