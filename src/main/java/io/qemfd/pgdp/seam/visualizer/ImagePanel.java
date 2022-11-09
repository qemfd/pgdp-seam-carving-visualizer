package io.qemfd.pgdp.seam.visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ImagePanel extends JPanel {
	public static int bufferSize = 100;

	private static class ImgContainer {
		int[] pixels;
		int width;
		int height;
		String path;
		boolean loaded;


		private ImgContainer(int[] pixels, int width, int height, String path, boolean loaded) {
			this.pixels = pixels;
			this.width = width;
			this.height = height;
			this.path = path;
			this.loaded = loaded;
		}

		public void load() {
			if (loaded) return;
			try {
				File myObj = new File(path);
				Scanner myReader = new Scanner(myObj);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					data = data.substring(1, data.length() - 1);
					String[] dataArr = data.split(", ");
					pixels = new int[this.width * this.height];
					Arrays.parallelSetAll(pixels, (int index) -> Integer.parseInt(dataArr[index]));
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			loaded = true;
		}

		public void loadAsync() {
			new Thread(this::load).start();
		}

		public void unload() {
			this.pixels = null;
			this.loaded = false;
		}
	}

	private final ArrayList<ImgContainer> images = new ArrayList<>();
	private int idx;

	private final int width;
	private final int height;

	public ImagePanel(int[] image, int[] mask, int width, int height) {
		this.idx = 0;

		this.width = width;
		this.height = height;

		this.pushImageToQueue(image, width, height);
		this.pushImageToQueue(mask, width, height);

		// Set bounds
		setSize(width, height);

		setDoubleBuffered(true);
		setVisible(true);
	}

	public ImagePanel pushImageToQueue(int[] image, int width, int height) {
		String imageName = getNextImagePath();
		boolean loaded = true;

		// save the pixels to disk
		savePixelsAsync(image, imageName);


		if (images.size() - bufferSize > this.idx) {
			image = null;
			loaded = false;
		}

		images.add(new ImgContainer(image, width, height, imageName, loaded));

		return this;
	}

	private String getNextImagePath() {
		return "pixels/image_" + images.size() + ".pixels";
	}

	public ImagePanel nextImage() {
		if (idx + 1 < this.images.size()) {
			idx += 1;

			if (idx + bufferSize < images.size()) images.get(idx + bufferSize).loadAsync();
			if (idx - bufferSize >= 0) images.get(idx - bufferSize).unload();
		}
		return this;
	}

	public ImagePanel previousImage() {
		if (idx > 0) idx -= 1;
		return this;
	}

	void savePixelsAsync(int[] image, String path) {
		new Thread(() -> {
			try {
				FileWriter writer = new FileWriter(path);
				writer.write(Arrays.toString(image));
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}).start();
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		ImgContainer img = images.get(idx);

		bufferedImage.setRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);

		g.clearRect(0, 0, width, height);
		g.drawImage(bufferedImage, 0, 0, new JPanel());
	}
}
