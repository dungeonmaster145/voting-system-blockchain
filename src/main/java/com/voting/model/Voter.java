package com.voting.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Voter {
    private String voterId;
    private String name;
    private String email;
    private boolean hasVoted;
    private String assignedToken;
    private long registrationTime;
    private boolean isEligible;
    
    public Voter(String name, String email) {
        this.voterId = generateVoterId();
        this.name = name;
        this.email = email;
        this.hasVoted = false;
        this.registrationTime = Instant.now().toEpochMilli();
        this.isEligible = true;
    }
    
    private String generateVoterId() {
        return "VOTER_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    public void assignToken(String tokenId) {
        this.assignedToken = tokenId;
    }
    
    public void markAsVoted() {
        this.hasVoted = true;
    }
}
