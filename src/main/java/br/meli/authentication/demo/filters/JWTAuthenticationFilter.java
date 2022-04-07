package br.meli.authentication.demo.filters;

import br.meli.authentication.demo.services.dto.AuthDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final String secret;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, String secret) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.secret = secret;

        setFilterProcessesUrl("/api/v1/auth");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthDTO credentials = objectMapper.readValue(request.getInputStream(), AuthDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Authentication failed.", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // get user from principal
        User user = (User) authentication.getPrincipal();

        //Instantiating parameters to create a token
        Date expirationAccessToken = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        Date expirationRefreshToken = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        String issuer = request.getRequestURL().toString();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        //Creates a token
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expirationAccessToken)
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(secret));

        //Creates a refresh token
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expirationRefreshToken)
                .withIssuer(issuer)
                .sign(Algorithm.HMAC256(secret));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("acess_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), tokens);
    }
}
