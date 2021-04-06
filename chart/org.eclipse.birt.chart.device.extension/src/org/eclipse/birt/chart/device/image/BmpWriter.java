/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.image;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.OutputStream;

/**
 * BmpWriter
 */
public class BmpWriter {

	// BITMAPFILEHEADER fields
	private static final int pbfType = 0;
	private static final int pbfSize = 2;
	// private static final int pbfReserved1 = 6;
	// private static final int pbfReserved2 = 8;
	private static final int pbfOffBits = 10;
	private static final int bf_size = 14;

	// BITMAPINFOHEADER fields
	private static final int pbiSize = 0;
	private static final int pbiWidth = 4;
	private static final int pbiHeight = 8;
	private static final int pbiPlanes = 12;
	private static final int pbiBitCount = 14;
	// private static final int pbiCompression = 16;
	private static final int pbiSizeImage = 20;
	private static final int pbiXPelsPerMeter = 24;
	private static final int pbiYPelsPerMeter = 28;
	// private static final int pbiClrUsed = 32;
	// private static final int pbiClrImportant = 36;
	private static final int bi_size = 40;

	// members of BITMAPFILEHEADER
	// private int bfType;
	// private int bfReserved1;
	// private int bfReserved2;
	private int bfSize;
	private int bfOffBits;

	// members of BITMAPINFOHEADER
	// private int biSize;
	// private int biWidth;
	// private int biHeight;
	// private int biPlanes;
	// private int biCompression;
	// private int biXPelsPerMeter;
	// private int biYPelsPerMeter;
	// private int biClrUsed;
	// private int biClrImportant;
	private int biBitCount;
	private int biSizeImage;

	private RGBProcessor processor;

	private int[] pix;
	private int width, height, rgbcount, rgbquadints, rgbquadbytes, widthbytes;

	/**
	 * The constructor.
	 * 
	 * @param img
	 */
	public BmpWriter(Image img) {
		if (img == null) {
			return;
		}

		PixelGrabber pg = new PixelGrabber(img, 0, 0, -1, -1, true);

		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			return;
		}

		if ((pg.status() & ImageObserver.ABORT) != 0) {
			return;
		}

