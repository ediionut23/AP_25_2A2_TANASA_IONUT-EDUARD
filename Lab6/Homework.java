import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Compulsory extends JFrame {

    private JPanel configPanel;
    private JPanel controlPanel;
    private DrawingPanel drawingPanel;
    private JTextField dotCountField;
    private JButton createGameButton, loadButton, saveButton, exportButton, exitButton;

    private ArrayList<Point> dots = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private boolean isPlayerOneTurn = true;

    public Compulsory() {
        setTitle("Dot Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initConfigPanel();
        initDrawingPanel();
        initControlPanel();

        add(configPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void initConfigPanel() {
        configPanel = new JPanel(new FlowLayout());
        JLabel dotCountLabel = new JLabel("Number of Dots:");
        dotCountField = new JTextField(5);
        createGameButton = new JButton("Create New Game");

        createGameButton.addActionListener(e -> createNewGame());

        configPanel.add(dotCountLabel);
        configPanel.add(dotCountField);
        configPanel.add(createGameButton);
    }

    private void initDrawingPanel() {
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(800, 400));
    }

    private void initControlPanel() {
        controlPanel = new JPanel();

        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        exportButton = new JButton("Export PNG");
        exitButton = new JButton("Exit");

        loadButton.addActionListener(e -> loadGame());
        saveButton.addActionListener(e -> saveGame());
        exportButton.addActionListener(e -> drawingPanel.exportToPNG("board.png"));
        exitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(loadButton);
        controlPanel.add(saveButton);
        controlPanel.add(exportButton);
        controlPanel.add(exitButton);
    }

    private void createNewGame() {
        int dotCount;
        try {
            dotCount = Integer.parseInt(dotCountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number.");
            return;
        }

        dots.clear();
        lines.clear();
        isPlayerOneTurn = true;

        Random random = new Random();
        for (int i = 0; i < dotCount; i++) {
            int x = random.nextInt(drawingPanel.getWidth() - 20) + 10;
            int y = random.nextInt(drawingPanel.getHeight() - 20) + 10;
            dots.add(new Point(x, y));
        }

        drawingPanel.repaint();
    }

    private void saveGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()))) {
                GameData data = new GameData(dots, lines, isPlayerOneTurn);
                out.writeObject(data);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadGame() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {
                GameData data = (GameData) in.readObject();
                this.dots = data.dots;
                this.lines = data.lines;
                this.isPlayerOneTurn = data.isPlayerOneTurn;
                drawingPanel.repaint();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class DrawingPanel extends JPanel {
        private Point selectedDot = null;

        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Point dot : dots) {
                        if (dot.distance(e.getPoint()) < 10) {
                            if (selectedDot == null) {
                                selectedDot = dot;
                            } else if (!selectedDot.equals(dot)) {
                                Color color = isPlayerOneTurn ? Color.BLUE : Color.RED;
                                lines.add(new Line(selectedDot, dot, color));
                                isPlayerOneTurn = !isPlayerOneTurn;
                                selectedDot = null;
                                repaint();
                            } else {
                                selectedDot = null;
                            }
                            break;
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (dots != null) {
                g.setColor(Color.BLACK);
                for (Point dot : dots) {
                    g.fillOval(dot.x - 5, dot.y - 5, 10, 10);
                }
            }
            for (Line line : lines) {
                g.setColor(line.color);
                g.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
            }
        }

        public void exportToPNG(String filename) {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            paint(g2);
            g2.dispose();
            try {
                ImageIO.write(image, "png", new File(filename));
                JOptionPane.showMessageDialog(this, "Image saved as " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Line implements Serializable {
        Point p1, p2;
        Color color;

        public Line(Point p1, Point p2, Color color) {
            this.p1 = p1;
            this.p2 = p2;
            this.color = color;
        }
    }

    private static class GameData implements Serializable {
        ArrayList<Point> dots;
        ArrayList<Line> lines;
        boolean isPlayerOneTurn;

        public GameData(ArrayList<Point> dots, ArrayList<Line> lines, boolean isPlayerOneTurn) {
            this.dots = new ArrayList<>(dots);
            this.lines = new ArrayList<>(lines);
            this.isPlayerOneTurn = isPlayerOneTurn;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Compulsory().setVisible(true));
    }
}
