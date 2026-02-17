package com.ems.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        assert user != null;
        return Jwts.builder()
                    .subject(user.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(getSigningKey())
                    .compact();
}

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build().parseSignedClaims(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT token : {} ",e.getMessage());
        }
        catch (ExpiredJwtException e){
            logger.error("JWT token is expired : {} ",e.getMessage());
        }
        catch (UnsupportedJwtException e){
            logger.error("JWT token is unsupported : {}",e.getMessage());
        }
        catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty : {}",e.getMessage());
        }
        return false;
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes=Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
