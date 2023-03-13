/*
 *************************************************************************
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
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class TestData {
	private static final double BIG_DECIMAL_DATA = 1234567890.12;
	private static final String CLOB_DATA = "abcdefghijklmnopqrstuvwxyz"; //$NON-NLS-1$
	private static final String STRING_DATA = "String data"; //$NON-NLS-1$
	private static final double DOUBLE_DATA = 123.4567890123;
	private static final int INT_DATA = 2147483647;

	// Time data for Wed, 1 Nov 2006 10:30:30 UTC,
	// represented in milliseconds since January 1, 1970, 00:00:00 GMT
	private static final long TIME_DATA = 1162377030;

	// Timestamp data for Thu, 2 Nov 2006 10:30:30 UTC,
	// represented in milliseconds since January 1, 1970, 00:00:00 GMT
	private static final long TIMESTAMP_DATA = 1162463430;

	// Date data for Fri, 3 Nov 2006,
	// represented in milliseconds since January 1, 1970, 00:00:00 GMT
	private static final long DATE_DATA = 1162512000;

	public static final boolean createBooleanTrueData() {
		return true;
	}

	public static final boolean createBooleanFalseData() {
		return false;
	}

	public static final BigDecimal createBigDecimalData() {
		return new BigDecimal(BIG_DECIMAL_DATA);
	}

	public static final String createClobDataString() {
		return CLOB_DATA;
	}

	public static final String createStringData() {
		return STRING_DATA;
	}

	public static final double createDoubleData() {
		return DOUBLE_DATA;
	}

	public static final int createIntData() {
		return INT_DATA;
	}

	public static final Time createTimeData() {
		return new Time(TIME_DATA);
	}

	public static final Timestamp createTimestampData() {
		return new Timestamp(TIMESTAMP_DATA);
	}

	public static final Date createDateData() {
		return new Date(DATE_DATA);
	}

	public static boolean checkBlobData(IBlob val1, IBlob val2) throws OdaException {
		if (val1 == val2) {
			return true;
		}

		// Check the content of the blob's byte array.
		try {
			// Obtain the byte data as an InputStream's. Reset
			// the streams to their starting points.
			InputStream is1 = val1.getBinaryStream();
			is1.reset();
			InputStream is2 = val2.getBinaryStream();
			is2.reset();

			// Check if they have the same content.
			return streamsContentEqual(is1, is2);
		} catch (IOException e) {
			throw new OdaException(e);
		}
	}

	private static boolean streamsContentEqual(InputStream is1, InputStream is2) throws OdaException {
		try {
			if (is1 == is2) {
				return true;
			}

			if (is1 == null || is2 == null) { // only one has contents
				return false;
			}

			while (true) {
				int c1 = is1.read();
				int c2 = is2.read();
				if (c1 == -1 && c2 == -1) {
					return true;
				}
				if (c1 != c2) {
					break;
				}
			}
		} catch (IOException ex) {
			throw new OdaException(ex);
		} finally {
			try {
				if (is1 != null) {
					is1.close();
				}
			} catch (IOException e) {
				throw new OdaException(e);
			}

			try {
				if (is2 != null) {
					is2.close();
				}
			} catch (IOException e) {
				throw new OdaException(e);
			}
		}

		return false;
	}

	public static boolean checkClobData(IClob val, String clobData) throws OdaException {
		String valStr = getClobDataAsString(val);
		if (valStr == null) {
			return (clobData == null);
		}

		return valStr.equals(clobData);
	}

	public static boolean checkClobData(IClob val1, IClob val2) throws OdaException {
		String val2Str = getClobDataAsString(val2);
		return checkClobData(val1, val2Str);
	}

	private static String getClobDataAsString(IClob val) throws OdaException {
		if (val == null) {
			return null;
		}

		Reader is = val.getCharacterStream();
		if (is == null) {
			return null;
		}

		try {
			String outStr = ""; //$NON-NLS-1$
			for (int index = 0;; index++) {
				int c = is.read();
				if (c == -1) {
					return outStr;
				}

				outStr += (char) c;
			}
		} catch (IOException ex) {
			throw new OdaException(ex);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				throw new OdaException(e);
			}
		}
	}
}
