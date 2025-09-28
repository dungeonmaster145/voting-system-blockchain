package com.voting.model;

import lombok.Data;

@Data
public class Voter {
    private String voterId;
    private String name;
    private String email;
    private boolean hasVoted;
    private String assignedToken;
}
