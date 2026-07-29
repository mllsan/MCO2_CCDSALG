public class Pathfinder {
    private Maze maze;
    private PriorityQueue unvisited;
    private boolean[][] closed;
    private int pathLength;
    private int cellsVisited;

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
        boolean goalFound = false;
        Cell checkGoalCell = null;

        while (!unvisited.isEmpty() && !goalFound) {
            Cell current = unvisited.dequeue();
            int r = current.getRow();
            int c = current.getCol();

            if (!closed[r][c]) {
                closed[r][c] = true;
                cellsVisited++;

                if (r == goalRow && c == goalCol) {
                    goalFound = true;
                    checkGoalCell = current;
                } else {
                    int[] vRow = {-1, 1, 0, 0};
                    int[] vCol = {0, 0, -1, 1};

                    for (int i = 0; i < 4; i++) {
                        int nextRow = r + vRow[i];
                        int nextCol = c + vCol[i];

                        if (maze.inBounds(nextRow, nextCol)
                                && !maze.getCell(nextRow, nextCol).isWall()
                                && !closed[nextRow][nextCol])
                        {
                            Cell neighbor = maze.getCell(nextRow, nextCol);
                            int tempGCost = current.getGCost() + 1;

                            if (tempGCost < neighbor.getGCost()) {
                                neighbor.setParent(current);
                                neighbor.setGCost(tempGCost);
                                neighbor.setHCost(calculateManhattan(nextRow, nextCol, goalRow, goalCol));
                                neighbor.getFCost();

                                unvisited.enqueue(neighbor);
                            }
                        }
                    }
                }
            }
        }

        if (goalFound) {
            Cell backtrack = checkGoalCell;
            while (backtrack != null) {
                pathLength++;
                backtrack = backtrack.getParent();
            }
        }
    }

    public int getPathLength() {
        return this.pathLength;
    }

    public int getCellsVisited() {
        return this.cellsVisited;
    }

}
