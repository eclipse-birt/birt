/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.xml.util.ui;

import java.util.List;

/**
 * This class is a Utility class which is used to help UI to populate the list
 * of possible XPath Expressions.
 * 
 * @deprecated Please use DTP xml driver
 */

public final class XPathPopulationUtil {
	/**
	 * This method is used to populate the possible root path expressions List
	 * 
	 * @param absolutePath must be the absolute path of root path
	 * @return
	 */
	public static List populateRootPath(String absolutePath) {
		return org.eclipse.datatools.enablement.oda.xml.util.ui.XPathPopulationUtil.populateRootPath(absolutePath);
	}

	/**
	 * This method is used to populate the possible column path expressions List
	 * 
	 * @param rootPath   the root path of the table the column in, must be absolute
	 *                   path.
	 * @param columnPath the absolute column path.
	 * @return
	 */
	public static String populateColumnPath(String rootPath, String columnPath) {
		return org.eclipse.datatools.enablement.oda.xml.util.ui.XPathPopulationUtil.populateColumnPath(rootPath,
				columnPath);
	}
}
