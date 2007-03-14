
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

/**
 * The interface used to access a set of data rows retrieved by a cube
 * aggregation.
 */

public interface IAggregationResultSet
{
	/**
	 * 
	 * @return
	 */
	public int getLevelCount( );
	
	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public int getLevelIndex( String levelName );
	
	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public int getLevelKeyColCount( int levelIndex );
	
	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public int getLevelAttributeColCount( int levelIndex );
	
	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public int getLevelKeyDataType( String levelName, String keyName );
	
	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public int getLevelKeyDataType( int levelIndex, String keyName );
	
	/**
	 * 
	 * @param levelIndex
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeIndex( int levelIndex, String attributeName );
	
	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeIndex( String levelName, String attributeName );
	
	/**
	 * 
	 * @param levelIndex
	 * @param attributeName
	 * @return
	 */
	public int getLevelKeyIndex( int levelIndex, String keyName );
	
	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelKeyIndex( String levelName, String keyName );
	
	
	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public String[] getAllAttributes( int levelIndex );
	
	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeDataType( String levelName, String attributeName );
	
	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeDataType( int levelIndex, String attributeName );
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public int getAggregationDataType( int aggregationIndex ) throws IOException;
	
	/**
	 * 
	 * @param index
	 * @throws IOException 
	 */
	public void seek( int index ) throws IOException;
	
	/**
	 * 
	 * @return
	 */
	public int length( );
	
	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public Object[] getLevelKeyValue( int levelIndex );
	
	/**
	 * 
	 * @param levelIndex
	 * @param attributeIndex
	 * @return
	 */
	public Object getLevelAttribute( int levelIndex, int attributeIndex );
	
	/**
	 * 
	 * @param aggregationIndex
	 * @return
	 * @throws IOException 
	 */
	public Object getAggregationValue( int aggregationIndex ) throws IOException;
	
	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public int getSortType( int levelIndex );
}
