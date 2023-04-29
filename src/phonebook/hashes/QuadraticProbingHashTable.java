package phonebook.hashes;

import phonebook.exceptions.UnimplementedMethodException;
import phonebook.utils.KVPair;
import phonebook.utils.PrimeGenerator;
import phonebook.utils.Probes;

/**
 * <p>{@link QuadraticProbingHashTable} is an Openly Addressed {@link HashTable} which uses <b>Quadratic
 * Probing</b> as its collision resolution strategy. Quadratic Probing differs from <b>Linear</b> Probing
 * in that collisions are resolved by taking &quot; jumps &quot; on the hash table, the length of which
 * determined by an increasing polynomial factor. For example, during a key insertion which generates
 * several collisions, the first collision will be resolved by moving 1^2 + 1 = 2 positions over from
 * the originally hashed address (like Linear Probing), the second one will be resolved by moving
 * 2^2 + 2= 6 positions over from our hashed address, the third one by moving 3^2 + 3 = 12 positions over, etc.
 * </p>
 *
 * <p>By using this collision resolution technique, {@link QuadraticProbingHashTable} aims to get rid of the
 * &quot;key clustering &quot; problem that {@link LinearProbingHashTable} suffers from. Leaving more
 * space in between memory probes allows other keys to be inserted without many collisions. The tradeoff
 * is that, in doing so, {@link QuadraticProbingHashTable} sacrifices <em>cache locality</em>.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see OrderedLinearProbingHashTable
 * @see LinearProbingHashTable
 * @see CollisionResolver
 */
public class QuadraticProbingHashTable extends OpenAddressingHashTable {

    /* ********************************************************************/
    /* ** INSERT ANY PRIVATE METHODS OR FIELDS YOU WANT TO USE HERE: ******/
    /* ********************************************************************/

	private Probes resize() {
		QuadraticProbingHashTable newTable = new QuadraticProbingHashTable(softFlag); 
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
	
	private Probes reinsert() {
		QuadraticProbingHashTable newTable = new QuadraticProbingHashTable(softFlag); 
		newTable.table = new KVPair[primeGenerator.getCurrPrime()];
		
		int probes = 0;
		
		for(int i = 0; i < table.length; i++) {
			if (table[i] != null && table[i] != TOMBSTONE) {
				KVPair pair = table[i];
				probes += newTable.put(pair.getKey(), pair.getValue()).getProbes();
			}
			
			probes++;
		}
		
		//probes++;
		
		this.table = newTable.table;
		this.count = newTable.count;
		this.occCells = newTable.occCells;
		
		return new Probes(null, probes);
	}
	
    /* ******************************************/
    /*  IMPLEMENT THE FOLLOWING PUBLIC METHODS: */
    /* **************************************** */

    /**
     * Constructor with soft deletion option. Initializes the internal storage with a size equal to the starting value of  {@link PrimeGenerator}.
     * @param soft A boolean indicator of whether we want to use soft deletion or not. {@code true} if and only if
     *               we want soft deletion, {@code false} otherwise.
     */
    public QuadraticProbingHashTable(boolean soft) {
    	primeGenerator = new PrimeGenerator();
    	table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
        occCells = 0;
        softFlag = soft;
    }

    @Override
    public Probes put(String key, String value) {
    	System.out.println(this.toString());
    	System.out.println("Inserting: " + key);
    	if (key.equals(null) == true || value.equals(null) == true) {
        	throw new IllegalArgumentException();
        }
    	
    	int probes = 0;
    	double cap = (double)occCells / table.length;
    	
    	if (cap > 0.50) {
    		probes += resize().getProbes();
    	}
        
    	int index = hash(key) % table.length;
    	//System.out.println(index);
    	int i = 2;
        
        if (table[index] == null || table[index] == TOMBSTONE) {
        	table[index] = new KVPair(key, value);
        	probes++;
        	count++;
        	occCells++;
        	
        	System.out.println("Probes: " + probes);
        	return new Probes(value, probes);
        }
        
    	while (!(table[index] == null) && !(table[index] == TOMBSTONE)) {
    		System.out.println(index);
    		index = (hash(key) + (i - 1) + ((i - 1) * (i - 1))) % table.length;
    		
    		if (index >= table.length) {
    			index = index - table.length;
    		}
    		
    		probes++;
    		i++;
    	}
    	
    	table[index] = new KVPair(key, value);
    	
    	probes++;
    	count++;
    	occCells++;
        
    	System.out.println("Probes: " + probes);
        return new Probes(value, probes);
    }


    @Override
    public Probes get(String key) {
    	int index = hash(key) % table.length;
        int probes = 0;
        int i = 2;
        String value = null;
        
        while ((!(table[index] == null) && !(table[index] == TOMBSTONE)) && !table[index].getKey().equals(key)) {
        	index = (hash(key) + (i - 1) + ((i - 1) * (i - 1))) % table.length;
        	
        	if (index >= table.length) {
        		index = index - table.length;
        	}
        	
        	probes++;
        	i++;
        }
        
        probes++;
        
        if (table[index] != null && table[index] != null) {
        	if (table[index].getKey().equals(key)) {
            	value = table[index].getValue();
            }
        }
        System.out.println("Probes: " + probes);
        return new Probes(value, probes);
    }

    @Override
    public Probes remove(String key) {
    	System.out.println(this.toString());
    	System.out.println("Removing: " + key);
    	if (key == null) {
    		return new Probes(null, 0);
    	}
    	
    	int index = hash(key) % table.length;
    	int probes = 0;
    	int i = 2;
    	String value = null;
    	
    	while ((!(table[index] == null)) && !table[index].getKey().equals(key)) {
    		System.out.println(index);
    		index = (hash(key) + (i - 1) + ((i - 1) * (i - 1))) % table.length;
    		
    		if (index >= table.length) {
        		index = index - table.length;
        	}
    		
    		probes++;
    		i++;
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
    			
    			probes += reinsert().getProbes();
    		}
    	} else {
    		System.out.println("Probes: " + probes);
    		return new Probes(value, probes);
    	}
    	System.out.println("Probes: " + probes);
    	return new Probes(value, probes);
    }


    @Override
    public boolean containsKey(String key) {
    	int index = hash(key) % table.length;
    	int i = 2;
    	
    	while ((!(table[index] == null) && !(table[index] == TOMBSTONE)) && !table[index].getKey().equals(key)) {
    		index = (hash(key) + (i - 1) + ((i - 1) * (i - 1))) % table.length;
    		
    		if (index >= table.length) {
        		index = index - table.length;
        	}
    		
    		i++;
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
    public int size(){
        return count;
    }

    @Override
    public int capacity() {
        return table.length;
    }

}