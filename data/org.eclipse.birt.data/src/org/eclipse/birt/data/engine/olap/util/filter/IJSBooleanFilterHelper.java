/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

public interface IJSBooleanFilterHelper {

	/**
	 * This method is used to evaluate the filter expression.
	 * 
	 * @param expr
	 * @param resultRow
	 * @return
	 * @throws DataException
	 */
	public boolean evaluateFilter(IResultRow resultRow) throws DataException;

}
