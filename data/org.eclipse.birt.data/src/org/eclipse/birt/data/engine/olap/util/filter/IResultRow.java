
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
