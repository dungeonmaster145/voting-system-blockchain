package com.voting.blockchain;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Data
public final class Block {

    private final String prevHash;
    private final String data;
    private final String hash;
    private final long timestamp;
    private long miningCounter;

    public Block(String prevHash, String data) {
        this.prevHash = prevHash == null ? "0" : prevHash;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
        this.miningCounter = 0;
        this.hash = mineBlock(4); // Default difficulty of 4
    }

    public Block(String prevHash, String data, int difficulty) {
        this.prevHash = prevHash == null ? "0" : prevHash;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
        this.miningCounter = 0;
        this.hash = mineBlock(difficulty);
    }

    public String calculateHash() {
        try {
            String payload = prevHash + "|" + data + "|" + timestamp + "|" + miningCounter;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(
                    digest.digest(payload.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm error", e);
        }
    }

    public String mineBlock(int difficulty) {
        String target = "0".repeat(difficulty);
        String hash;
        
        log.info("Mining block with difficulty: {}", difficulty);
        long startTime = System.currentTimeMillis();
        
        do {
            miningCounter++;
            hash = calculateHash();
        } while (!hash.startsWith(target));
        
        long endTime = System.currentTimeMillis();
        log.info("Block mined in {} ms with nonce: {}", (endTime - startTime), miningCounter);
        
        return hash;
    }
}

