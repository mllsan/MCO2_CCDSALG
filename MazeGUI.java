import javax.swing.*;
import java.awt.*;

public class MazeGUI {
    JFrame appFrame;
    JPanel appContainer;
    Maze m;
    MazePanel mazePanel;

    public MazeGUI(Maze m) {
        this.m = m;
    }

    public void menu() {
        appFrame = new JFrame("Maze: A* Search");
        appFrame.setLayout(new BorderLayout());
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.getContentPane().setBackground(new Color(0,0,0));

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

        Font buttonFont = new Font("Monospace", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(150,45);

        for (JButton b : new JButton[]{startButton, loadButton, exitButton}) {
            b.setFont(buttonFont);
            b.setPreferredSize(buttonSize);
            bar.add(b);
        }

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

                if (isLoaded)
                    mazePanel.setMaze(m);
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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Maze m = new Maze();
            new MazeGUI(m).menu();
        });
    }
}
