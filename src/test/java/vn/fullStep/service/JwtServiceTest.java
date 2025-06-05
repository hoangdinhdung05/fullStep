package vn.fullStep.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import vn.fullStep.common.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtServiceTest {

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAccessToken_Success() {
        // Arrange
        String username = "john.doe";
        List<String> authorities = List.of("USER", "ADMIN");
        String expectedToken = "mockAccessToken";

        when(jwtService.generateAccessToken(username, authorities)).thenReturn(expectedToken);

        // Act
        String actualToken = jwtService.generateAccessToken(username, authorities);

        // Assert
        assertEquals(expectedToken, actualToken);
        verify(jwtService, times(1)).generateAccessToken(username, authorities);
    }

    @Test
    void testGenerateRefreshToken_Success() {
        // Arrange
        String username = "john.doe";
        List<String> authorities = List.of("USER", "ADMIN");
        String expectedToken = "mockRefreshToken";

        when(jwtService.generateRefreshToken(username, authorities)).thenReturn(expectedToken);

        // Act
        String actualToken = jwtService.generateRefreshToken(username, authorities);

        // Assert
        assertEquals(expectedToken, actualToken);
        verify(jwtService, times(1)).generateRefreshToken(username, authorities);
    }

    @Test
    void testExtractUsername_Success() {
        // Arrange
        String token = "mockToken";
        TokenType tokenType = TokenType.ACCESS_TOKEN;
        String expectedUsername = "john.doe";

        when(jwtService.extractUsername(token, tokenType)).thenReturn(expectedUsername);

        // Act
        String actualUsername = jwtService.extractUsername(token, tokenType);

        // Assert
        assertEquals(expectedUsername, actualUsername);
        verify(jwtService, times(1)).extractUsername(token, tokenType);
    }
}
