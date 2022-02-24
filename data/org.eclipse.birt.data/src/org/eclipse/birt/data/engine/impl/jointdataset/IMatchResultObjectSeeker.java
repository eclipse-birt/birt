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
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The instance of IMatchResultObjectSeeker is used to find an IResultObject
 * which matchs the join condition.
 */
public interface IMatchResultObjectSeeker {
	/**
	 * Return the next IResultObject instance which matchs the join condition.
	 * 
	 * @param currentPrimaryIndex
	 * @return
	 * @throws DataException
	 */
	IResultObject getNextMatchedResultObject(int currentPrimaryIndex) throws DataException;

	/**
	 * Set the ResultIterator from which the IResultObject instance will be sought
	 * from.
	 * 
	 * @param ri
	 * @throws DataException
	 */
	void setResultIterator(IResultIterator ri) throws DataException;
}
