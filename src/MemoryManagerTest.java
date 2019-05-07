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
        byte[] bytes = { 0 };
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

        record = manager.insert("AAAA", "AAAA");
        manager.insert("CCCC", "CCCC");
        manager.remove(record, "AAAAA");
        duplicates = manager.getDuplicates();
        assertEquals(manager.getID(record)[0], bytes[0]);
        assertEquals(manager.getSequence(record)[0], bytes[0]);
        assertTrue(duplicates.contains("AAAA"));
    }


    /**
     * Tests the remove method.
     * @throws IOException 
     */
    public void testRemove() throws IOException {
        // only entry (hence, from end)
        DNARecord record = manager.insert("AAAA", "AAAA");
        byte[] seq = manager.remove(record, "AAAA");
        byte[] bytes = { 0 };
        assertEquals(seq[0], bytes[0]);
        LinkedList<FreeBlock> blocks = manager.getFreeBlocks();
        LinkedList<String> duplicates = manager.getDuplicates();
        assertTrue(blocks.isEmpty());
        assertTrue(duplicates.isEmpty());
        
        // 2 entries, non-end
        record = manager.insert("AAAA", "AAAA");
        DNARecord record2 = manager.insert("CCCC", "CCCC");
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
        
    }
}
