package com.voting.model;

import com.voting.enums.PoliticalParty;
import lombok.Data;

@Data
public class Candidate {
    private String candidateId;
    private PoliticalParty politicalParty;
    private String candidateName;

    public Candidate(String candidateId, PoliticalParty politicalParty, String candidateName) {
        this.candidateId = candidateId;
        this.politicalParty = politicalParty;
        this.candidateName = candidateName;
    }
}
