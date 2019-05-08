import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

/**
 * Tests the methods of the DataProcessor object to make sure they
 * work as intended
 * 
 * @author marku23
 * @author ccox17
 * @version 5.6.19
 *
 */

public class DataProcessorTest extends student.TestCase {

    private DataProcessor processor;
    private final ByteArrayOutputStream outContent =
            new ByteArrayOutputStream();
    /**
     * Sets up initial conditions
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        DNAHashTable table = new DNAHashTable(128);
        RandomAccessFile binFile = 
                new RandomAccessFile("processorTest.bin", "rw");
        binFile.setLength(0);
        processor = new DataProcessor(binFile, table);
        System.setOut(new PrintStream(outContent));
    }


    /**
     * Tests the insert method to ensure that it inserts to
     * both the hash table and the memory manager as intended
     * and that the output is correct
     * 
     * @throws IOException
     *             if the file was not found
     */
    public void testInsert() throws IOException {
        setUp();
        processor.insert("AAAA", "ACGTACGT");
        processor.insert("AAAA", "ACGTACGT");
        String output = outContent.toString();
        String[] parsedOutput = output.split("\n");
        String test1 = "SequenceID AAAA exists";
        assertEquals(test1, parsedOutput[0].trim());
    }


    /**
     * Tests the remove method to ensure it removes properly
     * and the output is correct
     * 
     * @throws IOException
     *             if the file was not found
     */
    public void testRemove() throws IOException {
        processor.remove("AAAA");
        String output = outContent.toString();
        String[] parsedOutput = output.split("\n");
        String test1 = "SequenceID AAAA not found";
        assertEquals(test1, parsedOutput[0].trim());
        processor.insert("AAAA", "ACGTACGT");
        processor.remove("AAAA");
        output = outContent.toString();
        parsedOutput = output.split("\n");
        String test2 = "Sequence Removed AAAA:";
        String test3 = "ACGTACGT";
        assertEquals(test2, parsedOutput[1].trim());
        assertEquals(test3, parsedOutput[2].trim());
    }


    /**
     * Tests the search method to ensure it searches properly
     * and the output is correct
     * 
     * @throws IOException
     */
    public void testSearch() throws IOException {
        processor.search("AAAA");
        String output = outContent.toString();
        String[] parsedOutput = output.split("\n");
        String test1 = "SequenceID AAAA not found";
        assertEquals(test1, parsedOutput[0].trim());
        processor.insert("AAAA", "ACGTACGT");
        processor.search("AAAA");
        output = outContent.toString();
        parsedOutput = output.split("\n");
        String test2 = "Sequence Found: ACGTACGT";
        assertEquals(test2, parsedOutput[1].trim());
    }


    /**
     * Tests the print method to ensure that it prints out
     * all entries in the table as expected
     * 
     * @throws IOException
     *             if the file was not found
     */
    public void testPrint() throws IOException {
        processor.print();
        String output = outContent.toString();
        String[] parsedOutput = output.split("\n");
        assertEquals("Sequence IDs:", parsedOutput[0].trim());
        processor.insert("AAAA", "ACGTACGT");
        processor.print();
        output = outContent.toString();
        parsedOutput = output.split("\n");
        assertEquals("AAAA: hash slot [18]", parsedOutput[3].trim());
    }


    /**
     * Tests the binaryToDNA method to ensure it converts a byte
     * array to a DNA string as intended
     */
    public void testBinaryToDNA() {
        byte[] b0 = {};
        byte[] b1 = { 0 };
        byte[] b2 = { (byte)64 };
        byte[] b3 = { (byte)128 };
        byte[] b4 = { (byte)192 };
        byte[] b5 = { (byte)48 };
        assertEquals("", processor.binaryToDNA(b0, 0));
        assertEquals("A", processor.binaryToDNA(b1, 1));
        assertEquals("C", processor.binaryToDNA(b2, 1));
        assertEquals("G", processor.binaryToDNA(b3, 1));
        assertEquals("T", processor.binaryToDNA(b4, 1));
        assertEquals("AT", processor.binaryToDNA(b5, 2));
    }

}
