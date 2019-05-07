import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * Tests the methods of the MemoryManager Class.
 * 
 * 
 * @author Christopher Cox (ccox17)
 * @version 05.06.2019
 *
 */
public class MemoryManagerTest extends student.TestCase {
    // Variables...............................................................
    private MemoryManager manager;
    private RandomAccessFile bin;


    // Methods.................................................................

    /**
     * Sets up variables prior to each test.
     * 
     * @throws IOException
     */

    public void setUp() throws IOException {
        bin = new RandomAccessFile("managerTest.bin", "rw");
        bin.setLength(0);
        manager = new MemoryManager(bin);
    }


    /**
     * Tests the insert method.
     * 
     * @throws IOException
     */

    public void testInsert() throws IOException {
        // To an empty memory manager
        DNARecord record = manager.insert("AAAA", "AAAA");
        byte[] bytes = { 0, -86 };
        LinkedList<String> duplicates = manager.getDuplicates();
        assertEquals(manager.getID(record)[0], bytes[0]);
        assertEquals(manager.getSequence(record)[0], bytes[0]);
        assertTrue(duplicates.contains("AAAA"));

        // More complex, multiples of 4 still
        DNARecord record2 = manager.insert("CCCC", "CCCCCAGTCGATGGGC");
        byte[] bytes2 = { 85, 75, 99, -87 };
        duplicates = manager.getDuplicates();
        assertEquals(manager.getID(record2)[0], bytes2[0]);
        assertEquals(manager.getSequence(record2)[0], bytes2[0]);
        assertEquals(manager.getSequence(record2)[1], bytes2[1]);
        assertEquals(manager.getSequence(record2)[2], bytes2[2]);
        assertEquals(manager.getSequence(record2)[3], bytes2[3]);
        assertTrue(duplicates.contains("CCCC"));

        // Not multiples of 4
        DNARecord record3 = manager.insert("GGG", "GGGAACGTCG");
        byte[] bytes3 = { -88, 27, 96 };
        duplicates = manager.getDuplicates();
        assertEquals(manager.getID(record3)[0], bytes3[0]);
        assertEquals(manager.getSequence(record3)[0], bytes3[0]);
        assertEquals(manager.getSequence(record3)[1], bytes3[1]);
        assertEquals(manager.getSequence(record3)[2], bytes3[2]);
        assertTrue(duplicates.contains("GGG"));

        // After a removal from the front, multiple entries

        manager.remove(record, "AAAAA");
        record = manager.insert("GGGG", "GGGG");
        duplicates = manager.getDuplicates();
        assertEquals(manager.getID(record)[0], bytes[1]);
        assertEquals(manager.getSequence(record)[0], bytes[1]);
        assertTrue(duplicates.contains("GGGG"));

        // After a removal from the end, not full-sized entries
        manager.remove(record3, "GGG");
        record3 = manager.insert("TT", "TTTT");
        duplicates = manager.getDuplicates();
        LinkedList<FreeBlock> blocks = manager.getFreeBlocks();
        assertEquals(record3.getIDOffset(), 7);
        assertTrue(blocks.isEmpty());

        // After a removal from the middle, not full-size entries
        manager.remove(record2, "CCCC");
        record2 = manager.insert("ACGT", "ACGTCCCC");
        blocks = manager.getFreeBlocks();
        assertFalse(blocks.isEmpty());
        assertEquals(blocks.getFirst().getOffset(), 5);
        assertEquals(blocks.getFirst().getSize(), 2);

        // With empty space that cannot fit next entry
        DNARecord record4 = manager.insert("AAAACCCCGGGG",
            "AAAACCCCGGGGTTTTACGT");
        blocks = manager.getFreeBlocks();
        assertFalse(blocks.isEmpty());
        assertEquals(blocks.getFirst().getOffset(), 5);
        assertEquals(blocks.getFirst().getSize(), 2);

        // With empty space that can only fit ID
        DNARecord record5 = manager.insert("ACGTACGT", "ACGTACGTACGT");
        blocks = manager.getFreeBlocks();
        assertTrue(blocks.isEmpty());

        // inserting duplicate
        DNARecord nullRecord = manager.insert("ACGTACGT", "ACGTACGTACGT");
        assertNull(nullRecord);

    }


