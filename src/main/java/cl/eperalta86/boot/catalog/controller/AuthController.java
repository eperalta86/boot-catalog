package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.dto.AuthResponse;
import cl.eperalta86.boot.catalog.dto.LoginRequest;
import cl.eperalta86.boot.catalog.dto.RegisterRequest;
import cl.eperalta86.boot.catalog.domain.User;
import cl.eperalta86.boot.catalog.exception.BusinessException;
import cl.eperalta86.boot.catalog.repository.UserRepository;
import cl.eperalta86.boot.catalog.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        String token = jwtTokenProvider.generateToken(authentication.getName());
        return new AuthResponse(token, authentication.getName());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("El nombre de usuario ya está en uso");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername());
    }
}
