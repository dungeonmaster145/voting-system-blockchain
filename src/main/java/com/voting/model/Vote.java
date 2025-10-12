package com.voting.model;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Vote {

    private String voterTokenId;
    private String candidateId;
    private long timestamp;
    private String voteId;

    public Vote(String voterTokenId, String candidateId) {
        this.voterTokenId = voterTokenId;
        this.candidateId = candidateId;
        this.timestamp = Instant.now().toEpochMilli();
        this.voteId = generateVoteId();
    }
    
    private String generateVoteId() {
        return "VOTE_" + System.nanoTime() + "_" + voterTokenId.hashCode();
    }
    
    public String toJson() {
        return new Gson().toJson(this);
    }
    
    public static Vote fromJson(String json) {
        return new Gson().fromJson(json, Vote.class);
    }
}