    /**
     * Tests the remove method.
     * 
     * @throws IOException
     */
    public void testRemove() throws IOException {
        // Fields
        DNARecord record;
        DNARecord record2;
        DNARecord record3;
        DNARecord record4;
        DNARecord record5;
        LinkedList<String> duplicates;
        LinkedList<FreeBlock> blocks;
        // only entry (hence, from end)
        record = manager.insert("AAAA", "AAAA");
        byte[] seq = manager.remove(record, "AAAA");
        byte[] bytes = { 0 };
        assertEquals(seq[0], bytes[0]);
        blocks = manager.getFreeBlocks();
        duplicates = manager.getDuplicates();
        assertTrue(blocks.isEmpty());
        assertTrue(duplicates.isEmpty());

        // 2 entries, non-end
        record = manager.insert("AAAA", "AAAA");
        record2 = manager.insert("CCCC", "CCCC");
        seq = manager.remove(record, "AAAA");
        assertEquals(seq[0], bytes[0]);
        blocks = manager.getFreeBlocks();
        duplicates = manager.getDuplicates();
        assertEquals(blocks.getFirst(), blocks.getLast());
        FreeBlock block = blocks.getFirst();
        assertEquals(block.getOffset(), 0);
        assertEquals(block.getSize(), 2);
        assertTrue(duplicates.contains("CCCC"));
        assertFalse(duplicates.contains("AAAA"));

        // 2 entries, end
        record = manager.insert("GGGG", "GGGG");
        byte[] seq2 = manager.remove(record2, "CCCC");
        byte[] bytes2 = { 85 };
        assertEquals(seq2[0], bytes2[0]);
        blocks = manager.getFreeBlocks();
        duplicates = manager.getDuplicates();
        assertTrue(blocks.isEmpty());
        assertFalse(duplicates.contains("CCCC"));
        assertTrue(duplicates.contains("GGGG"));

        // Reset manager to begin testing with 3 entries
        manager.clear();
        record = manager.insert("AAAA", "AAAA");
        record2 = manager.insert("CCCC", "CCCC");
        record3 = manager.insert("GGGG", "GGGG");

        // 3 entries, first
        seq = manager.remove(record, "AAAA");
        assertEquals(seq[0], bytes[0]);
        blocks = manager.getFreeBlocks();
        duplicates = manager.getDuplicates();
        assertEquals(blocks.getFirst(), blocks.getLast());
        block = blocks.getFirst();
        assertEquals(block.getOffset(), 0);
        assertEquals(block.getSize(), 2);
        assertTrue(duplicates.contains("CCCC"));
        assertFalse(duplicates.contains("AAAA"));

        record = manager.insert("AAAA", "AAAA");

        // 3 entries, middle
        seq2 = manager.remove(record2, "CCCC");
        assertEquals(seq2[0], bytes2[0]);
        blocks = manager.getFreeBlocks();
        duplicates = manager.getDuplicates();
        block = blocks.getFirst();
        assertEquals(block.getOffset(), 2);
        assertEquals(block.getSize(), 2);
        assertEquals(blocks.size(), 1);
        assertFalse(duplicates.contains("CCCC"));
        assertTrue(duplicates.contains("GGGG"));
        
        record2 = manager.insert("CCCC", "CCCC");
        
        // 3 entries, end
        byte[] seq3 = manager.remove(record3, "GGGG");
        byte[] bytes3 = { -86 };
        assertEquals(seq3[0], bytes3[0]);
        blocks = manager.getFreeBlocks();
        assertTrue(blocks.isEmpty());
        
        // reset to test with 5 entries, in order to test merging with blocks in the list
        manager.clear();
        record = manager.insert("AAAA", "AAAA");
        record2 = manager.insert("CCCC", "CCCC");
        record3 = manager.insert("GGGG", "GGGG");
        record4 = manager.insert("TTTT", "TTTT");
        record5 = manager.insert("ACGT", "ACGT");
        
        // Merging to left
        manager.remove(record2, "CCCC");
        manager.remove(record3, "GGGG");
        blocks = manager.getFreeBlocks();
        assertEquals(blocks.size(), 1);
        block = blocks.getFirst();
        assertEquals(block.getOffset(), 2);
        assertEquals(block.getSize(), 4);
        
        record2 = manager.insert("CCCC", "CCCC");
        record3 = manager.insert("GGGG", "GGGG");
        
        // Merging to right
        manager.remove(record3, "GGGG");
        manager.remove(record4, "TTTT");
        blocks = manager.getFreeBlocks();
        assertEquals(blocks.size(), 1);
        block = blocks.getFirst();
        assertEquals(block.getOffset(), 4);
        assertEquals(block.getSize(), 4);
        
        record3 = manager.insert("GGGG", "GGGG");
        record4 = manager.insert("TTTT", "TTTT");
        
        // Merging both ways
        manager.remove(record2, "CCCC");
        manager.remove(record4, "TTTT");
        blocks = manager.getFreeBlocks();
        assertEquals(blocks.size(), 2);
        block = blocks.getFirst();
        assertEquals(block.getOffset(), 2);
        assertEquals(block.getSize(), 2);
        block = blocks.getLast();
        assertEquals(block.getOffset(), 6);
        assertEquals(block.getSize(), 2);
        manager.remove(record3, "GGGG");
        blocks = manager.getFreeBlocks();
        assertEquals(blocks.size(), 1);
        block = blocks.getFirst();
    }
}
