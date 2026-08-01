public class Pathfinder {
    private Maze maze;
    private PriorityQueue unvisited;
    private boolean[][] closed;
    private int pathLength;
    private int cellsVisited;
    private boolean isInit;
    private boolean goalFound;
    private boolean isSearching;
    private boolean searchDone;


    public Pathfinder(Maze maze, PriorityQueue pq) {
        this.maze = maze;
        this.unvisited = pq;
    }

    public int calculateManhattan(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    public void init() {
        int rows = maze.getRows();
        int cols = maze.getCols();
        closed = new boolean[rows][cols];

        cellsVisited = 0;
        pathLength = 0;

        int startRow = maze.getStartRow();
        int startCol = maze.getStartCol();
        int goalRow = maze.getGoalRow();
        int goalCol = maze.getGoalCol();

        Cell start = maze.getCell(startRow, startCol);
        start.setGCost(0);
        start.setHCost(calculateManhattan(startRow, startCol, goalRow, goalCol));
        start.getFCost();
        unvisited.enqueue(start);
        start.setVisited(true);
        this.isInit = true;
        this.goalFound = false;
        this.isSearching = true;
    }

    public void move() {
        if (!unvisited.isEmpty() && !goalFound && isInit) {
            Cell current = unvisited.dequeue();
            int r = current.getRow();
            int c = current.getCol();

            if (!closed[r][c]) {
                closed[r][c] = true;
                current.setVisited(true);
                current.setOpen(false);
                cellsVisited++;

                if (r == maze.getGoalRow() && c == maze.getGoalCol()) {
                    goalFound = true;
                    isSearching = false;
                    backtrack(current);
                } else {
                    exploreNeighbors(current);
                }
            }
        } else if (unvisited.isEmpty() && !goalFound) {
            isSearching = false;
        }
    }

    public void exploreNeighbors(Cell current) {
        int[] vRow = {-1, 1, 0, 0};
        int[] vCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nextRow = current.getRow() + vRow[i];
            int nextCol = current.getCol() + vCol[i];

            if (maze.inBounds(nextRow, nextCol)
                    && !maze.getCell(nextRow, nextCol).isWall()
                    && !closed[nextRow][nextCol])
            {
                Cell neighbor = maze.getCell(nextRow, nextCol);
                int tempGCost = current.getGCost() + 1;

                if (tempGCost < neighbor.getGCost())
                {
                    neighbor.setParent(current);
                    neighbor.setGCost(tempGCost);
                    neighbor.setHCost(calculateManhattan(nextRow, nextCol,
                            maze.getGoalRow(), maze.getGoalCol()));
                    neighbor.getFCost();

                    unvisited.enqueue(neighbor);
                    neighbor.setOpen(true);
                }
            }
        }
    }

    public void backtrack(Cell goalCell) {
        if (goalFound) {
            Cell backtrack = goalCell;
            while (backtrack != null) {
                backtrack.setInPath(true);
                pathLength++;
                backtrack = backtrack.getParent();
            }
        }
        else
            System.err.println("Error: Goal has not been found.");
    }

    public int getPathLength() {
        return this.pathLength;
    }
    public int getCellsVisited() {
        return this.cellsVisited;
    }

    public boolean isSearching() {
        return isSearching;
    }

    public boolean isGoalFound() {
        return goalFound;
    }

    public boolean isSearchDone() {
        return searchDone;
    }
}
