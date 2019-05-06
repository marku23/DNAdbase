import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * Keeps track of free space in memory and handles requests to memory.
 *  
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 */

public class MemoryManager {
    // Variables...............................................................
    //private LinkedList
    private LinkedList<String> duplicateList;
    private LinkedList<FreeBlock> freeBlocks; //should this be int?
    private RandomAccessFile binFile;
    
    public MemoryManager(RandomAccessFile binFile)
    {
        duplicateList = new LinkedList<String>();
        freeBlocks = new LinkedList<FreeBlock>();
        this.binFile = binFile;
    }
    
    public DNARecord insert(String ID, String sequence)
    {
        for (int i = 0; i < duplicateList.size(); i++)
        {
            if (ID.equals(duplicateList.get(i)))
            {
                return null;
            }
        }
        duplicateList.add(ID);
        //add data to bin file
        //update freeBlocks list
        return true;
    }
    
    public String getID(DNARecord object) {
        
    }
}
