import java.util.ArrayList;

public class Pathfinder {
    private Maze maze;
    private PriorityQueue unvisited;
    private boolean[][] closed;

    private int pathLength;
    private int cellsVisited;
    private boolean solutionFound;

    private ArrayList<Cell> explorationOrder;
    private ArrayList<Cell> finalPath;

    public Pathfinder(Maze maze, PriorityQueue pq) {
        this.maze = maze;
        this.unvisited = pq;
    }

    public int calculateManhattan(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    public void solve() {

        int rows = maze.getRows();
        int cols = maze.getCols();

        closed = new boolean[rows][cols];

        explorationOrder = new ArrayList<Cell>();
        finalPath = new ArrayList<Cell>();

        cellsVisited = 0;
        pathLength = 0;
        solutionFound = false;

        int startRow = maze.getStartRow();
        int startCol = maze.getStartCol();

        int goalRow = maze.getGoalRow();
        int goalCol = maze.getGoalCol();

        Cell start = maze.getCell(startRow, startCol);

        start.setGCost(0);
        start.setHCost(calculateManhattan(startRow, startCol, goalRow, goalCol));

        unvisited.enqueue(start);

        Cell goalCell = null;

        while (!unvisited.isEmpty() && !solutionFound) {

            Cell current = unvisited.dequeue();

            int r = current.getRow();
            int c = current.getCol();

            if (!closed[r][c]) {

                closed[r][c] = true;
                cellsVisited++;

                explorationOrder.add(current);

                if (r == goalRow && c == goalCol) {

                    solutionFound = true;
                    goalCell = current;

                } else {

                    int[] rowMove = {-1, 1, 0, 0};
                    int[] colMove = {0, 0, -1, 1};

                    for (int i = 0; i < 4; i++) {

                        int nextRow = r + rowMove[i];
                        int nextCol = c + colMove[i];

                        if (maze.inBounds(nextRow, nextCol)
                                && !maze.getCell(nextRow, nextCol).isWall()
                                && !closed[nextRow][nextCol]) {

                            Cell neighbor = maze.getCell(nextRow, nextCol);

                            int newG = current.getGCost() + 1;

                            if (newG < neighbor.getGCost()) {

                                neighbor.setParent(current);
                                neighbor.setGCost(newG);
                                neighbor.setHCost(
                                        calculateManhattan(nextRow, nextCol,
                                                goalRow, goalCol));

                                unvisited.enqueue(neighbor);
                            }
                        }
                    }
                }
            }
        }

        if (solutionFound) {

            Cell backtrack = goalCell;

            while (backtrack != null) {

                finalPath.add(0, backtrack);
                pathLength++;

                backtrack = backtrack.getParent();
            }
        }
    }

    public ArrayList<Cell> getExplorationOrder() {
        return explorationOrder;
    }

    public ArrayList<Cell> getFinalPath() {
        return finalPath;
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public int getPathLength() {
        return pathLength;
    }

    public int getCellsVisited() {
        return cellsVisited;
    }

    public boolean[][] getClosed() {
        return closed;
    }
}