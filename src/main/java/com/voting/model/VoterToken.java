package com.voting.model;

import lombok.Data;

@Data
public class VoterToken {
    private String tokenId;
    private boolean isUsed;
    private long createdAt;
}
