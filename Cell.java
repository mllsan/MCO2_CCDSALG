/**
 * Represents a cell considered by the A* algorithm.
 *
 * Each cell stores the cost from the start (gCost), the
 * estimated cost to the goal (hCost), and their sum (fCost).
 */
public class Cell implements Comparable<Cell> {
    private final int row;
    private final int col;
    private final char symbol;
    private int gCost;
    private int hCost;
    private Cell parent;

    public Cell(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.gCost = Integer.MAX_VALUE;
        this.hCost = 0;
        this.parent = null;
        this.symbol = symbol;
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