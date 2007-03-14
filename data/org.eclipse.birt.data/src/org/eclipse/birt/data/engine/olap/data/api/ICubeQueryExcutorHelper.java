
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
package org.eclipse.birt.data.engine.olap.data.api;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;

/**
 * 
 */

public interface ICubeQueryExcutorHelper
{
	/**
	 * 
	 * @param levelName
	 * @param selections
	 */
	public void addFilter( String levelName, ISelection[] selections );
	
	/**
	 * 
	 * @param stopSign
	 * @return
	 * @throws IOException 
	 * @throws BirtOlapException 
	 * @throws BirtException 
	 */
	public IAggregationResultSet[] excute( AggregationDefinition[] aggregation,
			StopSign stopSign ) throws DataException, IOException, BirtException;
	
	/**
	 * 
	 *
	 */
	public void clear( );
	
	/**
	 * 
	 *
	 */
	public void close( );
}
