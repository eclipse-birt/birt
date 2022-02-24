
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
package org.eclipse.birt.data.engine.olap.util.filter;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface IResultRow {
	/**
	 * 
	 * @return
	 */
	public boolean isTimeDimensionRow();

	/**
	 * get the field value according to the specified field name.
	 * 
	 * @param fieldName
	 * @return
	 * @throws DataException
	 */
	public Object getFieldValue(String fieldName) throws DataException;

	/**
	 * get the aggregation values according to teh specified aggregation name.
	 * 
	 * @param aggrName
	 * @return
	 * @throws DataException
	 */
	public Object getAggrValue(String aggrName) throws DataException;
}
