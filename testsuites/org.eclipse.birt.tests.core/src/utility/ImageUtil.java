/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package utility;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * Perform operations on images and image files: read, save, compare etc. 
 */
public class ImageUtil {

	// private constructor
	private ImageUtil() {
	}

	public static enum ImageCompParam {
		XBLOCKS, // number of blocks by x-axis
		YBLOCKS, // number of blocks by y-axis
		TOLERANCE, // mismatch tolerance (max difference)
		STABILIZER, // algorithm stabilizer value
		DEBUG // 1: textual indication of change, 2: difference of factors
	};

	public static Map<ImageCompParam, Integer> getDefaultCompParams() {
		Map<ImageCompParam, Integer> params = new HashMap<ImageCompParam, Integer>();
		params.put(ImageCompParam.XBLOCKS, 20);
		params.put(ImageCompParam.YBLOCKS, 20);
		params.put(ImageCompParam.TOLERANCE, 3);
		params.put(ImageCompParam.STABILIZER, 10);
		params.put(ImageCompParam.DEBUG, 2);
		return params;
	}

	public static Map<ImageCompParam, Integer> mergeDefaultCompParams(Map<ImageCompParam, Integer> params) {
		Map<ImageCompParam, Integer> result = getDefaultCompParams();
		if (params != null) {
			result.putAll(params);
		}
		return result;
	}

	/*
	 * Compare actual image generated during test to expected golden result. Returns
	 * null if there is a match or an image with mismatched blocks marked in red if
	 * there is no match. Algorithm is based on dividing images on number of blocks
	 * and comparing average weighted brightness in corresponding blocks. The images
	 * are converted to gray scale for comparison.
	 */
	public static Image compare(BufferedImage golden, BufferedImage actual, Map<ImageCompParam, Integer> p) {

		Map<ImageCompParam, Integer> params = mergeDefaultCompParams(p);

		// prepare image for indicating potential mismatched blocks
		BufferedImage noMatch = imageToBufferedImage(actual);
		Graphics2D gc = noMatch.createGraphics();
		gc.setColor(Color.RED);

		// convert to gray images
		golden = imageToBufferedImage(GrayFilter.createDisabledImage(golden));
		actual = imageToBufferedImage(GrayFilter.createDisabledImage(actual));

		// get block size
		int goldenBlockXSize = (int) (golden.getWidth() / params.get(ImageCompParam.XBLOCKS));
		int goldenBlockYSize = (int) (golden.getHeight() / params.get(ImageCompParam.YBLOCKS));
		int actualBlockXSize = (int) (actual.getWidth() / params.get(ImageCompParam.XBLOCKS));
		int actualBlockYSize = (int) (actual.getHeight() / params.get(ImageCompParam.YBLOCKS));

		boolean match = true;

		// traverse and compare respective blocks of both images
		for (int y = 0; y < params.get(ImageCompParam.YBLOCKS); y++) {
			if (params.get(ImageCompParam.DEBUG) > 0)
				System.out.print("|");
			for (int x = 0; x < params.get(ImageCompParam.XBLOCKS); x++) {
				int goldenBrightness = getAverageBrightness(golden.getSubimage(x * goldenBlockXSize,
						y * goldenBlockYSize, goldenBlockXSize - 1, goldenBlockYSize - 1), params);
				int actualBrightness = getAverageBrightness(actual.getSubimage(x * actualBlockXSize,
						y * actualBlockYSize, actualBlockXSize - 1, actualBlockYSize - 1), params);
				int diff = Math.abs(goldenBrightness - actualBrightness);
				if (diff > params.get(ImageCompParam.TOLERANCE)) {
					// the difference in a certain region has passed the threshold value
					// draw an indicator on the change image to show where the change was detected
					gc.drawRect(x * actualBlockXSize, y * actualBlockYSize, actualBlockXSize - 1, actualBlockYSize - 1);
					match = false;
				}
				if (params.get(ImageCompParam.DEBUG) == 1)
					System.out.print((diff > params.get(ImageCompParam.TOLERANCE) ? "X" : " "));
				if (params.get(ImageCompParam.DEBUG) == 2)
					System.out.print(diff + (x < params.get(ImageCompParam.XBLOCKS) - 1 ? "," : ""));
			}
			if (params.get(ImageCompParam.DEBUG) > 0)
				System.out.println("|");
		}

		return match ? null : noMatch;
	}

	public static Image compare(String golden, String actual) throws IOException {
		return compare(loadImageFromFile(golden), loadImageFromFile(actual), getDefaultCompParams());
	}

	public static Image compare(String golden, String actual, Map<ImageCompParam, Integer> params) throws IOException {
		if (mergeDefaultCompParams(params).get(ImageCompParam.DEBUG) > 0) {
			System.out.print("Golden: " + golden);
			System.out.print("Actual: " + actual);
		}
		return compare(loadImageFromFile(golden), loadImageFromFile(actual), params);
	}

	public static Image compare(Image golden, Image actual, Map<ImageCompParam, Integer> params) {
		return compare(imageToBufferedImage(golden), imageToBufferedImage(actual), params);
	}

	/*
	 * Returns a value indicating the average brightness in the image
	 */
	protected static int getAverageBrightness(BufferedImage img, Map<ImageCompParam, Integer> p) {
		Map<ImageCompParam, Integer> params = mergeDefaultCompParams(p);
		Raster r = img.getData();
		int total = 0;
		for (int y = 0; y < r.getHeight(); y++) {
			for (int x = 0; x < r.getWidth(); x++) {
				total += r.getSample(r.getMinX() + x, r.getMinY() + y, 0);
			}
		}
		return (int) (total / ((r.getWidth() / params.get(ImageCompParam.STABILIZER))
				* (r.getHeight() / params.get(ImageCompParam.STABILIZER))));
	}

	public static BufferedImage imageToBufferedImage(Image img) {
		assert img != null;
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, null, null);
		return bi;
	}

	/*
	 * Write an image to a file in the specified image format
	 */
	public static void saveImageToFile(Image img, String filename, String format) throws IOException {
		assert img != null;
		BufferedImage bi = imageToBufferedImage(img);
		File outputfile = new File(filename);
		ImageIO.write(bi, format, outputfile);
	}

	public static void saveJPG(Image img, String filename) throws IOException {
		saveImageToFile(img, filename, "jpg");
	}

	public static void savePNG(Image img, String filename) throws IOException {
		saveImageToFile(img, filename, "png");
	}

	/*
	 * Read JPEG or other image file into a buffered image
	 */
	public static Image loadImageFromFile(String filename) throws IOException {
		return ImageIO.read(new File(filename));
	}

	public static void main(String[] args) throws IOException {
		Image ic = compare("C:\\8\\golden.jpg", "C:\\8\\actual.jpg");
		System.out.println("Match: " + (ic == null));
		if (ic != null) {
			savePNG(ic, "C:\\8\\diff.png ");
		}
	}

}
