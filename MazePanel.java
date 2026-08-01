import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;

public class MazePanel extends JPanel {
    private Maze maze;

    public MazePanel(Maze m) {
        this.maze = m;
    }

    public void setMaze(Maze m) {
        this.maze = m;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze != null && maze.isLoaded()) {
            int rows = maze.getRows();
            int cols = maze.getCols();

            int cellWidth = this.getWidth() / cols;
            int cellHeight = this.getHeight() / rows;

            for (int r = 0; r < rows; r++) {
                for(int c = 0; c < cols; c++) {
                    Cell cell = maze.getCell(r,c);
                    char symbol = cell.getSymbol();

                    if (cell.isInPath())
                        g.setColor(Color.YELLOW);
                    else if (cell.isVisited())
                        g.setColor(Color.LIGHT_GRAY);
                    else if(cell.isOpen())
                        g.setColor(new Color(255,255,204));
                    else {
                        switch(symbol) {
                            case '#':
                                g.setColor(new Color(13,13,104));
                                break;
                            case 'S':
                                g.setColor(Color.PINK);
                                break;
                            case 'G':
                                g.setColor(Color.CYAN);
                                break;
                            default:
                                g.setColor(Color.BLACK);
                                break;
                        }
                    }

                    int x = c * cellWidth;
                    int y = r * cellHeight;
                    g.fillRect(x, y, cellWidth, cellHeight);

                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x,y,cellWidth,cellHeight);
                }
            }
        }
    }
}
