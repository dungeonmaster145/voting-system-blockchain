package com.voting.controller;

import com.voting.enums.PoliticalParty;
import com.voting.model.Candidate;
import com.voting.model.Voter;
import com.voting.model.VoterToken;
import com.voting.service.VotingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/voting")
@CrossOrigin(origins = "*")
public class VotingController {
    
    @Autowired
    private VotingService votingService;
    

    @PostMapping("/register-voter")
    public ResponseEntity<?> registerVoter(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            
            if (name == null || email == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Name and email are required"));
            }
            
            Voter voter = votingService.registerVoter(name, email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Voter registered successfully");
            response.put("voter", voter);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error registering voter", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Register a candidate
    @PostMapping("/register-candidate")
    public ResponseEntity<?> registerCandidate(@RequestBody Map<String, String> request) {
        try {
            String candidateId = request.get("candidateId");
            String name = request.get("name");
            String party = request.get("party");
            
            if (candidateId == null || name == null || party == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Candidate ID, name, and party are required"));
            }
            
            PoliticalParty politicalParty = PoliticalParty.valueOf(party.toUpperCase());
            Candidate candidate = votingService.registerCandidate(candidateId, name, politicalParty);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Candidate registered successfully");
            response.put("candidate", candidate);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid party name. Valid parties: BJP, INC, SP, BSP, DMK, AIADMK, AAP"));
        } catch (Exception e) {
            log.error("Error registering candidate", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Generate voting token for a voter
    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> request) {
        try {
            String voterId = request.get("voterId");
            
            if (voterId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Voter ID is required"));
            }
            
            VoterToken token = votingService.generateVotingToken(voterId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Voting token generated successfully");
            response.put("token", token);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating voting token", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Cast a vote
    @PostMapping("/cast-vote")
    public ResponseEntity<?> castVote(@RequestBody Map<String, String> request) {
        try {
            String tokenId = request.get("tokenId");
            String candidateId = request.get("candidateId");
            
            if (tokenId == null || candidateId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token ID and candidate ID are required"));
            }
            
            String voteId = votingService.castVote(tokenId, candidateId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vote cast successfully");
            response.put("voteId", voteId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error casting vote", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get vote results
    @GetMapping("/results")
    public ResponseEntity<?> getResults() {
        try {
            Map<String, Long> results = votingService.getVoteResults();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("results", results);
            response.put("totalVotes", results.values().stream().mapToLong(Long::longValue).sum());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting results", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
        }
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Blockchain Voting System is running");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
