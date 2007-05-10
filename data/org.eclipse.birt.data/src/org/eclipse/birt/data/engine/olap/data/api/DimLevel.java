/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.api;

import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;

/**
 * This class is responsible for encapsulating level naming information in the
 * cube. All levels' names are not gurantee to be unique since Birt2.2RC0, which
 * means two levels in different dimensions maybe share the same level name.
 * Using DimLevel object will avoid these kind of conflict, since it use the
 * qualified name to identify a level. The qualified name of a level consistes
 * of the dimension name and level name and separated with splash.
 * 
 * @author Li Jianchao
 * 
 */
public class DimLevel implements Comparable
{

	private String dimensionName;
	private String levelName;

	private String qualifiedName;// = dimensionName+'/'+levelName

	/**
	 * @param dimensionName
	 * @param levelName
	 */
	public DimLevel( String dimensionName, String levelName )
	{
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		setQualifiedName( );
	}

	/**
	 * @param levelDefn
	 */
	public DimLevel( ILevelDefinition levelDefn )
	{
		this.levelName = levelDefn.getName( );
		this.dimensionName = levelDefn.getHierarchy( )
				.getDimension( )
				.getName( );
		setQualifiedName( );
	}

	private void setQualifiedName( )
	{
		StringBuffer buf = new StringBuffer( );
		buf.append( dimensionName );
		buf.append( '/' );
		buf.append( levelName );
		qualifiedName = buf.toString( );
	}

	/**
	 * @return the dimensionName
	 */
	public String getDimensionName( )
	{
		return dimensionName;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName( )
	{
		return levelName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ( ( dimensionName == null ) ? 0 : dimensionName.hashCode( ) );
		result = prime
				* result + ( ( levelName == null ) ? 0 : levelName.hashCode( ) );
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( !( obj instanceof DimLevel ) )
			return false;
		final DimLevel other = (DimLevel) obj;
		if ( dimensionName == null )
		{
			if ( other.dimensionName != null )
				return false;
		}
		else if ( !dimensionName.equals( other.dimensionName ) )
			return false;
		if ( levelName == null )
		{
			if ( other.levelName != null )
				return false;
		}
		else if ( !levelName.equals( other.levelName ) )
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString( )
	{
		return qualifiedName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo( Object obj )
	{
		if ( obj == null || !( obj instanceof DimLevel ) )
		{
			return -1;
		}
		DimLevel dimLevel = (DimLevel) obj;
		return this.toString( ).compareTo( dimLevel.toString( ) );
	}
}
