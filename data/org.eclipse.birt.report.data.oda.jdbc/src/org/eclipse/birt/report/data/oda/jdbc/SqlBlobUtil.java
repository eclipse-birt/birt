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

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate a new java.sql.Blob instance by accept input stream as parameter.
 */
public class SqlBlobUtil {

	/**
	 * @param inputStream
	 * @return a java.sql.Blob instance
	 */
	public static java.sql.Blob newBlob(InputStream inputStream) {
		return new BlobHelper(inputStream);
	}

	/**
	 * For some data base, such as MS access, the blob data can not be retrieved
	 * from and instead it can be retrieved by getBinaryStream method.
	 */
	private static class BlobHelper implements java.sql.Blob {
		// input stream
		private InputStream inputStream;

		private boolean isInit;
		private int blobLength = -1;
		private byte[] blobContent = null;

		/**
		 * @param inputStream
		 */
		BlobHelper(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		/**
		 * read stream to cache
		 *
		 * @throws SQLException
		 */
		private void init() throws SQLException {
			if (isInit) {
				return;
			}

			List byteList = new ArrayList();
			int b;
			try {
				while ((b = inputStream.read()) != -1) {
					byteList.add(Integer.valueOf(b));
				}
				inputStream.close();
			} catch (IOException e1) {
				throw new SQLException("can not read from blob data");
			}

			blobLength = byteList.size();
			blobContent = new byte[blobLength];

			for (int i = 0; i < blobLength; i++) {
				blobContent[i] = (byte) ((Integer) byteList.get(i)).intValue();
			}

			byteList = null;
			isInit = true;
		}

		/*
		 * @see java.sql.Blob#length()
		 */
		@Override
		public long length() throws SQLException {
			init();

			return this.blobLength;
		}

		/*
		 * @see java.sql.Blob#truncate(long)
		 */
		@Override
		public void truncate(long len) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		/*
		 * @see java.sql.Blob#getBytes(long, int)
		 */
		@Override
		public byte[] getBytes(long pos, int length) throws SQLException {
			init();

			int destPos = (int) ((pos - 1) + length);
			if (destPos > this.blobLength) // since pos is 1-based
			{
				throw new SQLException("pos or length is not valid");
			} else {
				int startPos = (int) (pos - 1);
				byte[] content = new byte[length];
				System.arraycopy(blobContent, startPos, content, startPos, length);
				return content;
			}
		}

		/*
		 * @see java.sql.Blob#setBytes(long, byte[])
		 */
		@Override
		public int setBytes(long pos, byte[] bytes) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		/*
		 * @see java.sql.Blob#setBytes(long, byte[], int, int)
		 */
		@Override
		public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		/*
		 * @see java.sql.Blob#position(byte[], long)
		 */
		@Override
		public long position(byte[] pattern, long start) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		/*
		 * @see java.sql.Blob#getBinaryStream()
		 */
		@Override
		public InputStream getBinaryStream() throws SQLException {
			init();

			return new ByteArrayInputStream(this.blobContent);
		}

		/*
		 * @see java.sql.Blob#setBinaryStream(long)
		 */
		@Override
		public OutputStream setBinaryStream(long pos) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		/*
		 * @see java.sql.Blob#position(java.sql.Blob, long)
		 */
		@Override
		public long position(Blob pattern, long start) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		@Override
		public void free() throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

		@Override
		public InputStream getBinaryStream(long pos, long length) throws SQLException {
			throw new SQLException("Unsupported in this database");
		}

	}

}
