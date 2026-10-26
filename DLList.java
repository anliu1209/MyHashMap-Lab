// DLList.java
public class DLList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public DLList() {
        head = new Node<>(null);
        tail = new Node<>(null);
        head.setNext(tail);
        head.setPrev(null);
        tail.setNext(null);
        tail.setPrev(head);
        size = 0;
    }

    // boolean add(E data)
    public boolean add(E data) {
        Node<E> newNode = new Node<>(data);
        tail.prev().setNext(newNode);
        newNode.setPrev(tail.prev());
        tail.setPrev(newNode);
        newNode.setNext(tail);
        size+=1;
        return true;
    }

    // void add(int location, E data)
    public void add(int location, E data) {
        if (location == size) {
            add(data);
            return;
        }
        Node<E> newNode = new Node<>(data);
        Node<E> current = getNode(location);
        Node<E> prevNode = current.prev();

        newNode.setNext(current);
        newNode.setPrev(prevNode);
        prevNode.setNext(newNode);
        current.setPrev(newNode);
        size+=1;
    }

    // E get(int location)
    public E get(int location) {
        return getNode(location).get();
    }

    // boolean contains(Object element)
    public boolean contains(Object element) {
        // Start at first element, stop before tail
        Node<E> current = head.next();
        while (current != tail) {
            if ((element == null && current.get() == null) || 
                (element != null && element.equals(current.get()))) {
                return true;
            }
            current = current.next();
        }
        return false;
    }

    // remove(int location)
    public E remove(int location) {
        Node<E> current = getNode(location);
        E removedData = current.get();

        current.prev().setNext(current.next());
        current.next().setPrev(current.prev());

        size-=1;
        return removedData;
    }

    // boolean remove(Object element)
    public boolean remove(Object element) {
        // Start at first element, stop before tail
        Node<E> current = head.next();
        while (current != tail) {
            if ((element == null && current.get() == null) || 
                (element != null && element.equals(current.get()))) {
                
                current.prev().setNext(current.next());
                current.next().setPrev(current.prev());

                size-=1;
                return true;
            }
            current = current.next();
        }
        return false;
    }
    
    // E set(int location, E data)
    public E set(int location, E data) {
        Node<E> current = getNode(location);
        E oldData = current.get();
        current.setData(data);
        return oldData;
    }

    // int size()
    public int size() {
        return size;
    }

    // String toString()
    @Override
    public String toString() {
        String output = "";
        // Start at first element, stop before tail
        Node<E> current = head.next();
        while (current != tail) {
            output += current.get();
            if (current.next() != tail) {
                output += ", ";
            }
            current = current.next();
        }
        return output;
    }

    // Node<E> getNode(int index)
    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < size / 2) {
            // Start at the first node
            current = head.next();
            for (int i = 0; i < index; i+=1) {
                current = current.next();
            }
        } else {
            // Start at the last  node
            current = tail.prev();
            for (int i = size - 1; i > index; i-=1) {
                current = current.prev();
            }
        }
        return current;
    }
}