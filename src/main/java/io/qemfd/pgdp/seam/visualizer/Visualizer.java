package io.qemfd.pgdp.seam.visualizer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Visualizer extends JFrame {
	private final ImagePanel panel;


	public Visualizer(int[] image, int[] mask, int width, int height) {
		setTitle("Seam Carving");
		getContentPane().setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(width, height);
		setVisible(true);

		panel = new ImagePanel(image, mask, width, height);
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				switch (e.getKeyChar()) {
					case 'k' -> panel.nextImage().repaint();
					case 'j' -> panel.previousImage().repaint();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		add(panel);
	}

	public void pushImage(int[] image, int width, int height) {
		panel.pushImageToQueue(image, width, height);
	}
}
