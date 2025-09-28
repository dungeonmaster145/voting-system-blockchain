package com.voting.blockchain;


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Slf4j
@Data
public class Block {
    private String prevHash;
    private String data;
    private String hash;
    private long timestamp;

    public Block(String prevHash, String data) {
        this.prevHash = prevHash;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
        this.hash = calculateHash();

    }

    public String calculateHash(){
        try {
            String prevHashValue = StringUtils.hasText(prevHash) ? prevHash : "0";
            String dataVal = prevHashValue + data + timestamp;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(dataVal.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash calculation failed", e);
        }
    }

}
