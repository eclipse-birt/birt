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

package org.eclipse.birt.data.engine.odi;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Used to process ResultObject before result object will be done sorting and
 * grouping.
 */

public interface IResultObjectEvent {
	/**
	 * Process the provided result object. This method may decide to exclude this
	 * object from the result set by returning false. This method may modify the
	 * object by setting custom field values.
	 * 
	 * @return true if resultObject should be accepted false if resultObject should
	 *         be excluded
	 * @throws DataException
	 */
	public boolean process(IResultObject resultObject, int rowIndex) throws DataException;

}
