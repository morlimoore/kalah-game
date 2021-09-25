package com.ikenna.echendu.kalah.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String username;
    private String email;
    private String role;
    private String type;
    private String token;
    private String issuedAt;
    private String expiry;
}
