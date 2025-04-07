import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Compulsory extends JFrame {

    private JPanel configPanel;
    private JPanel controlPanel;
    private DrawingPanel drawingPanel;
    private JTextField dotCountField;
    private JButton createGameButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton exitButton;

    private ArrayList<Point> dots;

    public Compulsory() {
        setTitle("Dot Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        configPanel = new JPanel();
        configPanel.setLayout(new FlowLayout());

        JLabel dotCountLabel = new JLabel("Number of Dots:");
        dotCountField = new JTextField(5);
        createGameButton = new JButton("Create New Game");

        configPanel.add(dotCountLabel);
        configPanel.add(dotCountField);
        configPanel.add(createGameButton);

        drawingPanel = new DrawingPanel();

        controlPanel = new JPanel();
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");

        controlPanel.add(loadButton);
        controlPanel.add(saveButton);
        controlPanel.add(exitButton);

        add(configPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        createGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void createNewGame() {
        int dotCount = Integer.parseInt(dotCountField.getText());
        dots = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < dotCount; i++) {
            int x = random.nextInt(drawingPanel.getWidth());
            int y = random.nextInt(drawingPanel.getHeight());
            dots.add(new Point(x, y));
        }

        drawingPanel.repaint();
    }

    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            setPreferredSize(new Dimension(800, 400));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (dots != null) {
                g.setColor(Color.BLACK);
                for (Point dot : dots) {
                    g.fillOval(dot.x - 5, dot.y - 5, 10, 10);  // Draw a small circle (dot)
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Compulsory().setVisible(true);
            }
        });
    }
}
