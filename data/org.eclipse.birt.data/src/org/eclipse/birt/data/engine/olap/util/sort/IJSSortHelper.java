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

package org.eclipse.birt.data.engine.olap.util.sort;

import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;

/**
 * 
 */

public interface IJSSortHelper
{
	/**
	 * 
	 * @return the target level
	 */
	public DimLevel getTargetLevel( );
	
	/**
	 * 
	 * @param row
	 * @return
	 * @throws DataException
	 */
	public Object evaluate( IResultRow row ) throws DataException;
	
	/**
	 * 
	 * @return
	 */
	public ISortDefinition getSortDefinition( );
	
	/**
	 * close this helper to clean up the registered javascript objects.
	 */
	public void close( );

}
