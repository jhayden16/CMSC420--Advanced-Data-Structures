package bpt;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Iterator;

/**
 * A jUnit test suite for {@link BinaryPatriciaTrie}.
 *
 * @author --- YOUR NAME HERE! ----.
 */
public class StudentTests {


    @Test public void testEmptyTrie() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("Trie should be empty",trie.isEmpty());
        assertEquals("Trie size should be 0", 0, trie.getSize());

        assertFalse("No string inserted so search should fail", trie.search("0101"));

    }

    @Test public void testFewInsertionsWithSearch() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("String should be inserted successfully",trie.insert("00000"));
        assertTrue("String should be inserted successfully",trie.insert("00011"));
        assertFalse("Search should fail as string does not exist",trie.search("000"));

    }


    //testing isEmpty function
    @Test public void testFewInsertionsWithDeletion() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        trie.insert("000");
        trie.insert("001");
        trie.insert("011");
        trie.insert("1001");
        trie.insert("1");

        assertFalse("After inserting five strings, the trie should not be considered empty!", trie.isEmpty());
        assertEquals("After inserting five strings, the trie should report five strings stored.", 5, trie.getSize());

        trie.delete("0"); // Failed deletion; should affect exactly nothing.
        assertEquals("After inserting five strings and requesting the deletion of one not in the trie, the trie " +
                "should report five strings stored.", 5, trie.getSize());
        assertTrue("After inserting five strings and requesting the deletion of one not in the trie, the trie had some junk in it!",
                trie.isJunkFree());

        trie.delete("011"); // Successful deletion
        assertEquals("After inserting five strings and deleting one of them, the trie should report 4 strings.", 4, trie.getSize());
        assertTrue("After inserting five strings and deleting one of them, the trie had some junk in it!",
                trie.isJunkFree());
    }
    
    @Test public void student() {
    	BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
    	
    	trie.insert("000001");
    	trie.insert("010010");
    	trie.insert("010111");
    	trie.insert("011011");
    	trie.insert("011101");
    	trie.insert("011111");
    	trie.insert("100100");
    	trie.insert("110001");
    	trie.insert("110110");
    	
    	Iterator<String> iter1 = trie.inorderTraversal();
    	
    	/*trie.insert("0101");
    	trie.insert("0101");
    	trie.insert("1101");
    	
    	Iterator<String> iter1 = trie.inorderTraversal();
    	
    	trie.insert("11");
    	trie.insert("0");
    	
    	Iterator<String> iter2 = trie.inorderTraversal();
    	
    	for (int i = 0; i < trie.getSize(); i++) {
    		if (iter2.hasNext()) {
    			System.out.println(iter2.next());
    		}
    	}*/
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
}