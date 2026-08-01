import javax.swing.*;
import java.awt.*;

public class MazeGUI {
    Timer timer;
    JFrame appFrame;
    Pathfinder pathfinder;
    Maze m;
    MazePanel mazePanel;

    public MazeGUI(Maze m, Pathfinder p) {
        this.m = m;
        this.pathfinder = p;
    }

    public void menu() {
        appFrame = new JFrame("Maze: A* Search");
        appFrame.setLayout(new BorderLayout());
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setBackground(new Color(13,13,104));

        JPanel topBar = createTopBar();
        appFrame.add(topBar, BorderLayout.NORTH);

        mazePanel = new MazePanel(m);
        appFrame.add(mazePanel, BorderLayout.CENTER);

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

        Font buttonFont = new Font("Monospaced", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(150,45);

        for (JButton b : new JButton[]{startButton, loadButton, exitButton}) {
            b.setFont(buttonFont);
            b.setPreferredSize(buttonSize);
            bar.add(b);
        }

        startButton.addActionListener(e -> animate());
        loadButton.addActionListener(e -> chooseMazes());
        exitButton.addActionListener(e -> System.exit(0));

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

            timer = new Timer(50, e -> {
                pathfinder.move();
                mazePanel.repaint();

                if(pathfinder.isSearchDone()) {
                    ((Timer) e.getSource()).stop();
                    displayMetrics();

                    if(pathfinder.isGoalFound()) {
                        JOptionPane.showMessageDialog(appFrame, "There is no valid path found!");
                    }
                }
            });
            timer.start();
        }
        else {
            JOptionPane.showMessageDialog(appFrame, "There is no maze loaded yet.");
        }
    }

    public void displayMetrics() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Maze m = new Maze();
            PriorityQueue pq = new PriorityQueue();
            Pathfinder p = new Pathfinder(m, pq);
            new MazeGUI(m,p).menu();
        });
    }
}
