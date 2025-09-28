package voting.blockchain;

import com.voting.blockchain.Block;
import com.voting.blockchain.BlockChain;

public class BlockChainTest {
    public static void main(String[] args) {
        BlockChain blockchain = new BlockChain();

        // Build chain
        Block genesis = new Block("Genesis Block", "0");
        blockchain.addBlock(genesis);

        Block block2 = new Block("Alice votes for Candidate A", genesis.getHash());
        blockchain.addBlock(block2);

        Block block3 = new Block("Bob votes for Candidate B", block2.getHash());
        blockchain.addBlock(block3);

        // Test 1: Valid chain
        System.out.println("=== Test 1: Valid Chain ===");
        System.out.println("Chain valid: " + blockchain.isChainValid());

        // Test 2: Tamper with data
        System.out.println("\n=== Test 2: Tampering with Block Data ===");
        block2.setData("Alice votes for Candidate B");  // Changed vote!

        try {
            blockchain.isChainValid();
            System.out.println("ERROR: Should have detected tampering!");
        } catch (RuntimeException e) {
            System.out.println("Tampering detected: " + e.getMessage());
        }

        // Test 3: Fix data, tamper with prevHash
        block2.setData("Alice votes for Candidate A");  // Fix data
        block2.setHash(block2.calculateHash());  // Recalculate hash

        System.out.println("\n=== Test 3: Breaking Chain Link ===");
        block3.setPrevHash("fake_hash_12345");  // Break the link!

        try {
            blockchain.isChainValid();
            System.out.println("ERROR: Should have detected broken chain!");
        } catch (RuntimeException e) {
            System.out.println("Chain break detected: " + e.getMessage());
        }
    }
}
