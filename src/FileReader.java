import java.util.*;
import java.io.*;

/**
 * A java class that reads in commands from the input file and
 * calls methods to process them accordingly by using the hash
 * file and the memory file.
 * 
 * @author marku23
 * @author ccox17
 *
 */
public class FileReader {

    private DataProcessor processor;
    private Scanner reader;
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
     * @param tableSize - the size of the hash table
     * @throws IOException 
     */
    public FileReader(
        String inputFile,
        String hashFile,
        String memoryFile,
        int tableSize)
        throws IOException {
        hash = new RandomAccessFile(hashFile, "rw");
        memory = new RandomAccessFile(memoryFile, "rw");
        memory.setLength(0);
        reader = new Scanner(inputFile);
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
        while (reader.hasNext()) {
            String temp = reader.nextLine().trim();
            String[] commands = temp.split("[ \t]");
            if (commands[0].equals("insert")) {
                String iD = commands[1];
                String seq = reader.nextLine();
                processor.insert(iD, seq);
            }
            else if (commands[0].equals("remove")) {
                String iD = commands[1];
                processor.remove(iD);
            }
            else if (commands[0].equals("print")) {
                processor.print();
            }
            else if (commands[0].equals("search")) {
                String iD = commands[1];
                processor.search(iD);
            }
            else {
                if (temp.length() != 0) {
                    System.out.println("Command not recognized");
                }
            }
        }

    }
}
