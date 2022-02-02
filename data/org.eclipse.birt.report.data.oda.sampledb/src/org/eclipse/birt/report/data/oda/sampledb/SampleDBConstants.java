/*******************************************************************************
 * Copyright (c) 2004, 2006 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.sampledb;

/**
 * Wrap the constants used in SampleDB.
 */
public class SampleDBConstants {
	// Driver class name. Note that this class does not actually exist. It's
	// only a name to identify this connection provider
	public static final String DRIVER_CLASS = "org.eclipse.birt.report.data.oda.sampledb.Driver";

	// URL accepted by this driver
	public static final String DRIVER_URL = "jdbc:classicmodels:sampledb";

	// ID of this plugin
	public static final String PLUGIN_ID = "org.eclipse.birt.report.data.oda.sampledb";

	public static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String SAMPLE_DB_SCHEMA = "ClassicModels";

}
