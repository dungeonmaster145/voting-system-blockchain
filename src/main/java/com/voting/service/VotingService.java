package com.voting.service;

import com.voting.blockchain.Block;
import com.voting.blockchain.BlockChain;
import com.voting.enums.PoliticalParty;
import com.voting.model.Candidate;
import com.voting.model.Vote;
import com.voting.model.Voter;
import com.voting.model.VoterToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class VotingService {

    private final BlockChain blockChain;
    private final Map<String, Voter> voters = new ConcurrentHashMap<>();
    private final Map<String, VoterToken> voterTokens = new ConcurrentHashMap<>();
    private final Map<String, Candidate> candidates = new ConcurrentHashMap<>();
    private final Set<String> usedTokens = ConcurrentHashMap.newKeySet();

    public VotingService(BlockChain blockChain) {
        this.blockChain = new BlockChain();
        this.blockChain.createGenesisBlock(); // gives the block-chain a headstart
    }

    public Voter registerVoter(String name, String email){

        boolean voterExists = voters.values().stream().anyMatch(voter -> voter.getEmail().equalsIgnoreCase(email));
        if(voterExists){
            throw new RuntimeException("Voter already exists");
        }
        Voter voter = new Voter(name, email);
        voters.put(voter.getVoterId(), voter);
        log.info("Voter registered:{} {}", name , email);
        return voter;
    }

    public VoterToken generateVotingToken(String voterId){
        Voter voter = voters.get(voterId);
        if(Objects.isNull(voter)){
            throw new RuntimeException("Voter not found");
        }
        String assignedToken = voter.getAssignedToken();
        if(!StringUtils.hasText(assignedToken)){
          VoterToken voterToken = new VoterToken(voterId);
          voterTokens.put(voterToken.getTokenId(), voterToken);
          voter.assignToken(voterToken.getTokenId());
          log.info("Voting token generated for voter:{}", voterId);
          return voterToken;
        }
        throw new RuntimeException("Voter already has a token");
    }

    public String castVote(String tokenId, String candidateId){
        if(!voterTokens.containsKey(tokenId)){
            throw new RuntimeException("Voter token does not exist");
        }
        if(!candidates.containsKey(candidateId)){
            throw new RuntimeException("Candidate does not exist");
        }
        if(usedTokens.contains(tokenId)){
            throw new RuntimeException("Token is already used. Vote not allowed");
        }
        Vote vote = new Vote(tokenId, candidateId);
        Block voteBlock = new Block(
                this.blockChain.getLatestBlock().getHash(),  // Previous hash
                vote.toJson()  // Vote data as JSON string
        );
        this.blockChain.addBlock(voteBlock);
        usedTokens.add(tokenId);
        VoterToken voterToken = voterTokens.get(tokenId);
        voterToken.markAsUsed();
        Voter voter = voters.get(voterToken.getVoterId());
        if(Objects.nonNull(voter)){
            voter.markAsVoted();
        }
        log.info("Vote casted for candidate:{} by voter:{}", candidateId, tokenId);
        return vote.getVoteId();
    }

    public Candidate registerCandidate(String candidateId, String name, PoliticalParty politicalParty){
        if(candidates.containsKey(candidateId)){
            throw new RuntimeException("Candidate already exists");
        }
        Candidate candidate = new Candidate(candidateId, politicalParty, name);
        candidates.put(candidateId, candidate);
        log.info("Candidate registered:{} {}", name , politicalParty);
        return candidate;
    }

    public Map<String, Long> getVoteResults() {
        Map<String, Long> voteResults = new HashMap<>();
        for(Block block: blockChain.getBlockChain()){
            if(block.getPrevHash().equalsIgnoreCase("0")){
                continue;
            }
            Vote vote = Vote.fromJson(block.getData());
            String candidateId = vote.getCandidateId();
            if(voteResults.containsKey(candidateId)){
                voteResults.put(candidateId, voteResults.get(candidateId)+1);
            }else{
                voteResults.put(candidateId, 1L);
            }

        }
        return voteResults;
    }
}
