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
 * This interface defined the methods that would be used by classes that use sax parser.
 */
public interface ISaxParserConsumer
{
	/**
	 * The method is used to populate one column that is specified by path in current row.
	 * @param path
	 * @param value
	 */
	public void manipulateData(String path, String value);
	
	/**
	 * Indicate whether a new row should started according to the given xPath expression.
	 * A new row would only started when the given xPath matches the root path of certain
	 * table.
	 * @param path
	 * @param start
	 */
	public void detectNewRow( String path, boolean start );
	
	/**
	 * This method is used by sax parser to notify the ISaxParserConsumer so that it can
	 * be active rather than blocked.
	 * 
	 */
	public void wakeup();
}
