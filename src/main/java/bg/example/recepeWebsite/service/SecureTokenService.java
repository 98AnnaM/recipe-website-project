package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.SecureTokenEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.repository.SecureTokenRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SecureTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);

    private final SecureTokenRepository secureTokenRepository;

    public SecureTokenService(SecureTokenRepository secureTokenRepository) {
        this.secureTokenRepository = secureTokenRepository;
    }

    public SecureTokenEntity createSecureToken(UserEntity user) {
        String tokenValue = new String(Base64.encodeBase64URLSafeString(DEFAULT_TOKEN_GENERATOR.generateKey()));
        SecureTokenEntity secureTokenEntity = new SecureTokenEntity();
        secureTokenEntity.setToken(tokenValue);
        secureTokenEntity.setExpireAt(LocalDateTime.now().plusMinutes(30));
        secureTokenEntity.setUser(user);
        return this.secureTokenRepository.save(secureTokenEntity);
    }

    public void cleanUpSecureTokens(){
        List<SecureTokenEntity> tokensToDelete = secureTokenRepository.findAllByExpireAtBefore(LocalDateTime.now());
        secureTokenRepository.deleteAll(tokensToDelete);
    }
}
