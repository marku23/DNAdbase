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
    private DNAHashTable table;
    private LinkedList<String> duplicateList;
    private LinkedList<Integer> freeBlocks; //should this be int?
    private RandomAccessFile binFile;
    
    public MemoryManager(RandomAccessFile binFile)
    {
        duplicateList = new LinkedList<String>();
        freeBlocks = new LinkedList<Integer>();
        this.binFile = binFile;
    }
    
    public boolean insert(String ID, DNARecord record)
    {
        for (int i = 0; i < duplicateList.size(); i++)
        {
            if (ID.equals(duplicateList.get(i)))
            {
                return false;
            }
        }
        duplicateList.add(ID);
        table.insert(ID, record);
        //add data to bin file
        //update freeBlocks list
        return true;
    }
}
