package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.SecureTokenEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.repository.SecureTokenRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Service
public class SecureTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    private final SecureTokenRepository secureTokenRepository;

    public SecureTokenService(SecureTokenRepository secureTokenRepository) {
        this.secureTokenRepository = secureTokenRepository;
    }

    public SecureTokenEntity createSecureToken(UserEntity user) {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        SecureTokenEntity secureTokenEntity = new SecureTokenEntity();
        secureTokenEntity.setToken(tokenValue);
        secureTokenEntity.setExpireAt(LocalDateTime.now().plusMinutes(30));
        secureTokenEntity.setUser(user);
        return this.secureTokenRepository.save(secureTokenEntity);
    }
}
