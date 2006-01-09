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
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */
public final class RDUtil
{
	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 * @return
	 * @throws DataException
	 */
	public static RDSave newSave( DataEngineContext context,
			String queryResultID, int rowCount, String subQueryName,
			int subQueryIndex )
			throws DataException
	{
		return new RDSave( context,
				queryResultID,
				rowCount,
				subQueryName,
				subQueryIndex );
	}

	/**
	 * @param context
	 * @param queryResultID
	 * @param subQueryName
	 * @param currParentIndex
	 * @return load util
	 * @throws DataException
	 */
	public static RDLoad newLoad( DataEngineContext context,
			String queryResultID, String subQueryName, int currParentIndex )
			throws DataException
	{
		return new RDLoad( context,
				queryResultID,
				subQueryName,
				currParentIndex );
	}

}
