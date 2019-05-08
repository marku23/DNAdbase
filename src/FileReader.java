
/**
 * A java class that reads in commands from the input file and
 * calls methods to process them accordingly by using the hash
 * file and the memory file.
 * 
 * @author marku23
 * @author ccox17
 *
 */
import java.util.*;
import java.io.*;

public class FileReader {

    private DataProcessor processor;
    private Scanner reader;
    private File input;
    private RandomAccessFile hash;
    private RandomAccessFile memory;


    /**
     * The default constructor for this file reader object
     * 
     * @param inputFile
     *            - the file we are reading input from
     * @param hashFile
     *            - the file we are hashing to
     * @param memoryFile
     *            - the file we are storing sequences in
     * @throws IOException
     */
    public FileReader(
        String inputFile,
        String hashFile,
        String memoryFile,
        int tableSize)
        throws IOException {
        input = new File(inputFile);
        hash = new RandomAccessFile(hashFile, "rw");
        memory = new RandomAccessFile(memoryFile, "rw");
        memory.setLength(0);
        reader = new Scanner(input);
        processor = new DataProcessor(memory, new DNAHashTable(tableSize));
    }


    /**
     * Reads through the input file and calls methods to process
     * the commands accordingly
     * 
     * @throws IOException
     *             if the memory file was not found
     */
    public void processInput() throws IOException {
        Scanner scan;
        while (reader.hasNext()) {
            String temp = reader.nextLine().trim();
            scan = new Scanner(temp);
            if (scan.hasNext()) {
                String command = scan.next();
                String[] commands = temp.split("[ \t]");
                if (command.equals("insert")) {
                    String ID = scan.next();
                    int size = Integer.parseInt(scan.next());
                    String seq = reader.nextLine().substring(0, size);
                    processor.insert(ID, seq);
                }
                else if (command.equals("remove")) {
                    String ID = scan.next();
                    processor.remove(ID);
                }
                else if (command.equals("print")) {
                    processor.print();
                }
                else if (command.equals("search")) {
                    String ID = scan.next();
                    processor.search(ID);
                }
                else {
                    System.out.println("Command not recognized");
                }
            }
        }

    }
}
