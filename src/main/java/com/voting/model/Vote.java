package com.voting.model;

import java.time.Instant;

public class Vote {

    private String voterTokenId;
    private String candidateId;
    private long timestamp;

    public Vote(String voterTokenId, String candidateId) {
        this.voterTokenId = voterTokenId;
        this.candidateId = candidateId;
        this.timestamp = Instant.now().toEpochMilli();
    }



}
