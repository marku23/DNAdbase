/**
 * 
 * Interface with methods for hashing.
 * 
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 *
 * @param <T>
 *            The type of data to be stored in the hash table.
 */
public interface HashTable<T> {

    /**
     * Attempts to insert a value into the hash table.
     * 
     * @param key
     *            The key for the entry to be inserted.
     * @param value
     *            The value to be inserted.
     * @return
     *         True if insertion was successful, false otherwise.
     */

    public boolean insert(String key, T value);


    /**
     * Attempts to remove a value from the hash table.
     * 
     * @param key
     *            the key to search for the value by.
     * @return
     *         true if removal was successful, false otherwise.
     */

    public boolean remove(String key);


    /**
     * Searches for a value in the table.
     * 
     * @param key
     *            specifies what to search for
     * @return
     *         the requested item if it is in the table, or null otherwise.
     */

    public T search(String key);


    /**
     * Creates a string representation of the table.
     * 
     * @return
     *         a String Representation of the table.
     */

    public String toString();

}
