/**
 * Stores the memory handles
 * 
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 *
 */

public class DNARecord {
    // Variables...............................................................
    
    private int iDOffset;
    private int iDLength;
    private int seqOffset;
    private int seqLength;
    
    // Constructors............................................................

    /**
     * Creates a DNARecord object
     * 
     * @param off1
     *      iDOffset
     * @param len1
     *      iDLength
     * @param off2
     *      seqOffset
     * @param len2
     *      seqLength
     */
    
    public DNARecord(int off1, int len1, int off2, int len2) {
        iDOffset = off1;
        seqOffset = off2;
        iDLength = len1;
        seqLength = len2;
    }


    // Methods.................................................................

    /**
     * getter method.
     * 
     * @return
     *         The offset of the SequenceID in the memory manager.
     */

    public int getIDOffset() {
        return iDOffset;
    }


    /**
     * getter method.
     * 
     * @return
     *         The offset of the Sequence in the binary file.
     */

    public int getSeqOffset() {
        return seqOffset;
    }


    /**
     * getter method.
     * 
     * @return
     *         The length of the SequenceID in the memory manager.
     */

    public int getIDLength() {
        return iDLength;
    }


    /**
     * Changes the IDLength of this DNARecord. This is used for setting
     * our condition for a tombstone: if the ID length is negative, then this
     * is a tombstone.
     * 
     * @param newLength
     *            - the new length of the ID of this sequence
     */
    public void setIDLength(int newLength)
    {
        iDLength = newLength;
    }


    /**
     * getter method.
     * 
     * @return
     *         The length of the Sequence in the binary file.
     */

    public int getSeqLength() {
        return seqLength;
    }

}
