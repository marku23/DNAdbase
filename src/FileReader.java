
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
     * @throws FileNotFoundException
     *             - if one of the files could not be found
     */
    public FileReader(
        File inputFile,
        RandomAccessFile hashFile,
        RandomAccessFile memoryFile, int tableSize)
        throws FileNotFoundException {
        input = inputFile;
        hash = hashFile;
        memory = memoryFile;
        reader = new Scanner(input);
        processor = new DataProcessor(memory, new DNAHashTable(tableSize));
    }


    /**
     * Reads through the input file and calls methods to process
     * the commands accordingly
     * @throws IOException if the memory file was not found
     */
    public void processInput() throws IOException {
        while (reader.hasNext()) {
            String temp = reader.nextLine().trim();
            String[] commands = temp.split(" \t\n");
            if (commands[0].equals("insert")) {
                String ID = commands[1];
                int seqLength = Integer.parseInt(commands[2]);
                String seq = reader.nextLine();
                processor.insert(ID, seq, seqLength);
            }
            else if (commands[0].equals("remove")) {
                String ID = commands[1];
                processor.remove(ID);
            }
            else if (commands[0].equals("print")) {
                processor.print();
            }
            else if (commands[0].equals("search")) {
                String ID = commands[1];
                processor.search(ID);
            }
            else {
                System.out.println("Command not recognized");
            }
        }

    }
}
