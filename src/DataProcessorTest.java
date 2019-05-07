import java.io.FileNotFoundException;
import java.io.IOException;
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
    
    /**
     * Sets up initial conditions
     * @throws FileNotFoundException  - if the binaryFile was not found
     */
    public void setUp() throws FileNotFoundException
    {
        DNAHashTable table = new DNAHashTable(128);
        RandomAccessFile binFile = new RandomAccessFile("processorTest.bin", "rw");
        processor = new DataProcessor(binFile, table);
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
