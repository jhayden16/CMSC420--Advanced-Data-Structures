package phonebook.hashes;

import java.util.ArrayList;
import java.util.Collections;

import phonebook.exceptions.UnimplementedMethodException;
import phonebook.utils.PrimeGenerator;
import phonebook.utils.Probes;
import phonebook.utils.KVPair;

/**
 * <p>{@link LinearProbingHashTable} is an Openly Addressed {@link HashTable} implemented with <b>Linear Probing</b> as its
 * collision resolution strategy: every key collision is resolved by moving one address over. It is
 * the most famous collision resolution strategy, praised for its simplicity, theoretical properties
 * and cache locality. It <b>does</b>, however, suffer from the &quot; clustering &quot; problem:
 * collision resolutions tend to cluster collision chains locally, making it hard for new keys to be
 * inserted without collisions. {@link QuadraticProbingHashTable} is a {@link HashTable} that
 * tries to avoid this problem, albeit sacrificing cache locality.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see OrderedLinearProbingHashTable
 * @see QuadraticProbingHashTable
 * @see CollisionResolver
 */
public class LinearProbingHashTable extends OpenAddressingHashTable {

    /* ********************************************************************/
    /* ** INSERT ANY PRIVATE METHODS OR FIELDS YOU WANT TO USE HERE: ******/
    /* ********************************************************************/

	private Probes resize() {
		LinearProbingHashTable newTable = new LinearProbingHashTable(softFlag); 
		newTable.table = new KVPair[primeGenerator.getNextPrime()];
		
		int probes = 0;
		
		for(int i = 0; i < table.length; i++) {
			if (table[i] != null && table[i] != TOMBSTONE) {
				KVPair pair = table[i];
				probes += newTable.put(pair.getKey(), pair.getValue()).getProbes();
			}
			
			probes++;
		}
		
		this.table = newTable.table;
		this.count = newTable.count;
		this.occCells = newTable.occCells;
		
		return new Probes(null, probes);
	}
	
	private Probes reinsert(int index) {
		System.out.println("Reinserting:");
		ArrayList<KVPair> temp = new ArrayList<KVPair>();
		int probes = 0;
		while (table[index] != null && table[index] != TOMBSTONE) {
			temp.add(table[index]);
			System.out.println(table[index].getKey());
			table[index] = null;
			
			probes++;
			index++;
			count--;
			
			if (index >= table.length) {
				index = 0;
			}
		}
		
		probes++;
		
		for(KVPair pair : temp) {
			probes += this.put(pair.getKey(), pair.getValue()).getProbes();
		}
		
		return new Probes(null, probes);
	}
	
    /* ******************************************/
    /*  IMPLEMENT THE FOLLOWING PUBLIC METHODS: */
    /* **************************************** */

    /**
     * Constructor with soft deletion option. Initializes the internal storage with a size equal to the starting value of  {@link PrimeGenerator}.
     *
     * @param soft A boolean indicator of whether we want to use soft deletion or not. {@code true} if and only if
     *             we want soft deletion, {@code false} otherwise.
     */
    public LinearProbingHashTable(boolean soft) {
    	primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
        occCells = 0;
        softFlag = soft;
    }

