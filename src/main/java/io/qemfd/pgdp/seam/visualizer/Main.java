package io.qemfd.pgdp.seam.visualizer;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Main {

	public static void printHelp() {
		System.out.println("""
				Usage: <program> [options] input_file mask_file
				                       
				    -h, --help: show this help message
				""");
	}

	public static void main(String[] args) throws IOException {
		String inputFile = null, maskFile = null;

		int index = 0;
		var helpPattern = Pattern.compile("--help|-[a-zA-Z]*h[a-zA-Z]*\\s*\\z");
		var optionPattern = Pattern.compile("-.*");
		for (String arg : args) {
			if (helpPattern.matcher(arg).matches()) {
				printHelp();
				System.exit(0);
			} else {
				if (optionPattern.matcher(arg).matches()) {
					printHelp();
					System.exit(1);
				} else {
					switch (index) {
						case 0 -> {
							inputFile = arg;
							index++;
						}
						case 1 -> {
							maskFile = arg;
							index++;
						}
						default -> {
							printHelp();
							System.exit(1);
						}
					}
				}
			}

		}

		if (inputFile == null) {
			printHelp();
			System.exit(1);
		}

		var inputDecoders = ImageIO.getImageReadersBySuffix(inputFile.substring(inputFile.lastIndexOf('.') + 1));
		if (!inputDecoders.hasNext()) {
			System.out.println("Unable to decode " + inputFile);
			System.exit(1);
		}

		var inputDecoder = inputDecoders.next();
		inputDecoder.setInput(new FileImageInputStream(new File(inputFile)));
		var inputImage = inputDecoder.read(0);
		var input = inputImage.getRGB(0, 0, inputImage.getWidth(), inputImage.getHeight(), null, 0, inputImage.getWidth());

		var maskDecoders = ImageIO.getImageReadersBySuffix(maskFile.substring(maskFile.lastIndexOf('.') + 1));
		if (!maskDecoders.hasNext()) {
			System.out.println("Unable to decode " + maskFile);
			System.exit(1);
		}
		var maskDecoder = maskDecoders.next();
		maskDecoder.setInput(new FileImageInputStream(new File(maskFile)));
		var maskImage = maskDecoder.read(0);
		if (maskImage.getWidth() != inputImage.getWidth() || maskImage.getHeight() != inputImage.getHeight()) {
			System.out.println("Mismatched image size between mask and input image");
			System.exit(1);
		}
		var mask = maskImage.getRGB(0, 0, maskImage.getWidth(), maskImage.getHeight(), null, 0, maskImage.getWidth());

		// Start GUI
		Visualizer viz = new Visualizer(input.clone(), mask.clone(), inputImage.getWidth(), inputImage.getHeight());


		SeamCarving carving = new SeamCarving();

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		while (width > 1) {
			int[] gradient = new int[width * height];
			carving.toGradientMagnitude(input, gradient, width, height);
			int[] gradientWithMask = gradient.clone();
			carving.combineMagnitudeWithMask(gradientWithMask, mask, width, height);
			viz.pushImage(gradient, width, height);
			viz.pushImage(gradientWithMask, width, height);

			int[] carved = carving.shrink(input, mask, width, height, width - 1);
			width--;
			viz.pushImage(carved, width, height);
			viz.pushImage(mask.clone(), width, height);
		}

	}
}
