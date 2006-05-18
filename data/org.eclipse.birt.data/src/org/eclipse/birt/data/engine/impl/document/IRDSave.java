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

package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * 
 */
public interface IRDSave
{
	/**
	 * @param currIndex
	 * @param exprID
	 * @param exprValue
	 */
	public void saveExprValue( int currIndex, String exprID,
			Object exprValue ) throws DataException;

	/**
	 * Notify save needs to be finished
	 */
	public void saveFinish( int currIndex ) throws DataException;

	/**
	 * Save below information into report document:
	 * 		result class
	 * 		group information
	 * 		subquery information
	 *  
	 * @param odiResult
	 * @throws DataException
	 */
	public void saveResultIterator( IResultIterator odiResult,
			int groupLevel, int[] subQueryInfo ) throws DataException;

}