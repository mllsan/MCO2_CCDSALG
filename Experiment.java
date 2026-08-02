public class Experiment {
    public static void main(String[] args) {
        Maze maze = new Maze();
        PriorityQueue pq = new PriorityQueue();
        Pathfinder pf = new Pathfinder(maze, pq);

        for (int i = 1; i <= 6; i++) {
            String filepath = "mazes/maze" + i + ".txt";
            double avgExec = 0;
            int visited = 0, path = 0, avgVis, avgPath, count = 0;
            maze.loadFromFile(filepath);
            for (int j = 0; j < 10000; j++) {
                pf.init();
                while (!pf.isSearchDone()) {
                    pf.move();
                }
                avgExec += pf.getExecutionTimeMillis();
                visited += pf.getCellsVisited();
                path += pf.getPathLength();
                count++;
            }
            avgExec /= count;
            avgPath = (path / count);
            avgVis = (visited / count);
            System.out.println("File: " + filepath);
            System.out.println("-----------------------------");
            System.out.println("Average execution time in milliseconds: " + avgExec + " ms");
            System.out.println("Average cells visited (should not be float): " + avgVis);
            System.out.println("Average path length (should not be float): " + avgPath);
            System.out.println("Goal found: " + pf.isGoalFound());
            System.out.println("-----------------------------");
            System.out.println(" ");
        }
    }
}
