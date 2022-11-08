package io.qemfd.pgdp.seam.visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class ImagePanel extends JPanel {
	private final ArrayList<int[]> images = new ArrayList<>();
	private int idx;

	private final int width;
	private final int height;

	public ImagePanel(int[] image, int[] mask, int width, int height) {
		this.images.add(image);
		this.images.add(mask);

		this.idx = 0;


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

	public ImagePanel nextImage() {
		if (idx + 1 < this.images.size()) idx += 1;
		return this;
	}

	public ImagePanel previousImage() {
		if (idx > 0) idx -= 1;
		return this;
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.setRGB(0, 0, width, height, images.get(idx), 0, width);

		g.drawImage(bufferedImage, 0, 0, new JPanel());
	}
}
