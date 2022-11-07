package io.qemfd.pgdp.seam.visualizer;

import javax.swing.*;

public class Visualizer extends JFrame {

    public Visualizer(int[] image, int[] mask) {
        setTitle("Seam Carving");
        setLocation(0, 0);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        add(new ImagePanel(image, mask));
    }
}
