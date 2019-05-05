/**
 * Stores the memory handles
 * 
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 *
 */

public class DNARecord {
    // Variables...............................................................
    
    private int IDOffset;
    private int IDLength;
    private int SeqOffset;
    private int SeqLength;
    
    // Constructors............................................................
    
    /**
     * Creates a DNARecord object 
     * @param off1
     *      IDOffset
     * @param len1
     *      IDLength
     * @param off2
     *      SeqOffset
     * @param len2
     *      SeqLength
     */
    
    public DNARecord (int off1, int len1, int off2, int len2) {
        IDOffset = off1;
        SeqOffset = off2;
        IDLength = len1;
        SeqLength = len2;
    }
    
    
    // Methods.................................................................
    
    /**
     * getter method. 
     * @return
     *      The offset of the SequenceID in the memory manager.
     */
    
    public int getIDOffset() {
        return IDOffset;
    }
    
    
    /**
     * getter method. 
     * @return
     *      The offset of the Sequence in the binary file.
     */
    
    public int getSeqOffset() {
        return SeqOffset;
    }
    
    
    /**
     * getter method. 
     * @return
     *      The length of the SequenceID in the memory manager.
     */
    
    public int getIDLength() {
        return IDLength;
    }
    
    
    /**
     * getter method. 
     * @return
     *      The length of the Sequence in the binary file.
     */
    
    public int getSeqLength() {
        return SeqLength;
    }

}
