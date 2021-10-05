package com.ikenna.echendu.kalah.security;

import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.ikenna.echendu.kalah.util.DateTimeUtil.*;
import static com.ikenna.echendu.kalah.util.ErrorMessageUtil.setAuthErrorMessage;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${app.jwt.validityPeriodInMilliSecs}")
    private int jwtValidityPeriodInMilliSecs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtValidityPeriodInMilliSecs))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public int getTokenValidityInSecs() {
        return jwtValidityPeriodInMilliSecs / 1000;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @SneakyThrows
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            String tokenExpiry = StringUtils.substringBetween(e.getMessage(),"JWT expired at",". Current time");
            String tokenExpiryFormatted = getSimpleDateTimeFormat().format(getJavaTimeFormat().parse(tokenExpiry.trim()));
            setAuthErrorMessage(String.format("Sorry, your token expired on %s. Please, login again to get a valid token.",
                    tokenExpiryFormatted));
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}