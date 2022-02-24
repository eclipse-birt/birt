/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.document.util;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Read the data from report document and wrap it as a data source for furthur
 * transformation.
 */
public interface IExprDataResultSet extends IDataSetPopulator {

	/**
	 * @return
	 */
	public IResultClass getResultClass();

	/**
	 * @return
	 * @throws DataException
	 */
	public IResultObject fetch() throws DataException;

	/**
	 * @return
	 * @throws DataException
	 */
	public int getCount() throws DataException;

	/**
	 *
	 */
	public void close() throws DataException;
}
