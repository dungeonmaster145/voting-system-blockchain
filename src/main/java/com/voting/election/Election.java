package com.voting.election;

import com.voting.enums.Phase;
import com.voting.model.Candidate;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Election {
    private String electionId;
    private String title;
    private String description;
    private Phase currentPhase;
    private long startTime;
    private long duration;
    private long endTime;
    private List<Candidate> candidates;
    private int totalVotes;
    private boolean isActive;

    public Election(String title, String description, long durationInHours) {
        this.electionId = "ELECTION_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        this.title = title;
        this.description = description;
        this.currentPhase = Phase.REGISTRATION;
        this.startTime = Instant.now().toEpochMilli();
        this.endTime = this.startTime + (durationInHours * 60 * 60 * 1000);
        this.candidates = new ArrayList<>();
        this.totalVotes = 0;
        this.isActive = true;
    }

    public void addCandidate(Candidate candidate){
        if(currentPhase.equals(Phase.REGISTRATION)){
            candidates.add(candidate);
        }else{
            throw new RuntimeException("Cannot add candidate in current phase");
        }
    }

    public void moveToNextPhase(){
        switch (currentPhase){
            case REGISTRATION -> currentPhase = Phase.COMMIT;
            case COMMIT -> currentPhase = Phase.REVEAL;
            case REVEAL -> currentPhase = Phase.TALLY;
            case TALLY -> {
                currentPhase = Phase.CLOSED;
                isActive = false;
            }
            default -> throw new RuntimeException("Invalid phase");
        }
    }

    public boolean isElectionActive(){
        long currentTime = Instant.now().toEpochMilli();
        return currentTime >= startTime && currentTime <= endTime && isActive;

    }

    public void incrementVoteCount(){
         totalVotes++;
    }
}
