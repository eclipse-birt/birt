/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
