
/**
 * Data type to represent the free spaces in memory.
 * 
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 *
 */
public class FreeBlock implements Comparable<FreeBlock> {
    // Variables...............................................................

    private int offset;
    private int size;


    // Constructors............................................................

    /**
     * Creates a new FreeBlock Object.
     * 
     * @param off
     *            the offset in memory of the block
     * @param s
     *            the size of the block
     */

    public FreeBlock(int off, int s) {
        offset = off;
        size = s;
    }


    // Methods.................................................................

    /**
     * getter method.
     * 
     * @return
     *         the offset in memory of the free block.
     */

    public int getOffset() {
        return offset;
    }


    /**
     * getter method.
     * 
     * @return
     *         The size of the free block.
     */

    public int getSize() {
        return size;
    }


    @Override
    public int compareTo(FreeBlock otherBlock) {
        if (offset > otherBlock.getOffset()) {
            return 1;
        }
        else if (offset < otherBlock.getOffset()) {
            return -1;
        }
        return 0;
    }
}
