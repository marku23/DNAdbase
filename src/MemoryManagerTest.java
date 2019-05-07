import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

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
     * @throws FileNotFoundException 
     */
    
    public void setUp() throws FileNotFoundException {
        bin = new RandomAccessFile("managerTest.bin", "rw");
        
    }
}
