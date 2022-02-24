/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.postscript.truetypefont;

public class Util {

	private static int toInt(byte b) {
		return 0xff & b;
	}

	private static long toLong(byte b) {
		return 0xff & b;
	}

	private static int mergeInt(byte ch1, byte ch2, byte ch3, byte ch4) {
		int i1 = toInt(ch1);
		int i2 = toInt(ch2);
		int i3 = toInt(ch3);
		int i4 = toInt(ch4);
		return ((i1 << 24) + (i2 << 16) + (i3 << 8) + i4);
	}

	private static long mergeLong(byte ch1, byte ch2, byte ch3, byte ch4) {
		long i1 = toLong(ch1);
		long i2 = toLong(ch2);
		long i3 = toLong(ch3);
		long i4 = toLong(ch4);
		return ((i1 << 24) + (i2 << 16) + (i3 << 8) + i4);
	}

	private static byte[] get4Bytes(long data) {
		byte[] result = new byte[4];
		result[0] = (byte) (data >> 24);
		result[1] = (byte) (data >> 16);
		result[2] = (byte) (data >> 8);
		result[3] = (byte) data;
		return result;
	}

	private static byte[] get2Bytes(int data) {
		byte[] result = new byte[2];
		result[0] = (byte) (data >> 8);
		result[1] = (byte) data;
		return result;
	}

	public static String toHexString(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		int length = 0;
		for (int i = 0; i < bytes.length; i++) {
			result.append(toHexString(bytes[i]));
			length += 2;
			if (length > 80) {
				result.append("\n");
				length = 0;
			}
		}
		return result.toString();
	}

	private static String toHexString(byte b) {
		String result;
		result = Integer.toHexString(toInt(b));
		if (result.length() == 1) {
			result = "0" + result;
		}
		return result;
	}

	public static void putInt16(byte[] bytes, int index, int data) {
		assert bytes.length > index + 1;
		byte[] intBytes = get2Bytes(data);
		for (int i = 0; i < 2; i++) {
			bytes[index + i] = intBytes[i];
		}
	}

	public static void putInt32(byte[] bytes, int index, long data) {
		assert bytes.length > index + 3;
		byte[] intBytes = get4Bytes(data);
		for (int i = 0; i < 4; i++) {
			bytes[index + i] = intBytes[i];
		}
	}

	public static int getUnsignedShort(byte[] source, int index) {
		assert (source.length >= index + 2);
		return (((0xff & source[index]) << 8)) + (0xff & source[index + 1]);
	}

	public static int getInt(byte[] source, int index) {
		assert (source.length >= index + 4);
		return mergeInt(source[index], source[index + 1], source[index + 2], source[index + 3]);
	}

	public static int getIntLE(byte[] source, int index) {
		return mergeInt(source[index + 3], source[index + 2], source[index + 1], source[index]);
	}

	public static long getUnsignedInt(byte[] source, int index) {
		assert (source.length >= index + 4);
		return mergeLong(source[index], source[index + 1], source[index + 2], source[index + 3]);
	}

	public static float div(int dividend, int divisor) {
		return ((float) dividend) / ((float) divisor);
	}

	public static String toBase85String(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for (int i = 0; i <= data.length - 4; i += 4) {
			char[] base85String = toBase85String(getUnsignedInt(data, i));
			buffer.append(base85String);
			count += base85String.length;
			if (count > 80) {
				buffer.append('\n');
				count = 0;
			}
		}
		return buffer.toString();
	}

	private static char[] toBase85String(long data) {
		if (data == 0) {
			return new char[] { 'z' };
		}
		char[] result = new char[5];
		long tempData = data;
		for (int i = 0; i < 5; i++) {
			long number = tempData % 85;
			result[i] = getBase85Char(number);
			tempData = (tempData - number) / 85;
		}
		return result;
	}

	private static char getBase85Char(long number) {
		return (char) ('!' + number);
	}

	public static String toHexString(int c) {
		final String[] padding = { "0", "00", "000" };
		String result = Integer.toHexString(c);
		if (result.length() < 4) {
			result = padding[3 - result.length()] + result;
		}
		return result;
	}
}
