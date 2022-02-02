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

package org.eclipse.birt.chart.device.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import org.eclipse.birt.chart.device.extension.i18n.Messages;

import com.ibm.icu.util.ULocale;

/**
 * Class representing a single PNG chunk.
 */
class Chunk {

	static final String CHUNK_IHDR = "IHDR"; //$NON-NLS-1$

	static final String CHUNK_PLTE = "PLTE"; //$NON-NLS-1$

	static final String CHUNK_TRNS = "tRNS"; //$NON-NLS-1$

	static final String CHUNK_IDAT = "IDAT"; //$NON-NLS-1$

	static final String CHUNK_IEND = "IEND"; //$NON-NLS-1$

	/** Data for chunk */
	private ByteArrayOutputStream data;

	/** Type of chunk */
	private String type;

	/**
	 * Constructor.
	 * 
	 * @param type Chunk type (4 character string)
	 */
	Chunk(String type) {
		this.type = type;
		if (type == null || type.length() != 4) {
			throw new IllegalArgumentException(Messages.getString("Chunk.exception.invalid.png.chunk", //$NON-NLS-1$
					new Object[] { type }, ULocale.getDefault()));
		}
		this.data = new ByteArrayOutputStream();
	}

	/**
	 * Writes an integer to the internal data stream.
	 */
	void write(int value) {
		data.write(intToBytes(value), 0, 4);
	}

	/**
	 * Writes an integer to the internal data stream.
	 */
	void writeInt(int value) {
		write(value);
	}

	/**
	 * Writes a short integer to the internal data stream.
	 */
	void write(short value) {
		data.write((value & 0xff) >>> 8);
		data.write(value & 0xff);
	}

	/**
	 * Writes a short integer to the internal data stream.
	 */
	void writeShort(int value) {
		write((short) value);
	}

	/**
	 * Writes a byte to the internal data stream.
	 */
	void write(byte value) {
		data.write(value);
	}

	/**
	 * Writes a byte to the internal data stream.
	 */
	void writeByte(int value) {
		write((byte) value);
	}

	/**
	 * Writes a block of data to the internal data stream.
	 */
	void write(byte[] block) {
		data.write(block, 0, block.length);
	}

	/**
	 * Returns the data stream. The returned output stream should <b>not </b> be
	 * closed by the caller.
	 */
	ByteArrayOutputStream getDataStream() {
		return data;
	}

	/**
	 * Writes the entire chunk to a given output stream. This does not close either
	 * the output stream or the internal data stream, so multiple calls to this
	 * method may be made if necessary.
	 */
	void output(OutputStream out) throws IOException {
		CRC32 crc = new CRC32();
		byte[] dataBytes = data.toByteArray();
		byte[] lenBytes = intToBytes(dataBytes.length);
		out.write(lenBytes);
		for (int i = 0; i < 4; i++) {
			crc.update(type.charAt(i));
			out.write(type.charAt(i));
		}
		crc.update(dataBytes);
		out.write(dataBytes);
		out.write(intToBytes((int) crc.getValue()));
	}

	private static byte[] intToBytes(int value) {
		byte[] ret = new byte[4];
		ret[0] = (byte) ((value & 0xff000000) >>> 24);
		ret[1] = (byte) ((value & 0x00ff0000) >>> 16);
		ret[2] = (byte) ((value & 0x0000ff00) >>> 8);
		ret[3] = (byte) ((value & 0x000000ff));
		return ret;
	}

	/**
	 * Closes the chunk's internal data stream. After this method has been called,
	 * no other methods may be called.
	 */
	void close() {
		IOUtil.close(data);
		data = null; // Make further calls fail nastily
	}
}
