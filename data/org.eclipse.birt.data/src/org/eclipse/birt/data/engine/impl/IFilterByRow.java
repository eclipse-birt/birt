/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import java.util.List;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

public interface IFilterByRow extends IResultObjectEvent {
	/**
	 * @return A list of <code>IFilterDefinition</code>.
	 * @throws DataException If any error occurs.
	 */
	List<IFilterDefinition> getFilterList() throws DataException;

	/**
	 * Set current working filters.
	 *
	 * @param filterSetType Filter set type
	 * @throws DataException If any error occurs.
	 */
	void setWorkingFilterSet(int filterSetType) throws DataException;

	/**
	 * Close IFilterByRow and release allocated resources.
	 */
	void close() throws DataException;
}
