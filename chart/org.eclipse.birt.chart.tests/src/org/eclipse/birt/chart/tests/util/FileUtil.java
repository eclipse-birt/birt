/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.util;

import java.io.InputStream;

/**
 * 
 * FileUtil is a utility class that will compare the contents from one file to
 * another.
 * 
 */
public class FileUtil {

	/**
	 * computeFiles method compares two input streams and returns whether the
	 * contents of the input streams match.
	 * 
	 * @param left  - inputstream of the first resource
	 * @param right - inputstream of the second resource
	 * @return true if the contents match; otherwise, false is returned.
	 * @throws Exception thrown if io errors occur
	 */
	public static boolean compareFiles(InputStream left, InputStream right, boolean ignoreLineBreaks) throws Exception {

		int leftChar, rightChar;
		do {
			do {
				leftChar = left.read();
			} while (ignoreLineBreaks && (leftChar == 0x0D || leftChar == 0x0A));
			do {
				rightChar = right.read();
			} while (ignoreLineBreaks && (rightChar == 0x0D || rightChar == 0x0A));
			if (leftChar != rightChar) {
				return false;
			}
		} while (leftChar != -1 && rightChar != -1);
		return true;
	}

	public static boolean compareFiles(InputStream left, InputStream right) throws Exception {
		return compareFiles(left, right, false);
	}
}