    /**
     * Inserts the pair &lt;key, value&gt; into this. The container should <b>not</b> allow for {@code null}
     * keys and values, and we <b>will</b> test if you are throwing a {@link IllegalArgumentException} from your code
     * if this method is given {@code null} arguments! It is important that we establish that no {@code null} entries
     * can exist in our database because the semantics of {@link #get(String)} and {@link #remove(String)} are that they
     * return {@code null} if, and only if, their key parameter is {@code null}. This method is expected to run in <em>amortized
     * constant time</em>.
     * <p>
     * Instances of {@link LinearProbingHashTable} will follow the writeup's guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     *
     * @param key   The record's key.
     * @param value The record's value.
     * @return The {@link phonebook.utils.Probes} with the value added and the number of probes it makes.
     * @throws IllegalArgumentException if either argument is {@code null}.
     */
    @Override
    public Probes put(String key, String value) {
    	if (key.equals(null) == true || value.equals(null) == true) {
        	throw new IllegalArgumentException();
        }
    	
    	int probes = 0;
    	double cap = (double)occCells / table.length;
    	
    	//System.out.println(this.toString());
    	//System.out.println("Inserting: " + key);
    	
    	if (cap > 0.50) {
    		probes += resize().getProbes();
    	}
        
    	int index = hash(key) % table.length;
    	//System.out.println(index);
        
        if (table[index] == null || table[index] == TOMBSTONE) {
        	table[index] = new KVPair(key, value);
        	probes++;
        	count++;
        	occCells++;
        	
        	return new Probes(value, probes);
        }
        
    	while (!(table[index] == null)) {
    		index++;
    		
    		if (index == table.length) {
    			index = 0;
    		}
    		
    		probes++;
    	}
    	
    	table[index] = new KVPair(key, value);
    	
    	probes++;
    	count++;
    	occCells++;
    	
    	//System.out.println("After insertion:\n");
    	//System.out.println(this.toString());
        
        return new Probes(value, probes);
    }

    @Override
    public Probes get(String key) {
    	int index = hash(key) % table.length;
        int probes = 0; 
        String value = null;
        
        while ((!(table[index] == null) && !(table[index] == TOMBSTONE)) && !table[index].getKey().equals(key)) {
        	index++;
        	
        	if (index == table.length) {
        		index = 0;
        	}
        	
        	probes++;
        }
        
        if (table[index] != null) {
        	if (table[index].getKey().equals(key)) {
            	value = table[index].getValue();
            }
        }
        
        probes++;
        
        return new Probes(value, probes);
    }


    /**
     * <b>Return</b> the value associated with key in the {@link HashTable}, and <b>remove</b> the {@link phonebook.utils.KVPair} from the table.
     * If key does not exist in the database
     * or if key = {@code null}, this method returns {@code null}. This method is expected to run in <em>amortized constant time</em>.
     *
     * @param key The key to search for.
     * @return The {@link phonebook.utils.Probes} with associated value and the number of probe used. If the key is {@code null}, return value {@code null}
     * and 0 as number of probes; if the key doesn't exist in the database, return {@code null} and the number of probes used.
     */
    @Override
    public Probes remove(String key) {
    	System.out.println(this.toString());
    	System.out.println("Removing: " + key);
    	if (key == null) {
    		return new Probes(null, 0);
    	}
    	
    	int index = hash(key) % table.length;
    	//System.out.println(index);
    	int probes = 0;
    	String value = null;
    	
    	while ((!(table[index] == null)) && !table[index].getKey().equals(key)) {
    		index++;
    		//System.out.println(index);
    		
    		if (index == table.length) {
    			index = 0;
    		}
    		
    		probes++;
    	}
    	
    	probes++;
    	
    	if (table[index] != null && table[index].getKey().equals(key)) {
    		value = table[index].getValue();
    		if (softFlag) {
    			table[index] = TOMBSTONE;
    			count--;
    		} else {
    			table[index] = null;
    			count--;
    			occCells--;
    			
    			if (index == table.length - 1) {
    				probes += reinsert(0).getProbes();
    			} else {
    				probes += reinsert(index + 1).getProbes();
    			}
    		}
    		System.out.println("After deletion:\n");
    		System.out.println(this.toString());
    	} else {
    		return new Probes(value, probes);
    	}
    	
    	return new Probes(value, probes);
    }

    @Override
    public boolean containsKey(String key) {
    	int index = hash(key) % table.length;
    	
    	while ((!(table[index] == null) && !(table[index] == TOMBSTONE)) && !table[index].getKey().equals(key)) {
    		index++;
    		
			if (index == table.length) {
				index = 0;
			}
    	}
    	
    	if (table[index] != null && table[index].getKey().equals(key)) {
    		return true;
    	} else {
    		return false;
    	}
        
    }

    @Override
    public boolean containsValue(String value) {
        for (int i = 0; i < table.length; i++) {
        	if (table[i] != null || table[i] != TOMBSTONE) {
        		if (table[i].getValue().equals(value)) {
        			return true;
        		}
        	}
        }
        
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return table.length;
    }
}
