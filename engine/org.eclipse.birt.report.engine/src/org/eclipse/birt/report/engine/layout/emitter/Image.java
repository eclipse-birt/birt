/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.emitter;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

public class Image {

	public static final int FORMAT_JPEG = 0;

	public static final int FORMAT_GIF = 1;

	public static final int FORMAT_PNG = 2;

	public static final int FORMAT_BMP = 3;

	private static final String[] FORMAT_NAMES = { "JPEG", "GIF", "PNG", "BMP" };

	private static final String[] MIME_TYPE_STRINGS = { "image/jpeg", "image/gif", "image/png", "image/bmp" };

	private int width;
	private int height;
	private int bitsPerPixel;
	private boolean progressive;
	private int format;
	private InputStream in;
	private DataInput dataIn;
	private boolean collectComments = true;
	private int heightDpi;
	private int widthDpi;
	private byte[] data;

	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean check() {
		format = -1;
		width = -1;
		height = -1;
		bitsPerPixel = -1;
		heightDpi = -1;
		widthDpi = -1;
		try {
			int byte1 = read() & 0xff;
			int byte2 = read() & 0xff;
			if (byte1 == 0x47 && byte2 == 0x49) {
				return checkGif();
			} else if (byte1 == 0x89 && byte2 == 0x50) {
				return checkPng();
			} else if (byte1 == 0xff && byte2 == 0xd8) {
				return checkJpeg();
			} else if (byte1 == 0x42 && byte2 == 0x4d) {
				return checkBmp();
			} else {
				return false;
			}
		} catch (IOException ioe) {
			return false;
		}
	}

	private boolean checkBmp() throws IOException {
		byte[] array = new byte[44];
		if (read(array) != array.length) {
			return false;
		}

		width = getIntLittleEndian(array, 16);
		height = getIntLittleEndian(array, 20);
		if (width < 1 || height < 1) {
			return false;
		}

		bitsPerPixel = getShortLittleEndian(array, 26);
		if (bitsPerPixel != 1 && bitsPerPixel != 4 && bitsPerPixel != 8 && bitsPerPixel != 16 && bitsPerPixel != 24
				&& bitsPerPixel != 32) {
			return false;
		}

		int physicalWidthDpi = (int) (getIntLittleEndian(array, 36) * 0.0254);
		if (physicalWidthDpi > 0) {
			setPhysicalWidthDpi(physicalWidthDpi);
		}

		int physicalHeightDpi = (int) (getIntLittleEndian(array, 40) * 0.0254);
		if (physicalHeightDpi > 0) {
			setPhysicalHeightDpi(physicalHeightDpi);
		}

		format = FORMAT_BMP;
		return true;
	}

	private boolean checkGif() throws IOException {
		final byte[] GIF_MAGIC_87A = { 0x46, 0x38, 0x37, 0x61 };
		final byte[] GIF_MAGIC_89A = { 0x46, 0x38, 0x39, 0x61 };

		byte[] array = new byte[11];
		if ((read(array) != array.length) || ((!equals(array, 0, GIF_MAGIC_89A, 0, 4)) && (!equals(array, 0, GIF_MAGIC_87A, 0, 4)))) {
			return false;
		}

		format = FORMAT_GIF;
		width = getShortLittleEndian(array, 4);
		height = getShortLittleEndian(array, 6);
		int flags = array[8] & 0xff;
		bitsPerPixel = ((flags >> 4) & 0x07) + 1;
		if ((flags & 0x80) != 0) {
			int tableSize = (1 << ((flags & 7) + 1)) * 3;
			skip(tableSize);
		}
		int blockType;
		do {
			blockType = read();
			switch (blockType) {
			case (0x2c):
				if (read(array, 0, 9) != 9) {
					return false;
				}
				flags = array[8] & 0xff;
				progressive = (flags & 0x40) != 0;
				int localBitsPerPixel = (flags & 0x07) + 1;
				if (localBitsPerPixel > bitsPerPixel) {
					bitsPerPixel = localBitsPerPixel;
				}
				if ((flags & 0x80) != 0) {
					skip((1 << localBitsPerPixel) * 3);
				}
				skip(1);
				int n;
				do {
					n = read();
					if (n > 0) {
						skip(n);
					} else if (n == -1) {
						return false;
					}
				} while (n > 0);
				break;
			case (0x21):
				int extensionType = read();
				if (collectComments && extensionType == 0xfe) {
					StringBuilder sb = new StringBuilder();
					int n1;
					do {
						n1 = read();
						if (n1 == -1) {
							return false;
						}
						if (n1 > 0) {
							for (int i = 0; i < n1; i++) {
								int ch = read();
								if (ch == -1) {
									return false;
								}
								sb.append((char) ch);
							}
						}
					} while (n1 > 0);
				} else {
					int m;
					do {
						m = read();
						if (m > 0) {
							skip(m);
						} else if (m == -1) {
							return false;
						}
					} while (m > 0);
				}
				break;
			case (0x3b):
				break;
			default:
				return false;
			}
		} while (blockType != 0x3b);
		return true;
	}

