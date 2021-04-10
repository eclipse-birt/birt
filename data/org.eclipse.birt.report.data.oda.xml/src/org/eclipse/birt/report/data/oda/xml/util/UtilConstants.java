/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.xml.util;

/**
 * The class server as a collection of Constants.
 * 
 * @deprecated Please use DTP xml driver
 */
public final class UtilConstants {

	private UtilConstants() {
	}

	public static final String XPATH_WITH_ATTR_PATTERN = ".*\\Q[@\\E.*\\Q]\\E.*";
	public static final String XPATH_ELEM_WITH_INDEX_REF_PATTERN = ".*\\Q[\\E\\d+\\Q]\\E.*";
	public static final String XPATH_ELEM_INDEX_PATTERN = "\\Q[\\E\\d+\\Q]\\E";
	public static final String XPATH_DOUBLE_SLASH = "//";
	public static final String XPATH_SLASH = "/";
}
