/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;
import org.mozilla.javascript.Scriptable;

/**
 * A cube operation executor to execute all the cube operations one by one in a
 * <code>ICubeQueryDefinition</code>
 * 
 */
public class CubeOperationsExecutor
{

	private ICubeQueryDefinition cubeQueryDefn;

	private Scriptable scope;

	public CubeOperationsExecutor( ICubeQueryDefinition cubeQueryDefn,
			Scriptable scope ) throws DataException
	{
		this.cubeQueryDefn = cubeQueryDefn;
		this.scope = scope;
	}

	/**
	 * execute all the added cube operations
	 * 
	 * @param source:
	 *            the common execution result of cubeQueryDefn
	 * @param manager:
	 *            used to maintain the map of the returned
	 *            IAggregationResultSet[] and current CalculatedMember[]
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IAggregationResultSet[] execute( IAggregationResultSet[] source,
			MeasureNameManager manager, StopSign stopSign ) throws IOException,
			BirtException
	{
		IAggregationResultSet[] currentResult = source;
		for ( ICubeOperation co : cubeQueryDefn.getCubeOperations( ) )
		{
			IOperationExecutor oe = CubeOperationFactory.createOperationExecutor( co );

			currentResult = oe.execute( cubeQueryDefn,
					currentResult,
					manager,
					scope,
					stopSign );

		}
		return currentResult;
	}
}
