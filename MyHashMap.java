public class MyHashMap<K, V> {
    private final Object[] hashArray;
    private int size;
    private final MyHashSet<K> keySet;

    private static class Entry<K, V> {
        private final K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        hashArray = new Object[100];
        size = 0;
        keySet = new MyHashSet<>();
    }

    public V put(K key, V value) {
        int index = bucketIndex(key);
        DLList<Entry<K, V>> bucket = getBucket(index);
        if (bucket == null) {
            bucket = new DLList<>();
            hashArray[index] = bucket;
        }

        int position = findKey(bucket, key);
        if (position >= 0) {
            Entry<K, V> entry = bucket.get(position);
            V previous = entry.value;
            entry.value = value;
            return previous;
        }

        keySet.add(key);
        bucket.add(new Entry<>(key, value));
        size+=1;
        return null;
    }

    public V get(Object key) {
        DLList<Entry<K, V>> bucket = getBucket(bucketIndex(key));
        int position = findKey(bucket, key);
        return position < 0 ? null : bucket.get(position).value;
    }

    public V remove(Object key) {
        DLList<Entry<K, V>> bucket = getBucket(bucketIndex(key));
        int position = findKey(bucket, key);
        if (position < 0) {
            return null;
        }

        keySet.remove(key);
        size-=1;
        return bucket.remove(position).value;
    }

    private int bucketIndex(Object key) {
        return Math.floorMod(key.hashCode(), hashArray.length);
    }

    @SuppressWarnings("unchecked")
    private DLList<Entry<K, V>> getBucket(int index) {
        return (DLList<Entry<K, V>>) hashArray[index];
    }

    private int findKey(DLList<Entry<K, V>> bucket, Object key) {
        if (bucket != null) {
            for (int i = 0; i < bucket.size(); i++) {
                if (key.equals(bucket.get(i).key)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int size() {
        return size;
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }
}
