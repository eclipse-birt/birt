/*******************************************************************************
 * Copyright (c) 2004, 2013 Actuate Corporation.
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
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.hive;

/**
 * Constants for the Hive ODA data source properties.
 */
public class HiveConstants {
	// ID of this plugin
	public static final String PLUGIN_ID = "org.eclipse.birt.report.data.oda.hive"; //$NON-NLS-1$

	// Driver class name. Note that this class does not actually exist. It's
	// only a name to identify this connection provider
	public static final String DRIVER_CLASS = "org.eclipse.birt.report.data.oda.hive.HiveDriver"; //$NON-NLS-1$

	// URL accepted by this driver
	/**
	 * @deprecated since 4.2.3; replaced by {@link HS1_DEFAULT_URL}
	 */
	public static final String DRIVER_URL = "jdbc:hive://localhost:10000/default"; //$NON-NLS-1$

	/**
	 * @deprecated since 4.2.3; replaced by {@link HS1_JDBC_DRIVER_CLASS}
	 */
	public static final String HIVE_DRIVER_CLASS = "org.apache.hadoop.hive.jdbc.HiveDriver"; //$NON-NLS-1$

	public static final String HIVE_ADD_FILE_PROPERTY = "addListFile"; //$NON-NLS-1$

	/**
	 * Support for HiveServer1 and HiveServer2
	 * 
	 * @since 4.2.3
	 */
	public static final String HS1_JDBC_DRIVER_CLASS = HIVE_DRIVER_CLASS;
	public static final String HS1_URL_PROTOCOL = "jdbc:hive:"; //$NON-NLS-1$
	private static final String DEFAULT_URL_SERVER = "//localhost:10000/default"; //$NON-NLS-1$
	public static final String HS1_DEFAULT_URL = HS1_URL_PROTOCOL + DEFAULT_URL_SERVER;

	public static final String HS2_JDBC_DRIVER_CLASS = "org.apache.hive.jdbc.HiveDriver"; //$NON-NLS-1$
	public static final String HS2_URL_PROTOCOL = "jdbc:hive2:"; //$NON-NLS-1$
	public static final String HS2_DEFAULT_URL = HS2_URL_PROTOCOL + DEFAULT_URL_SERVER;

	// Utility methods to format the specified database URL string to use the
	// appropriate HiveServer protocol

	/**
	 * Formats the specified JDBC connection URL string to adopt the HiveServer1
	 * protocol.
	 * 
	 * @param dbURL a JDBC connection URL
	 * @return JDBC connection URL formatted with the HiveServer1 protocol
	 */
	public static String formatHiveServer1URL(String dbURL) {
		return resetURLProtocol(dbURL, HS1_URL_PROTOCOL);
	}

	/**
	 * Formats the specified JDBC connection URL string to adopt the HiveServer2
	 * protocol.
	 * 
	 * @param dbURL a JDBC connection URL
	 * @return JDBC connection URL formatted with the HiveServer2 protocol
	 */
	public static String formatHiveServer2URL(String dbURL) {
		return resetURLProtocol(dbURL, HS2_URL_PROTOCOL);
	}

	private static String resetURLProtocol(String dbURL, final String toProtocol) {
		if (dbURL == null)
			return dbURL; // nothing to format

		String trimmedURL = dbURL.trim();
		if (trimmedURL.length() == 0 || trimmedURL.startsWith(toProtocol))
			return trimmedURL; // empty, or already has requested protocol

		// replace existing URL protocol, if exists
		int slashIndex = trimmedURL.indexOf("//"); //$NON-NLS-1$
		if (slashIndex >= 0)
			return toProtocol + trimmedURL.substring(slashIndex);

		return trimmedURL; // return as is, trimmed
	}

}
