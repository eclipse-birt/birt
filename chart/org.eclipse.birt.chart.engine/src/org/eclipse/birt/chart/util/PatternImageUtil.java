/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.util;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.PatternImage;

/**
 * Utilities to handle PatternImage
 */

public class PatternImageUtil {

	private static long[] BIT_MASK = initBitMasks();

	private static long[] initBitMasks() {
		int len = 64;
		long[] bitMasks = new long[len];
		for (int i = 0; i < len; i++) {
			bitMasks[i] = (long) 1 << i;
		}
		return bitMasks;
	}

	private static boolean validateIndex(int x, int y) {
		return x < 8 && x >= 0 && y < 8 && y >= 0;
	}

	public static boolean isPixelSet(long bitmap, int x, int y) {
		if (validateIndex(x, y)) {
			int index = x + y * 8;
			return (bitmap & BIT_MASK[index]) != 0;
		}
		return false;
	}

	public static long togglePixel(long bitmap, int x, int y) {
		if (validateIndex(x, y)) {
			int index = x + y * 8;
			bitmap ^= BIT_MASK[index];
		}
		return bitmap;
	}

	public static enum ByteColorModel {
		BGRA {

			@Override
			public byte[] getColorValue(ColorDefinition color) {
				byte[] data = new byte[4];
				data[0] = (byte) color.getBlue();
				data[1] = (byte) color.getGreen();
				data[2] = (byte) color.getRed();
				data[3] = (byte) color.getTransparency();
				return data;
			}
		},
		RGBA {

			@Override
			public byte[] getColorValue(ColorDefinition color) {
				byte[] data = new byte[4];
				data[0] = (byte) color.getRed();
				data[1] = (byte) color.getGreen();
				data[2] = (byte) color.getBlue();
				data[3] = (byte) color.getTransparency();
				return data;
			}
		};

		public abstract byte[] getColorValue(ColorDefinition color);
	}

	public static enum IntColorModel {
		BGRA {

			@Override
			public int getColorValue(ColorDefinition color) {
				return color.getBlue() << 24 | color.getGreen() << 16 | color.getRed() << 8 | color.getTransparency();
			}
		},
		RGBA {

			@Override
			public int getColorValue(ColorDefinition color) {
				return color.getTransparency() << 24 | color.getBlue() << 16 | color.getGreen() << 8 | color.getRed();
			}
		};

		public abstract int getColorValue(ColorDefinition color);
	}

	public static byte[] createImageData(PatternImage pi, ByteColorModel colorModel) {
		byte[] valFore = colorModel.getColorValue(pi.getForeColor());
		byte[] valBack = colorModel.getColorValue(pi.getBackColor());
		byte[] data = new byte[64 * 4];
		long bitmap = pi.getBitmap();

		int offset = 0;
		for (int i = 0; i < 64; i++) {
			byte[] val = (bitmap & BIT_MASK[i]) != 0 ? valFore : valBack;
			System.arraycopy(val, 0, data, offset, 4);
			offset += 4;
		}

		return data;
	}

	public static int[] createImageData(PatternImage pi, IntColorModel colorModel) {
		int valFore = colorModel.getColorValue(pi.getForeColor());
		int valBack = colorModel.getColorValue(pi.getBackColor());
		int[] data = new int[64];
		long bitmap = pi.getBitmap();

		for (int i = 0; i < 64; i++) {
			data[i] = (bitmap & BIT_MASK[i]) != 0 ? valFore : valBack;
		}

		return data;
	}

}
