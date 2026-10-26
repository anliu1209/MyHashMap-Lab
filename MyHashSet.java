public class MyHashSet<E> {
    private Object[] hashArray;
    private int size;

    public MyHashSet() {
        hashArray = new Object[1000];
        size = 0;
    }

    public boolean add(E element) {
        boolean status = !contains(element);
        if (status) {
            hashArray[size] = element;
            size += 1;
        }
        return status;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            hashArray[i] = null;
        }
        size = 0;
    }

    public boolean contains(Object obj) {
        // Get the hashcode
        int hc = obj.hashCode();
        
        for (int i = 0; i < size; i++) {
            // Check if the hashcode exists in every array location
            if (hashArray[i] != null && hashArray[i].hashCode() == hc) {
                // Check if the element is the same
                if (hashArray[i].equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean remove(Object obj) {
        for (int i = 0; i < size; i += 1) {
            if (hashArray[i] != null && hashArray[i].equals(obj)) {
                hashArray[i] = hashArray[size - 1];
                hashArray[size - 1] = null;
                size -= 1;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public DLList<E> toDLList() {
        DLList<E> newList = new DLList<E>();
        for (int i = 0; i < size; i++) {
            if (hashArray[i] != null) {
                newList.add((E) hashArray[i]);
            }
        }
        return newList;
    }
}