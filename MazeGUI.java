import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MazeGUI {
    private Timer timer;
    private JFrame appFrame;
    private Pathfinder pathfinder;
    private Maze m;
    private MazePanel mainMaze;
    private MazePanel previewMaze;
    private JLabel previewDescription;
    private JLabel metricsLabel;
    private JPanel topBar;
    private JPanel centerContainer;
    private JPanel rightMenuPanel;
    private CardLayout screenMngr;
    private JButton startButton;
    private JButton loadButton;
    private JButton exitButton;
    private JButton menuButton;

    public MazeGUI(Maze m, Pathfinder p) {
        this.m = m;
        this.pathfinder = p;
    }

    public void menu() {
        appFrame = new JFrame("A Star is Searched: A* Search for Maze");
        appFrame.setLayout(new BorderLayout());
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.getContentPane().setBackground(new Color(13,13,104));

        startButton = new JButton("Start");
        loadButton = new JButton("Load Maze");
        exitButton = new JButton("Exit");
        menuButton = new JButton("Main Menu");

        topBar = createTopBar();
        appFrame.add(topBar, BorderLayout.NORTH);
        JPanel metricBar = metricsBar();
        appFrame.add(metricBar, BorderLayout.SOUTH);

        screenMngr = new CardLayout();
        centerContainer = new JPanel(screenMngr);
        centerContainer.setBackground(new Color(13,13,104));

        JPanel menuScreen = new JPanel(new BorderLayout(20,20));
        menuScreen.setBackground(new Color(13,13,104));
        menuScreen.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        JLabel titleCard = new JLabel("<html>A Star is<br>Searched</html>", JLabel.LEFT);
        titleCard.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 45));
        titleCard.setForeground(Color.WHITE);
        menuScreen.add(titleCard, BorderLayout.WEST);

        rightMenuPanel = new JPanel(new BorderLayout(0,10));
        rightMenuPanel.setBackground(new Color(13,13,104));

        previewMaze = new MazePanel(m);
        previewMaze.setPreferredSize(new Dimension(500, 450));

        previewDescription = new JLabel("Select a maze to preview", JLabel.CENTER);
        previewDescription.setFont(new Font("Monospaced", Font.PLAIN, 14));
        previewDescription.setForeground(Color.LIGHT_GRAY);

        rightMenuPanel.add(previewMaze,BorderLayout.CENTER);
        rightMenuPanel.add(previewDescription,BorderLayout.SOUTH);
        rightMenuPanel.setVisible(false);

        menuScreen.add(rightMenuPanel, BorderLayout.EAST);

        mainMaze = new MazePanel(m);
        JPanel mazeScreen = new JPanel(new BorderLayout());
        mazeScreen.add(mainMaze, BorderLayout.CENTER);

        centerContainer.add(menuScreen, "MENU_SCREEN");
        centerContainer.add(mazeScreen, "MAZE_SCREEN");
        appFrame.add(centerContainer, BorderLayout.CENTER);

        appFrame.setSize(1000, 700);
        appFrame.setLocationRelativeTo(null);
        screenMngr.show(centerContainer, "MENU_SCREEN");
        appFrame.setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bar.setBackground(Color.black);

        Font buttonFont = new Font("Monospaced", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(150,45);
        Border btnBorder = BorderFactory.createLineBorder(Color.WHITE, 2);

        for (JButton b : new JButton[]{startButton, loadButton, exitButton, menuButton}) {
            b.setBackground(Color.black);
            b.setForeground(Color.WHITE);
            b.setBorder(btnBorder);
            b.setFocusPainted(false);
            b.setFont(buttonFont);
            b.setPreferredSize(buttonSize);
            bar.add(b);
        }

        startButton.addActionListener(e -> animate());
        loadButton.addActionListener(e -> chooseMazes());
        exitButton.addActionListener(e -> System.exit(0));
        menuButton.addActionListener(e -> resetAll());

        updateButtonVisibility();

        return bar;
    }

    public void chooseMazes() {
        JDialog dialog = new JDialog(appFrame, "Select a Maze", true);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        Dimension btnSize = new Dimension(80, 40);
        for (int i = 1; i <= 6; i++) {
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
                    previewMaze.setMaze(m);
                    previewMaze.repaint();
                    previewDescription.setText("Successfully loaded maze! (" + m.getRows() + " x " + m.getCols() + ")");
                    rightMenuPanel.setVisible(true);
                    mainMaze.setMaze(m);
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
        mainMaze.setMaze(m);
        mainMaze.repaint();
        screenMngr.show(centerContainer, "MAZE_SCREEN");
        mainMaze.setVisible(true);
        if (m != null && m.isLoaded()) {

            if (timer != null && timer.isRunning())
                timer.stop();

            pathfinder.init();
            mainMaze.repaint();

            timer = new Timer(75, e -> {
                if (pathfinder.isSearching()) {
                    pathfinder.move();
                    mainMaze.repaint();

                    metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                            " | Path Length: " + pathfinder.getPathLength() +
                            " | Status: Searching for path...");

                } else if (pathfinder.isGoalFound() && pathfinder.isAnimatingPath()) {
                    pathfinder.stepThruPath();
                    mainMaze.repaint();

                    metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                            " | Path Length: " + pathfinder.getPathLength() +
                            " | Status: Tracing final path...");
                } else {
                    ((Timer) e.getSource()).stop();
                    updateButtonVisibility();

                    if(!pathfinder.isGoalFound()) {
                        metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                                " | Path Length: " + pathfinder.getPathLength() +
                                " | Status: No path was found! | Execution Time: "
                                + pathfinder.getExecutionTimeMillis() + " ms");
                    }
                    else {
                        metricsLabel.setText("Cells Visited: " + pathfinder.getCellsVisited() +
                                " | Path Length: " + pathfinder.getPathLength() +
                                " | Status: Goal found! | Execution Time: "
                                + pathfinder.getExecutionTimeMillis() + " ms");
                    }
                    pathfinder.resetTimers();
                }
            });
            timer.start();
            updateButtonVisibility();
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

    public void updateButtonVisibility() {
        if (pathfinder != null && pathfinder.isSearchDone()) {
            startButton.setVisible(false);
            loadButton.setVisible(false);
            exitButton.setVisible(true);
            menuButton.setVisible(true);
        } else if (pathfinder != null && pathfinder.isSearching()) {
            startButton.setVisible(false);
            loadButton.setVisible(false);
            exitButton.setVisible(true);
            menuButton.setVisible(false);
        } else {
            startButton.setVisible(true);
            loadButton.setVisible(true);
            exitButton.setVisible(true);
            menuButton.setVisible(false);
        }

        if (topBar != null) {
            topBar.revalidate();
            topBar.repaint();
            appFrame.repaint();
        }
    }

    private void resetAll() {
        if (timer != null && timer.isRunning())
            timer.stop();

        pathfinder.init();
        if (pathfinder != null) {
            pathfinder.setSearchDone(false);
            pathfinder.setSearching(false);
        }

        if (metricsLabel != null) {
            metricsLabel.setText("Cells Visited: 0 | Path Length: 0 | Status: Not Started");
        }
        updateButtonVisibility();

        if (mainMaze != null) {
            mainMaze.repaint();
            mainMaze.setVisible(false);
        }
        screenMngr.show(centerContainer, "MENU_SCREEN");
    }
}
