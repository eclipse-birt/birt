/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition}
 * interface.
 * 
 */
public class SubCubeQueryDefinition implements ISubCubeQueryDefinition
{
	private String name, startingLevelOnColumn, startingLevelOnRow;

	/**
	 * 
	 * @param name
	 * @param startingLevelOnColumn
	 * @param startingLevelOnRow
	 */
	public SubCubeQueryDefinition( String name, String startingLevelOnColumn,
			String startingLevelOnRow )
	{
		this.name = name;
		this.startingLevelOnColumn = startingLevelOnColumn;
		this.startingLevelOnRow = startingLevelOnRow;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#getStartingLevelOnColumn()
	 */
	public String getStartingLevelOnColumn( )
	{
		return startingLevelOnColumn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#getStartingLevelOnRow()
	 */
	public String getStartingLevelOnRow( )
	{
		return startingLevelOnRow;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.INamedObject#getName()
	 */
	public String getName( )
	{
		return name;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.INamedObject#setName(java.lang.String)
	 */
	public void setName( String name )
	{
		this.name = name;
	}

}
