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

package org.eclipse.birt.report.data.oda.xml;

import org.eclipse.birt.report.data.oda.xml.i18n.Messages;

/**
 * This class hosts all constants used in xml driver.
 * 
 * @deprecated Please use DTP xml driver
 */
public final class Constants {

	private Constants() {
	}

	public static final int DATA_SOURCE_MAJOR_VERSION = 1;
	public static final int DATA_SOURCE_MINOR_VERSION = 0;
	public static final String DATA_SOURCE_PRODUCT_NAME = Messages.getString("Constants.DriverName");
	public static final int CACHED_RESULT_SET_LENGTH = 10000;
	public static final String APPCONTEXT_INPUTSTREAM = "org.eclipse.birt.report.data.oda.xml.inputStream";
	public static final String APPCONTEXT_CLOSEINPUTSTREAM = "org.eclipse.birt.report.data.oda.xml.closeInputStream";
	// The connection proporty that is used to give the relation information
	// string
	// to the driver.
	public static final String CONST_PROP_RELATIONINFORMATION = "RELATIONINFORMATION";

	// The connection property that gives the file name(s).Currently we only
	// support single file.
	public static final String CONST_PROP_FILELIST = "FILELIST";
}
