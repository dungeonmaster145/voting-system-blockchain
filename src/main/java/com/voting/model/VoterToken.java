package com.voting.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Data
@NoArgsConstructor
public class VoterToken {
    private String tokenId;
    private boolean isUsed;
    private long createdAt;
    private String voterId; // Link to voter for validation
    
    public VoterToken(String voterId) {
        this.voterId = voterId;
        this.tokenId = generateSecureToken();
        this.isUsed = false;
        this.createdAt = Instant.now().toEpochMilli();
    }
    
    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
    
    public void markAsUsed() {
        this.isUsed = true;
    }
}
