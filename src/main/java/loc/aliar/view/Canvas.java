package loc.aliar.view;

import loc.aliar.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class Canvas extends JPanel {

    private final Image[] BACTS_IMAGES;

    private int cellSide = 20;

    private Graphics2D g;
    private Controller controller;

    Canvas(Controller controller) {
        BACTS_IMAGES = new Image[4];
        for (int i = 0; i <= 3; i++) {
            URL imgURL = ClassLoader.getSystemResource("image/b" + i + ".png");
            BACTS_IMAGES[i] = new ImageIcon(imgURL).getImage();
        }

        setDoubleBuffered(true);
        this.controller = controller;
        setupListeners();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = (Graphics2D) g;

        if (cellSide < 10) {
            drawField();
        } else {
            drawFieldWithImage();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int width = controller.getWidth() * cellSide;
        int height = controller.getHeight() * cellSide;
        return new Dimension(width, height);
    }

    int getCellSide() {
        return cellSide;
    }

    void setCellSide(int cellSide) {
        this.cellSide = cellSide;
        getParent().doLayout();
        repaint();
    }

    private void drawFieldWithImage() {
        for (int i = 0; i < controller.getHeight(); i++) {
            for (int j = 0; j < controller.getWidth(); j++) {
                if (controller.isBusy(i, j)) {
                    int imgInx = ThreadLocalRandom.current().nextInt(4);
                    g.drawImage(BACTS_IMAGES[imgInx], j * cellSide, i * cellSide, cellSide, cellSide, null);
                }
                g.drawRect(j * cellSide, i * cellSide, cellSide, cellSide);
            }
        }
    }

    private void drawField() {
        for (int i = 0; i < controller.getHeight(); i++) {
            for (int j = 0; j < controller.getWidth(); j++) {
                if (controller.isBusy(i, j)) {
                    g.fillRect(j * cellSide, i * cellSide, cellSide, cellSide);
                }
                g.drawRect(j * cellSide, i * cellSide, cellSide, cellSide);
            }
        }
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int y = e.getY();
                int x = e.getX();
                int i = y / cellSide;
                int j = x / cellSide;
                if (i < controller.getHeight() && j < controller.getWidth()) {
                    controller.click(i, j);
                    repaint();
                }
            }
        });
    }
}
