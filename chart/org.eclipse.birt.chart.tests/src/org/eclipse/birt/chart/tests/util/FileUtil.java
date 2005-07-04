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
 * FileUtil is a utility class that will compare the contents from one file to another.
 * 
 */
public class FileUtil {

/**
 * computeFiles method compares two input streams and returns whether the contents of the input streams match.
 * @param left - inputstream of the first resource
 * @param right - inputstream of the second resource
 * @return true if the contents match; otherwise, false is returned.
 * @throws Exception thrown if io errors occur
 */
	public static boolean compareFiles(InputStream left, InputStream right)
			throws Exception {
		int leftChar = -1;
		while ((leftChar = left.read()) != -1){
			if (leftChar != right.read())
				return false;
			
		}
		return true;
	}
}