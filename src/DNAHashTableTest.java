/**
 * A class to test the methods of the DNAHashTable object
 * @author marku23
 * @author ccox17
 * @version 5.5.19
 *
 */


public class DNAHashTableTest extends student.TestCase {

    private DNAHashTable table;
    
    /**
     * Sets up initial conditions
     */
    public void setUp()
    {
        table = new DNAHashTable(128);
    }
    
    /**
     * Tests the insert method to make sure it inserts objects
     * as intended
     */
    public void testInsert()
    {
        DNARecord temp = new DNARecord(0, 8, 8, 8);
        table.insert("AAAAAAAA", temp);
        System.out.println(table.sfold("AAAAAAAA", 128));
        assertNotNull(table.getTable()[74]);
        table.insert("AAAAAAAA", temp);
        assertNotNull(table.getTable()[75]);
    }
    
    /**
     * Tests the remove method to make sure it removes objects
     * as intended
     */
    public void testRemove()
    {
        DNARecord temp = new DNARecord(0, 8, 8, 8);
        table.insert("AAAAAAAA", temp);
        assertNotNull(table.getTable()[74]);
        table.remove("AAAAAAAA");
        assertNull(table.getTable()[74]);
    }
    
    /**
     * Tests the search method to make sure it searches for objects
     * as intended
     */
    public void testSearch()
    {
        DNARecord temp = new DNARecord(0, 8, 8, 8);
        table.insert("AAAAAAAA", temp);
        assertEquals(temp, table.search("AAAAAAAA"));
        assertNull(table.search("i wanna fuckin drop out lol"));
    }
    
    
}