	private boolean checkJpeg() throws IOException {
		byte[] array = new byte[12];
		while (true) {
			if (read(array, 0, 4) != 4) {
				return false;
			}
			int marker = getShortBigEndian(array, 0);
			int size = getShortBigEndian(array, 2);
			if ((marker & 0xff00) != 0xff00) {
				return false;
			}
			if (marker == 0xffe0) {
				if (size < 14) {
					skip(size - 2);
					continue;
				}
				if (read(array, 0, 12) != 12) {
					return false;
				}
				final byte[] APP0_ID = { 0x4a, 0x46, 0x49, 0x46, 0x00 };
				if (equals(APP0_ID, 0, array, 0, 5)) {
					int physicalWidthDpi = getShortBigEndian(array, 8);
					int physicalHeightDpi = getShortBigEndian(array, 10);
					if (array[7] == 1) {
						setPhysicalWidthDpi(physicalWidthDpi);
						setPhysicalHeightDpi(physicalHeightDpi);
					} else if (array[7] == 2) {
						setPhysicalWidthDpi((int) (physicalWidthDpi * 2.54f));
						setPhysicalHeightDpi((int) (physicalHeightDpi * 2.54f));
					}
				}
				skip(size - 14);
			} else if (collectComments && size > 2 && marker == 0xfffe) { // comment
				size -= 2;
				byte[] chars = new byte[size];
				if (read(chars, 0, size) != size) {
					return false;
				}
				String comment = new String(chars, "iso-8859-1");
				comment = comment.trim();
			} else if (marker >= 0xffc0 && marker <= 0xffcf && marker != 0xffc4 && marker != 0xffc8) {
				if (read(array, 0, 6) != 6) {
					return false;
				}
				format = FORMAT_JPEG;
				bitsPerPixel = (data[0] & 0xff) * (data[5] & 0xff);
				progressive = marker == 0xffc2 || marker == 0xffc6 || marker == 0xffca || marker == 0xffce;
				width = getShortBigEndian(array, 3);
				height = getShortBigEndian(array, 1);
				return true;
			} else {
				skip(size - 2);
			}
		}
	}

	private boolean checkPng() throws IOException {
		final byte[] PNG_MAGIC = { 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };
		byte[] array = new byte[27];
		if ((read(array) != array.length) || !equals(array, 0, PNG_MAGIC, 0, 6)) {
			return false;
		}
		format = FORMAT_PNG;
		width = getIntBigEndian(array, 14);
		height = getIntBigEndian(array, 18);
		bitsPerPixel = array[22] & 0xff;
		int colorType = array[23] & 0xff;
		if (colorType == 2 || colorType == 6) {
			bitsPerPixel *= 3;
		}
		progressive = (array[26] & 0xff) != 0;
		return true;
	}

	private static boolean equals(byte[] a1, int offs1, byte[] a2, int offs2, int num) {
		while (num-- > 0) {
			if (a1[offs1++] != a2[offs2++]) {
				return false;
			}
		}
		return true;
	}

	public int getBitsPerPixel() {
		return bitsPerPixel;
	}

	public int getFormat() {
		return this.format;
	}

	public String getFormatName() {
		return format >= 0 && format < FORMAT_NAMES.length ? FORMAT_NAMES[format] : "?";
	}

	public int getHeight() {
		return this.height;
	}

	private static int getIntBigEndian(byte[] array, int offset) {
		return (array[offset] & 0xff) << 24 | (array[offset + 1] & 0xff) << 16 | (array[offset + 2] & 0xff) << 8
				| array[offset + 3] & 0xff;
	}

	private static int getIntLittleEndian(byte[] array, int offset) {
		return (array[offset + 3] & 0xff) << 24 | (array[offset + 2] & 0xff) << 16 | (array[offset + 1] & 0xff) << 8
				| array[offset] & 0xff;
	}

	public String getMimeType() {
		if (format >= 0 && format < MIME_TYPE_STRINGS.length) {
			if (format == FORMAT_JPEG && progressive) {
				return "image/pjpeg";
			}
			return MIME_TYPE_STRINGS[format];
		} else {
			return null;
		}
	}

	public int getPhysicalHeightDpi() {
		return this.heightDpi;
	}

	public float getPhysicalHeightInch() {
		if (height > 0 && heightDpi > 0) {
			return ((float) height) / ((float) heightDpi);
		}
		return -1.0f;
	}

	public int getPhysicalWidthDpi() {
		return this.widthDpi;
	}

	public float getPhysicalWidthInch() {
		if (width > 0 && widthDpi > 0) {
			return ((float) width) / ((float) widthDpi);
		}
		return -1.0f;
	}

	public byte[] getData() {
		return data;
	}

	private static int getShortBigEndian(byte[] a, int offs) {
		return (a[offs] & 0xff) << 8 | (a[offs + 1] & 0xff);
	}

	private static int getShortLittleEndian(byte[] a, int offs) {
		return (a[offs] & 0xff) | (a[offs + 1] & 0xff) << 8;
	}

	public int getWidth() {
		return this.width;
	}

	private int read() throws IOException {
		return in != null ? in.read() : dataIn.readByte();
	}

	private int read(byte[] a) throws IOException {
		if (in != null) {
			return in.read(a);
		}
		dataIn.readFully(a);
		return a.length;
	}

	private int read(byte[] a, int offset, int num) throws IOException {
		if (in != null) {
			return in.read(a, offset, num);
		}
		dataIn.readFully(a, offset, num);
		return num;
	}

	public void setInput(DataInput din) {
		this.dataIn = din;
		this.in = null;
	}

	public void setInput(byte[] data) {
		this.data = data;
		setInput(new ByteArrayInputStream(data));
	}

	public void setInput(InputStream in) {
		this.in = in;
		this.dataIn = null;
	}

	private void setPhysicalHeightDpi(int physicalWidthDpi) {
		this.widthDpi = physicalWidthDpi;
	}

	private void setPhysicalWidthDpi(int physicalHeightDpi) {
		this.heightDpi = physicalHeightDpi;
	}

	private void skip(int num) throws IOException {
		while (num > 0) {
			long result = in != null ? in.skip(num) : dataIn.skipBytes(num);

			if (result > 0) {
				num -= result;
			} else {
				result = in != null ? in.read() : dataIn.readByte();
				if (result == -1) {
					throw new IOException("Premature end of input.");
				} else {
					num--;
				}
			}
		}
	}
}