		this.pix = (int[]) pg.getPixels();
		this.width = pg.getWidth();
		this.height = pg.getHeight();
	}

	/**
	 * The constructor.
	 * 
	 * @param pix
	 * @param width
	 * @param height
	 */
	public BmpWriter(int pix[], int width, int height) {
		this.pix = pix;
		this.width = width;
		this.height = height;
	}

	/**
	 * Writes the image to given OutputStream.
	 * 
	 * @param os
	 * @throws IOException
	 */
	public void write(OutputStream os) throws IOException {
		processor = new RGBProcessor(pix, 256);

		rgbcount = processor.count();
		if (rgbcount < 0) {
			biBitCount = 24;
			rgbquadints = 0;
		} else if (rgbcount > 16) {
			biBitCount = 8;
			rgbquadints = 256;
		} else if (rgbcount > 2) {
			biBitCount = 4;
			rgbquadints = 16;
		} else {
			biBitCount = 1;
			rgbquadints = 2;
		}

		widthbytes = ((width * biBitCount + 31) & ~31) >>> 3;
		rgbquadbytes = rgbquadints << 2;
		bfOffBits = bf_size + bi_size + rgbquadbytes;
		biSizeImage = widthbytes * height;
		bfSize = bfOffBits + biSizeImage;

		writeFileHeader(os);
		writeInfoHeader(os);
		writeRGBQuad(os);
		writeBody(os);
	}

	void writeFileHeader(OutputStream os) throws IOException {
		ByteArray fileheader = new ByteArray(bf_size, false);
		fileheader.set2(0x4d42, pbfType);
		fileheader.set4(bfSize, pbfSize);
		fileheader.set4(bfOffBits, pbfOffBits);
		os.write(fileheader.getBytes());
	}

	void writeInfoHeader(OutputStream os) throws IOException {
		ByteArray infoheader = new ByteArray(bi_size, false);
		infoheader.set4(bi_size, pbiSize);
		infoheader.set4(width, pbiWidth);
		infoheader.set4(height, pbiHeight);
		infoheader.set2(1, pbiPlanes);
		infoheader.set2(biBitCount, pbiBitCount);
		infoheader.set4(biSizeImage, pbiSizeImage);
		infoheader.set4(0x1000, pbiXPelsPerMeter);
		infoheader.set4(0x1000, pbiYPelsPerMeter);
		os.write(infoheader.getBytes());
	}

	void writeRGBQuad(OutputStream os) throws IOException {
		int[] rgbquad = processor.getTable();
		if (rgbquad == null)
			return;

		ByteArray bamRgbQuad = new ByteArray(rgbquadbytes, false);
		for (int i = 0; i < rgbcount; i++) {
			bamRgbQuad.set4(rgbquad[i], i << 2);
		}
		os.write(bamRgbQuad.getBytes());
	}

	void writeBody(OutputStream os) throws IOException {
		int xy = width * height;
		byte[] bline = new byte[widthbytes];
		int[] iline = new int[width + 7]; // width padding

		while (xy > 0) {
			xy -= width;
			for (int x = 0; x < width; x++) {
				iline[x] = pix[xy + x];
			}

			switch (biBitCount) {
			case 1: // 2 colors
				for (int x = 0; x < width;) {
					int bx = x >>> 3;
					int ixs = 0;
					for (int i = 0; i < 8; i++) {
						ixs <<= 1;
						ixs |= processor.getIndex(iline[x++]);
					}
					bline[bx] = (byte) ixs;
				}
				break;

			case 4: // 16 colors
				for (int x = 0; x < width; x += 2) {
					bline[x >>> 1] = (byte) ((processor.getIndex(iline[x]) << 4) | processor.getIndex(iline[x + 1]));
				}
				break;

			case 8: // 256 colors
				for (int x = 0; x < width; x++) {
					bline[x] = (byte) processor.getIndex(iline[x]);
				}
				break;

			default: // 24 bit colors:
				int x3 = 0;
				for (int x = 0; x < width; x++) {
					int col = iline[x];
					bline[x3 + 2] = (byte) ((col >> 16) & 0xff);
					bline[x3 + 1] = (byte) ((col >> 8) & 0xff);
					bline[x3 + 0] = (byte) (col & 0xff);
					x3 += 3;
				}
			}
			os.write(bline);
		}
	}

	/**
	 * ByteArray
	 */
	static class ByteArray {

		private int h2, l2, // high,low pointer in 2-byte
				h4, l4, // high,low pointer in 4-byte
				h8, l8; // high,low pointer in 8-byte
		private byte[] bytes;

		byte[] getBytes() {
			return bytes;
		}

		ByteArray(boolean bigendian) {
			h2 = bigendian ? 0 : 1;
			l2 = 1 - h2;
			h4 = h2 << 1;
			l4 = l2 << 1;
			h8 = h4 << 1;
			l8 = l4 << 1;
		}

		ByteArray(int len, boolean bigendian) {
			this(bigendian);
			bytes = new byte[len];
		}

		// set n-byte
		void set1(byte by, int i) {
			bytes[i] = by;
		}

		void set1(int in, int i) {
			bytes[i] = (byte) in;
		}

		void set2(int in, int i) {
			bytes[i + h2] = (byte) (in >> 8);
			bytes[i + l2] = (byte) in;
		}

		void set4(int in, int i) {
			set2(in >> 16, i + h4);
			set2(in, i + l4);
		}

		void set8(long lo, int i) {
			set4((int) (lo >> 32), i + h8);
			set4((int) lo, i + l8);
		}

	}

	/**
	 * RGBProcessor
	 */
	static class RGBProcessor {

		private int[][][] tree;
		private int[] pix, tbl;
		private int lim, count;
		private int rrggbb, rr, gg, bb;

		RGBProcessor(int[] pix, int lim) {
			this.pix = pix;
			this.lim = lim;
			tree = new int[256][][];
			tbl = new int[lim + 1];
		}

		/**
		 * count number of colors
		 */
		int count() {
			for (int i = 0; i < pix.length; i++) {
				splitRGB(pix[i]);

				if (tree[rr] == null) {
					if (overCount())
						break;
					tree[rr] = new int[256][];
					tree[rr][gg] = new int[256];
					addRGB(true);
				} else if (tree[rr][gg] == null) {
					if (overCount())
						break;
					tree[rr][gg] = new int[256];
					addRGB(true);
				} else if (tree[rr][gg][bb] < 0) {
					if (overCount())
						break;
					addRGB(false);
				}
			}
			return count;
		}

		/**
		 * check count over
		 */
		boolean overCount() {
			boolean over = count >= lim;
			if (over)
				count = -1;
			return over;
		}

		/**
		 * RGB -> R,G,B
		 */
		void splitRGB(int col) {
			rrggbb = col & 0xffffff;
			rr = (rrggbb >> 16);
			gg = (rrggbb >> 8) & 0xff;
			bb = rrggbb & 0xff;
		}

		/**
		 * register new color
		 */
		void addRGB(boolean init) {
			if (init) {
				for (int i = 0; i < 256; i++)
					tree[rr][gg][i] = -1;
			}
			tree[rr][gg][bb] = count;
			tbl[count] = rrggbb;
			count++;
		}

		/**
		 * get color table (RGBQuad)
		 */
		int[] getTable() {
			if (count > lim)
				return null;
			else
				return tbl;
		}

		/**
		 * color -> index
		 */
		int getIndex(int col) {
			splitRGB(col);
			return tree[rr][gg][bb];
		}
	}

}
