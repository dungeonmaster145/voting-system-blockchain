package com.voting.blockchain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BlockChain {
    private List<Block> blockChain = new ArrayList<>();


    public void addBlock(Block block){
        this.blockChain.add(block);
    }

    public Block getLatestBlock(){
        if (blockChain.isEmpty()) return null;
        return blockChain.get(blockChain.size()-1);
    }

    public List<Block> getBlockChain(){
        return blockChain;
    }

    public boolean isChainValid() {
        Block firstBlock = blockChain.get(0);
        String calculatedHash = firstBlock.calculateHash();
        String currentBlockHash = firstBlock.getHash();
        if (!calculatedHash.equals(currentBlockHash)) {
            throw new RuntimeException("Invalid block chain");
        }
        for (int i = 1; i < blockChain.size(); i++) {
            String prevHash = blockChain.get(i - 1).getHash();
            String currentHash = blockChain.get(i).getHash();
            String currentHashPrevHash = blockChain.get(i).getPrevHash();
            if (!prevHash.equals(currentHashPrevHash)) {
                throw new RuntimeException("Invalid block chain due to prev Hash mismatch");
            }
            String calculatedCurrentHash = blockChain.get(i).calculateHash();
            if(!currentHash.equals(calculatedCurrentHash)){
                throw new RuntimeException("Invalid block chain due to current Hash mismatch");
            }
        }
        return true;
    }




}
