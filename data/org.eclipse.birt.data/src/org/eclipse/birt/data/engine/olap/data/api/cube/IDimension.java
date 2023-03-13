
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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import java.io.IOException;

import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 *
 */

public interface IDimension {
	int TIME_DIMESION_YEAR_TYPE = 0;
	int TIME_DIMESION_QUARTER_TYPE = 1;
	int TIME_DIMESION_MONTH_TYPE = 2;
	int TIME_DIMESION_WEEK_TYPE = 3;
	int TIME_DIMESION_DAY_TYPE = 4;
	int TIME_DIMESION_HOUR_TYPE = 5;
	int TIME_DIMESION_MINUTE_TYPE = 6;
	int TIME_DIMESION_SECOND_TYPE = 7;

	/**
	 *
	 * @return
	 */
	String getName();

	/**
	 * Return whether this dimesion is a time Dimension.
	 *
	 * @return
	 */
	boolean isTime();

	/**
	 *
	 * @return Hierarchy
	 */
	IHierarchy getHierarchy();

	/**
	 *
	 * @return
	 */
	int length();

	/**
	 *
	 * @return
	 */
	IDiskArray findAll() throws IOException;

	/**
	 *
	 *
	 */
	void close() throws IOException;

	/**
	 * @param stopSign
	 * @return
	 * @throws IOException
	 */
	IDiskArray getAllRows(StopSign stopSign) throws IOException;

}
