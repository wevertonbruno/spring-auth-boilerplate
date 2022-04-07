package br.meli.authentication.demo.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public static final String HEADER_AUTORIZATION = "Authorization";
    public static final String AUTHORIZATION_TYPE = "Bearer ";

    @Value("${security.secret}")
    private String secret;

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String acessToken = request.getHeader(HEADER_AUTORIZATION);
        if(acessToken == null || !acessToken.startsWith(AUTHORIZATION_TYPE)){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(acessToken.replace(AUTHORIZATION_TYPE, ""));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token){
        String username = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();

        if(username == null) return null;

        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }
}
