package io.qemfd.pgdp.seam.visualizer;

import javax.swing.*;

public class Visualizer extends JFrame {

	public Visualizer(int[] image, int[] mask, int width, int height) {
		setTitle("Seam Carving");
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(width, height);
		setVisible(true);
		add(new ImagePanel(image, mask, width, height));
	}
}
