import java.util.ArrayList;

/**
 * Manages unvisited maze nodes by dynamically sorting using lowest fcost.
 */
public class PriorityQueue {
    private ArrayList<Cell> list;

    /**
     * Construct empty PriorityQueue.
     */
    public PriorityQueue() {
        list = new ArrayList<Cell>();
    }

    /**
     * Insert Cell into the priority queue and maintain the heap property.
     * 
     * @param cell the Cell to be added
     */
    public void enqueue(Cell cell) {
        list.add(cell);
        heapifyUp(list.size() - 1);
    }

    /**
     * Removes the Cell with the highest priority (lowest fcost).
     * 
     * @return the Cell with the lowest cost or null if empty
     */
    public Cell dequeue() {
        Cell minCell = null;

        if (!isEmpty()) {
            minCell = list.get(0);
            Cell lastCell = list.remove(list.size() - 1);
            if (!isEmpty()) {
                list.set(0, lastCell);
                heapifyDown(0);
            }
        }

        return minCell;
    }

    /**
     * Return highest priority Cell without removing it.
     * 
     * @return the Cell with the minimum cost, or null if empty
     */
    public Cell viewNext() {
        Cell nextCell = null;

        if (!isEmpty()) {
            nextCell = list.get(0);
        }

        return nextCell;
    }

    /**
     * Checks if the priority queue is empty.
     * 
     * @return true if empty and false if not
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Checks whether a specific cell is in the priority queue.
     * 
     * @param cell the Cell to search for
     * @return true if the cell's coordinates are found in the queue, otherwise false
     */
    public boolean inQueue(Cell cell) {
        boolean found = false;

        if (cell != null) {
            for (int i = 0; i < list.size() && !found; i++) {
                Cell c = list.get(i);
                if (c.getRow() == cell.getRow() && c.getCol() == cell.getCol()) {
                    found = true;
                }
            }
        }

        return found;
    }

    /**
     * Returns the number of elements in the priority queue.
     * 
     * @return size of the queue
     */
    public int size() {
        return list.size();
    }

    // Helpers 

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (list.get(index).compareTo(list.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } 
            else {
                index = -1;
            }
        }
    }

    private void heapifyDown(int index) {
        int size = list.size();
        boolean finish = false;

        while (!finish) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int lowest = index;

            if (leftChild < size && list.get(leftChild).compareTo(list.get(lowest)) < 0) {
                lowest = leftChild;
            }

            if (rightChild < size && list.get(rightChild).compareTo(list.get(lowest)) < 0) {
                lowest = rightChild;
            }

            if (lowest != index) {
                swap(index, lowest);
                index = lowest;
            } 
            else {
                finish = true;
            }
        }
    }

    private void swap(int i, int j) {
        Cell temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}