package org.example.journex.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.UserRepository;
import org.example.journex.domain.User;
import org.example.journex.enums.UserRole;
import org.example.journex.model.LoginRequest;
import org.example.journex.model.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${jwt.blacklist}")
    private String blacklistPrefix;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
    }

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()+ expiration))
                .signWith(SignatureAlgorithm.HS256,key).compact();
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token){

        try {
            if (isBlacklisted(token)) {
                return false;
            }

            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public Long register(RegisterRequest request) {


        if(userRepository.existsByUsername(request.getUsername())){
            throw new JournexException("error.username.exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new JournexException("error.email.exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());


        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(UserRole.USER);
        user.setEnabled(true);
        user.setLocked(false);
        userRepository.save(user);
        return user.getId();

    }

    public String login(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        if(!authentication.isAuthenticated()){
            throw new JournexException("error.login.failed");}

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new JournexException("error.user.notFound"));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return generateToken(user.getUsername());

    }

    public String logout(String token) {

        if(token == null || !token.startsWith("Bearer ")) {
            throw new JournexException("error.token.invalid");
        }

        token = token.substring(7);

        if(!validate(token)) {
            throw new JournexException("error.token.invalid");
        }

        long remainingMillis = extractExpirationTime(token) - System.currentTimeMillis();

        if (remainingMillis > 0) {
            redisTemplate.opsForValue().set(
                    blacklistPrefix + token,
                    "true",
                    remainingMillis,
                    TimeUnit.MILLISECONDS
            );
        }

        return "LOGOUT_SUCCESS";
    }

    private long extractExpirationTime(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }

    private boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistPrefix + token));
    }

}