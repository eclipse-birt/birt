
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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import java.io.IOException;

import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * 
 */

public interface IDimension {
	public static final int TIME_DIMESION_YEAR_TYPE = 0;
	public static final int TIME_DIMESION_QUARTER_TYPE = 1;
	public static final int TIME_DIMESION_MONTH_TYPE = 2;
	public static final int TIME_DIMESION_WEEK_TYPE = 3;
	public static final int TIME_DIMESION_DAY_TYPE = 4;
	public static final int TIME_DIMESION_HOUR_TYPE = 5;
	public static final int TIME_DIMESION_MINUTE_TYPE = 6;
	public static final int TIME_DIMESION_SECOND_TYPE = 7;

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Return whether this dimesion is a time Dimension.
	 * 
	 * @return
	 */
	public boolean isTime();

	/**
	 * 
	 * @return Hierarchy
	 */
	public IHierarchy getHierarchy();

	/**
	 * 
	 * @return
	 */
	public int length();

	/**
	 * 
	 * @return
	 */
	public IDiskArray findAll() throws IOException;

	/**
	 * 
	 *
	 */
	public void close() throws IOException;

	/**
	 * @param stopSign
	 * @return
	 * @throws IOException
	 */
	public IDiskArray getAllRows(StopSign stopSign) throws IOException;

}
