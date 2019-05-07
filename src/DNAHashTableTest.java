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
        assertEquals(74, table.remove("AAAAAAAA"));
    }
    
    /**
     * Tests the search method to make sure it searches for objects
     * as intended
     */
    public void testSearch()
    {
        DNARecord temp = new DNARecord(0, 8, 8, 8);
        table.insert("AAAAAAAA", temp);
        assertEquals(74, table.search("AAAAAAAA"));
        assertEquals(17, table.search("C"));
    }
    
    /**
     * Tests the print method to make sure it prints sequences
     * as intended
     */
    public void testMakeTombstone()
    {
        DNARecord temp = new DNARecord(0, 8, 8, 8);
        table.insert("AAAAAAAA", temp);
        table.makeTombstone(74);
        assertEquals(-1, table.getHandleAtOffset(74).getIDLength());
    }
    
    /**
     * Tests the getTable method to ensure it returns a table object
     * properly
     */
    public void testGetTable()
    {
        assertNotNull(table.getTable());
    }

    /**
     * Tests the getHandleAtOffset method to ensure it returns the
     * proper DNARecord object at a certain offset
     */
    public void testGetHandleAtOffset()
    {
        DNARecord temp = new DNARecord(0, 8, 8, 8);
        table.insert("AAAAAAAA", temp);
        table.getHandleAtOffset(74);
        assertEquals(temp, table.getHandleAtOffset(74));   
    }
}
