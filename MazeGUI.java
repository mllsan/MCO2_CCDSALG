import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MazeGUI {
    private Timer timer;
    private JFrame appFrame;
    private Pathfinder pathfinder;
    private Maze m;
    private MazePanel mazePanel;
    private JLabel metricsLabel;
    private JPanel topBar;

    public MazeGUI(Maze m, Pathfinder p) {
        this.m = m;
        this.pathfinder = p;
    }

    public void menu() {
        appFrame = new JFrame("Maze: A* Search");
        appFrame.setLayout(new BorderLayout());
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setBackground(new Color(13,13,104));

        topBar = createTopBar();
        appFrame.add(topBar, BorderLayout.NORTH);

        mazePanel = new MazePanel(m);
        appFrame.add(mazePanel, BorderLayout.CENTER);

        JPanel metricBar = metricsBar();
        appFrame.add(metricBar, BorderLayout.SOUTH);

        appFrame.setSize(1000, 700);
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bar.setBackground(Color.black);

        JButton startButton = new JButton("Start");
        JButton loadButton = new JButton("Load Maze");
        JButton exitButton = new JButton("Exit");
        JButton menuButton = new JButton("Return to Main Menu");

        Font buttonFont = new Font("Monospaced", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(150,45);
        Border btnBorder = BorderFactory.createLineBorder(Color.WHITE, 2);

        if (!pathfinder.isSearching() ) {
            for (JButton b : new JButton[]{startButton, loadButton, exitButton}) {
                b.setBackground(Color.black);
                b.setForeground(Color.WHITE);
                b.setBorder(btnBorder);
                b.setFocusPainted(false);
                b.setFont(buttonFont);
                b.setPreferredSize(buttonSize);
                bar.add(b);
            }
        }
        else if (pathfinder.isSearchDone()){
            for (JButton b : new JButton[]{exitButton, menuButton}) {
                b.setBackground(Color.black);
                b.setForeground(Color.WHITE);
                b.setBorder(btnBorder);
                b.setFocusPainted(false);
                b.setFont(buttonFont);
                b.setPreferredSize(buttonSize);
                bar.add(b);
            }
        }

        startButton.addActionListener(e -> animate());
        loadButton.addActionListener(e -> chooseMazes());
        exitButton.addActionListener(e -> System.exit(0));
        menuButton.addActionListener(e -> menu());

        return bar;
    }

    public void chooseMazes() {
        JDialog dialog = new JDialog(appFrame, "Select a Maze", true);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        Dimension btnSize = new Dimension(80, 40);
        for (int i = 1; i <= 4; i++) {
            int maze = i;
            JButton btnChoice = new JButton(String.valueOf(maze));
            btnChoice.setPreferredSize(btnSize);
            btnChoice.setFont(new Font("SansSerif", Font.BOLD, 14));

            btnChoice.addActionListener(e -> {
                String filepath = "mazes/maze" + maze +".txt";
                boolean isLoaded = m.loadFromFile(filepath);

                if (isLoaded) {
                    for (int r = 0; r < m.getRows(); r++) {
                        for (int c = 0; c < m.getCols(); c++) {
                            m.getCell(r,c).resetStates();
                        }
                    }
                    mazePanel.setMaze(m);
                }
                else
                    JOptionPane.showMessageDialog(dialog, "Failed to load maze.");

                dialog.dispose();
            });
            dialog.add(btnChoice);
        }
        dialog.pack();
        dialog.setLocationRelativeTo(appFrame);
        dialog.setVisible(true);
    }

    public void animate() {
        if (m != null && m.isLoaded()) {

            if (timer != null && timer.isRunning())
                timer.stop();

            pathfinder.init();
            mazePanel.repaint();
            topBar = createTopBar();
            topBar.repaint();

            timer = new Timer(75, e -> {
                pathfinder.move();
                mazePanel.repaint();

                metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                        " | Path Length: " + pathfinder.getPathLength() +
                        " | Status: Searching for path...");

                if(pathfinder.isSearchDone()) {
                    ((Timer) e.getSource()).stop();

                    if(!pathfinder.isGoalFound()) {
                        metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                                " | Path Length: " + pathfinder.getPathLength() +
                                " | Status: No path was found! | Execution Time: "
                                + pathfinder.getExecutionTimeMillis() + " ms");
                        pathfinder.resetTimers();
                    }
                    else {
                        metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                                " | Path Length: " + pathfinder.getPathLength() +
                                " | Status: Goal found! | Execution Time: "
                                + pathfinder.getExecutionTimeMillis() + " ms");
                        pathfinder.resetTimers();
                    }
                }
            });
            timer.start();
        }
        else {
            JOptionPane.showMessageDialog(appFrame, "There is no maze loaded yet.");
        }
    }

    public JPanel metricsBar() {
        JPanel metricsBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,10));
        metricsBar.setBackground(Color.black);

        metricsLabel = new JLabel("Cells Visited: 0 | Path Length: 0 | Status: Not Started");
        metricsLabel.setForeground(Color.white);
        metricsLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        metricsBar.add(metricsLabel);

        return metricsBar;
    }
}
