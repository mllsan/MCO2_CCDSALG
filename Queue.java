import java.util.LinkedList;

/**
 * Represents a queue data structure.
 * <p>
 *     This class implements the Queue data structure that follows a
 *     First-In First-Out Policy. It is of general Type and works with
 *     the reference data types.
 * </p>
 *
 * @author Raphael Romero
 * @author Michaela Santos
 * @version 3.4.0
 * @param <T> is the reference data type the LinkedList will be declared as.
 */
public class Queue<T> {
    private LinkedList<T> elements;

    public Queue() {
        elements = new LinkedList<T>();
    }

    public boolean isEmpty() {
        return elements.size() == 0;
    }

    public void enqueue(T elem) {
        elements.addLast(elem);
    }

    public T dequeue() {
        T dq = null;

        if (!elements.isEmpty())
            dq = elements.removeFirst();

        return dq;
    }

    public T getHead() {
        T head = null;

        if (!elements.isEmpty())
            head = elements.getFirst();

        return head;
    }

    public T getTail() {
        T tail = null;

        if (!elements.isEmpty())
            tail = elements.getLast();

        return tail;
    }

    public int getSize() {
        return elements.size();
    }
}