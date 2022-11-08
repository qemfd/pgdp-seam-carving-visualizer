package io.qemfd.pgdp.seam.visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private final int[] image;
    private final int[] mask;
    private final int width;
    private final int height;

    public ImagePanel(int[] image, int[] mask, int width, int height) {
        this.image = image;
        this.mask = mask;
        this.width = width;
        this.height = height;

        // Set bounds
        setSize(width, height);

        setDoubleBuffered(true);
        setVisible(true);
    }

    public ImagePanel pushImageToQueue(int[] image) {
        return this;
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setRGB(0, 0, width, height, image, 0, width);

        g.drawImage(bufferedImage, 0, 0, new JPanel());
    }
}
