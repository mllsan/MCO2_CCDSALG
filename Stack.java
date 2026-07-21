import java.util.ArrayList;

/**
 * Represents a stack data structure.
 * <p>
 *     This class implements the Stack data structure that follows a
 *     Last-In First-Out Policy. It is of general Type and works with
 *     the reference data types.
 * </p>
 *
 * @author Raphael Romero
 * @author Michaela Santos
 * @version 3.4.0
 * @param <T> is the reference data type the ArrayList will be declared as.
 */
public class Stack<T> {
    private ArrayList<T> elements;

    public Stack() {
        elements = new ArrayList<T>();
    }

    public boolean isEmpty() {
        return elements.size() == 0;
    }

    public void push(T elem) {
        elements.add(elem);
    }

    public T pop() {
        T popped = null;

        if(!elements.isEmpty())
            popped = elements.remove(elements.size() - 1);

        return popped;
    }

    public T top() {
        T top = null;

        if(!elements.isEmpty())
            top = elements.get(elements.size() - 1);

        return top;
    }

    public int getSize() {
        return elements.size();
    }
}
