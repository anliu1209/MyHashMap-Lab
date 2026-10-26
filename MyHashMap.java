public class MyHashMap<K, V> {
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;

    public MyHashMap() {
        hashArray = new Object[100];
        size = 0;
        keySet = new MyHashSet<>();
    }

    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        int index = key.hashCode() % hashArray.length;
        if (index < 0) {
            index += hashArray.length;
        }
        V previous = null;
        if (keySet.contains(key)) {
            previous = (V) hashArray[index];
        }
        if (keySet.add(key)) {
            size++;
        }
        hashArray[index] = value;
        return previous;
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
        int index = key.hashCode() % hashArray.length;
        if (index < 0) {
            index += hashArray.length;
        }
        return (V) hashArray[index];
    }

    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        if (!keySet.remove(key)) {
            return null;
        }
        int index = key.hashCode() % hashArray.length;
        if (index < 0) {
            index += hashArray.length;
        }
        V removed = (V) hashArray[index];
        hashArray[index] = null;
        size--;
        return removed;
    }

    public int size() {
        return size;
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }
}
