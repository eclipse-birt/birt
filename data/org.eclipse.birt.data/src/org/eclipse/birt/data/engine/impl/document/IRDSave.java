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

package org.eclipse.birt.data.engine.impl.document;

import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Save necessary information into report document in the first generation time
 * of report document based on original data set or in the later time based on
 * result set.
 */
public interface IRDSave {

	/**
	 * @param currIndex, index of current row
	 * @param valueMap,  expression value of current row
	 * @throws DataException
	 */
	public void saveExprValue(int currIndex, Map valueMap) throws DataException;

	/**
	 * Notify save needs to be finished
	 * 
	 * @param currIndex, index of current row
	 * @throws DataException
	 */
	public void saveFinish(int currIndex) throws DataException;

	/**
	 * Save below information into report document: result class, group level
	 * information and subquery information
	 * 
	 * @param odiResult,    associated ODI result set
	 * @param groupLevel,   group level of current sub query
	 * @param subQueryInfo, row index information of current sub querys
	 * @throws DataException
	 */
	public void saveResultIterator(IResultIterator odiResult, int groupLevel, int[] subQueryInfo) throws DataException;

	/**
	 * Save QueryDefinition to report design.
	 * 
	 * @throws DataException
	 */
	public void saveStart() throws DataException;

}
