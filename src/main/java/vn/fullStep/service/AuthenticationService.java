package vn.fullStep.service;

import vn.fullStep.controller.request.SignInRequest;
import vn.fullStep.controller.response.TokenResponse;
import java.nio.file.AccessDeniedException;

public interface AuthenticationService {

    TokenResponse getAccessToken(SignInRequest request) throws AccessDeniedException;

    TokenResponse getRefreshToken(String refreshToken);

}
