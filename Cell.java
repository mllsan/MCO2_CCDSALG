/**
 * Represents a cell considered by the A* algorithm.
 *
 * Each cell stores the cost from the start (gCost), the
 * estimated cost to the goal (hCost), and their sum (fCost).
 */
public class Cell implements Comparable<Cell> {
    private final int row;
    private final int col;
    private final int gCost;
    private final int hCost;
    private final Cell parent;

    public Cell(int row, int col, int gCost, int hCost, Cell parent) {
        this.row = row;
        this.col = col;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getGCost() {
        return gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    public Cell getParent() {
        return parent;
    }

    /**
     * Orders cells by their estimated total path cost. When f costs tie,
     * the cell with the lower heuristic is preferred.
     */
    @Override
    public int compareTo(Cell other) {
        int fComparison = Integer.compare(getFCost(), other.getFCost());
        int comparison = fComparison;
        if (comparison == 0) {
            comparison = Integer.compare(hCost, other.hCost);
        }
        return comparison;
    }
}