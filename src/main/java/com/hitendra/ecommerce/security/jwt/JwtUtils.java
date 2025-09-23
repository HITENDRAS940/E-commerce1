package com.hitendra.ecommerce.security.jwt;

import com.hitendra.ecommerce.security.services.UserDetailsImplementation;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie!=null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImplementation userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal);
        return ResponseCookie.from(jwtCookie, jwt)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();
    }

     //Generating token from username
    public String generateTokenFromUsername(UserDetails userDetails) {
         String username = userDetails.getUsername();
         return Jwts.builder()
                 .subject(username)
                 .issuedAt(new Date())
                 .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                 .signWith(key())
                 .compact();
    }

    //Getting username from token
    public String generateUsernameFromToken(String token) {
         return Jwts.parser()
                 .verifyWith((SecretKey)key())
                 .build().parseSignedClaims(token)
                 .getPayload().getSubject();
    }

    //Generating secret key
    public Key key() {
         return Keys.hmacShaKeyFor(
                 Decoders.BASE64.decode(jwtSecret)
         );
    }

    //Validate JWT token
    public boolean validateJwtToken(String authToken) {
         try {
             System.out.println("Validate");
             Jwts.parser()
                     .verifyWith((SecretKey) key())
                     .build()
                     .parseSignedClaims(authToken);
             return true;
         } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
         } catch (ExpiredJwtException e) {
            logger.error("Token expired: {}", e.getMessage());
         } catch (UnsupportedJwtException e) {
            logger.error("Token is not supported: {}", e.getMessage());
         } catch (IllegalArgumentException e) {
            logger.error("JWT claim string is empty: {}", e.getMessage());
         }
         return false;
    }
}