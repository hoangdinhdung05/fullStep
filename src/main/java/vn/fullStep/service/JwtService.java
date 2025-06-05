package vn.fullStep.service;

import org.springframework.security.core.GrantedAuthority;
import vn.fullStep.common.TokenType;
import java.util.Collection;
import java.util.List;

public interface JwtService {

    String generateAccessToken(String username, List<String> authorities);

//    String generateRefreshToken(long userId, String username, List<String> authorities);

    String extractUsername(String token, TokenType type);

    String generateRefreshToken(String username, List<String> authorities);
}
