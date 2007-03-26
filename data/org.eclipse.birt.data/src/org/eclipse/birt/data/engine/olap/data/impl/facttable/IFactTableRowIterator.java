
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

/**
 * 
 */

public interface IFactTableRowIterator
{
	/**
	 * 
	 * @return
	 */
	public int getDimensionCount( );
	
	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public int getDimensionIndex( String dimensionName );
	
	/**
	 * 
	 * @param dimensionIndex
	 * @return
	 */
	public int getDimensionPosition( int dimensionIndex );
	
	/**
	 * 
	 * @param measureIndex
	 * @return
	 */
	public Object getMeasure( int measureIndex );
	
	/**
	 * 
	 * @return
	 */
	public int getMeasureCount( );
	
	/**
	 * 
	 * @param measureName
	 * @return
	 */
	public int getMeasureIndex( String measureName );
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean next( ) throws IOException;
	
}
