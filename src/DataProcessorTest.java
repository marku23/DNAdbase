import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

/**
 * Tests the methods of the DataProcessor object to make sure they
 * work as intended
 * @author marku23
 * @author ccox17
 * @version 5.6.19
 *
 */


public class DataProcessorTest extends student.TestCase {

    private DataProcessor processor;
    private final ByteArrayOutputStream outContent =
            new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    
    /**
     * Sets up initial conditions
     * @throws FileNotFoundException  - if the binaryFile was not found
     */
    public void setUp() throws FileNotFoundException
    {
        DNAHashTable table = new DNAHashTable(128);
        RandomAccessFile binFile = new RandomAccessFile("processorTest.bin", "rw");
        processor = new DataProcessor(binFile, table);
        System.setOut(new PrintStream(outContent));
    }
 
    /**
     * Tests the insert method to ensure that it inserts to 
     * both the hash table and the memory manager as intended
     * and that the output is correct
     * @throws FileNotFoundException if the binFile was not found
     */
    public void testInsert() throws FileNotFoundException
    {
        setUp();
        processor.insert("AAAA", "ACGTACGT");
        processor.insert("AAAA", "ACGTACGT");
        String output = outContent.toString();
        String[] parsedOutput = output.split("\n");
        String test1 = "Sequence AAAA exists";
        assertEquals(test1, parsedOutput[0].trim());
    }
    
    /**
     * Tests the remove method to ensure it removes properly
     * and the output is correct
     */
    public void testRemove()
    {
        
    }
    
    /**
     * Tests the search method to ensure it searches properly
     * and the output is correct
     */
    public void testSearch()
    {
        
    }
    
    /**
     * Tests the binaryToDNA method to ensure it converts a byte 
     * array to a DNA string as intended
     */
    public void testBinaryToDNA()
    {
        byte[] b0 = {};
        byte[] b1 = {0};
        byte[] b2 = {(byte) 64};
        byte[] b3 = {(byte) 128};
        byte[] b4 = {(byte) 192};
        byte[] b5 = {(byte) 48};
        assertEquals("", processor.binaryToDNA(b0, 0));
        assertEquals("A", processor.binaryToDNA(b1, 1));
        assertEquals("C", processor.binaryToDNA(b2, 1));
        assertEquals("G", processor.binaryToDNA(b3, 1));
        assertEquals("T", processor.binaryToDNA(b4, 1));
        assertEquals("AT", processor.binaryToDNA(b5, 2));
    }
    
}
