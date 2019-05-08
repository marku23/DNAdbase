
// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.


import java.io.*;

/**
 * The main class for the program, which takes in arguments
 * from the command line and uses them to run the program.
 * 
 * @author marku23
 * @author ccox17
 * @version 5/4/19
 *
 */
public class DNAdbase {

    /**
     * The main method for the program
     * 
     * @param args
     *            - arguments from command line
     * @throws IOException
     *             if any of the files could not be found
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            throw new IllegalArgumentException(
                "Please specify the program arguments. "
                    + "Invoke as: <command-file> <hash-file>\\r\\n " + 
                        "<hash-table-size <memory-file>");
        }
        else {
            int tableSize = Integer.parseInt(args[2]);
            FileReader reader = 
                    new FileReader(args[0], args[1], args[3], tableSize);
            reader.processInput();
        }

    }

}
