package mk.finki.ukim.ispit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}



class CBHT<K extends Comparable<K>, E> {

    // An object of class CBHT is a closed-bucket hash table, containing
    // entries of class MapEntry.
    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        // Construct an empty CBHT with m buckets.
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
        // Find which if any node of this CBHT contains an entry whose key is
        // equal
        // to targetKey. Return a link to that node (or null if there is none).
        int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {		// Insert the entry <key, val> into this CBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }
        // Insert newEntry at the front of the 1WLL in bucket b ...
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}
//class Pacient{
//    String ime;
//    int poz;
//
//    public Pacient(String ime, int poz) {
//        this.ime = ime;
//        this.poz = poz;
//    }
//
//}

public class CoronaRiskFactor {
    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bf.readLine());
        CBHT<String, String> tabela = new CBHT<>(N*2-1);
//        ArrayList<Integer> list = new ArrayList<>();

        for(int i = 0; i<N; i++){
            String p []= bf.readLine().split(" ");

            String opstina = p[0];
            String prezime = p[1];
            String rezultat = p[2];
            SLLNode<MapEntry<String ,String>> node = tabela.search(opstina);

            if (node!=null){
                String value="";
                value = node.element.value;
                String parts[] = value.split(",");
                int pozitivni = Integer.parseInt(parts[0]);
                int vkupno = Integer.parseInt(parts[1]);
                if (rezultat.compareTo("pozitiven")==0){
                    pozitivni++;
                    vkupno++;
                    value=pozitivni+","+vkupno;
                    tabela.insert(opstina,value);
                }else {
                    vkupno++;
                    value=pozitivni+","+vkupno;
                    tabela.insert(opstina,value);
                }
            }
            else {
                if (rezultat.compareTo("pozitiven")==0){
                    String poz = "1,1";
                    tabela.insert(opstina,poz);
                }else {
                    String poz = "0,1";
                    tabela.insert(opstina,poz);
                }
            }
        }

        String opstina = bf.readLine();
        SLLNode<MapEntry<String,String>> node = tabela.search(opstina);
//        System.out.println(node.element.value);


            String parts[] = node.element.value.split(",");
            float poz = Float.parseFloat(parts[0]);
            float vkupno = Float.parseFloat(parts[1]);
            float riskFactor = poz/vkupno;


        System.out.println(String.format("%.2f", riskFactor));
    }


}
