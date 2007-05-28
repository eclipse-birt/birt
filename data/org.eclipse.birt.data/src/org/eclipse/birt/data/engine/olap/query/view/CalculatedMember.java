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

package org.eclipse.birt.data.engine.olap.query.view;

import java.util.List;

import org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn;

/**
 * A CalculatedMember is an Aggregation Object which need to be calculated in
 * olap.data
 * 
 */
public class CalculatedMember
{

	private String aggrFunction, onMeasureName, name;
	private List aggrOnList, arguments;
	private int rsID;

	/**
	 * 
	 * @param onMeasureName
	 * @param aggrOnList
	 * @param aggrFunction
	 * @param isMeasure
	 */
	CalculatedMember( String name, String onMeasureName, List aggrOnList,
			String aggrFunction, int rsID )
	{
		this.name = name;
		this.onMeasureName = onMeasureName;
		this.aggrOnList = aggrOnList;
		this.aggrFunction = aggrFunction;
		this.rsID = rsID;
	}

	/**
	 * 
	 * @param aggrDefn
	 * @param rsID
	 */
	CalculatedMember( ICubeAggrDefn aggrDefn, int rsID )
	{
		this.name = aggrDefn.getName( );
		this.onMeasureName = aggrDefn.getMeasure( );
		this.aggrOnList = aggrDefn.getAggrLevels( );
		this.aggrFunction = aggrDefn.getAggrName( );
		this.arguments = aggrDefn.getArguments( );
		this.rsID = rsID;
	}
	
	/**
	 * 
	 * @return
	 */
	String getAggrFunction( )
	{
		return this.aggrFunction;
	}

	/**
	 * 
	 * @return
	 */
	String getMeasureName( )
	{
		return onMeasureName;
	}

	/**
	 * 
	 * @return
	 */
	public List getAggrOnList( )
	{
		return this.aggrOnList;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getFirstArgumentInfo( )
	{
		if ( this.arguments == null || this.arguments.isEmpty( ) )
		{
			return new String[0];
		}
		else
			return (String[]) this.arguments.get( 0 );
	}
	
	/**
	 * 
	 * @return
	 */
	String getName( )
	{
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	int getRsID( )
	{
		return this.rsID;
	}
}
