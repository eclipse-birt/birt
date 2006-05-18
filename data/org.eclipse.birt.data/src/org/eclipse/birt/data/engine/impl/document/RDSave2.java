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

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */
public class RDSave2 extends RDSave implements IRDSave
{

	/**
	 * @param context
	 * @param queryDefn
	 * @param queryResultID
	 * @param rowCount
	 * @param subQueryName
	 * @param subQueryIndex
	 * @throws DataException
	 */
	public RDSave2( DataEngineContext context, IBaseQueryDefinition queryDefn,
			String queryResultID, int rowCount, String subQueryName,
			int subQueryIndex ) throws DataException
	{
		super( context,
				queryDefn,
				queryResultID,
				rowCount,
				subQueryName,
				subQueryIndex );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.RDSave#saveExprValue(int,
	 *      java.lang.String, java.lang.Object)
	 */
	public void saveExprValue( int currIndex, String exprID, Object exprValue )
			throws DataException
	{
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.RDSave#saveFinish(int)
	 */
	public void saveFinish( int currIndex ) throws DataException
	{
		this.saveForIV( );
	}

}
