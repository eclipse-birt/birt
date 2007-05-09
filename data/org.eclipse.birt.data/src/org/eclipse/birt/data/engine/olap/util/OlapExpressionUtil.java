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

package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * 
 */

public class OlapExpressionUtil
{
	/**
	 * This method is used to get the level name that reference by a level
	 * reference expression of following format:
	 * dimension["dimensionName"]["levelName"].
	 * 
	 * String[0] dimensionName;
	 * String[1] levelName;
	 * @param expr
	 * @return String[]
	 */
	public static String[] getTargetLevel( String expr )
	{
		// TODO enhance me.
		if ( expr == null )
			return null;
		if ( !expr.matches( "\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E" ) )
			return null;

		expr = expr.replaceFirst( "\\Qdimension\\E", "" );
		String[] result = expr.split( "\\Q\"][\"\\E" );
		result[0] = result[0].replaceAll( "\\Q[\"\\E", "" );
		result[1] = result[1].replaceAll( "\\Q\"]\\E", "" );
		return result;
	}

	/**
	 * This method is to get the measure name that referenced by a measure
	 * reference expression.
	 * 
	 * @param expr
	 * @return
	 */
	public static String getMeasure( String expr ) throws DataException
	{
		if ( expr == null || !expr.matches( "\\Qmeasure[\"\\E.*\\Q\"]\\E" ) )
			throw new DataException( ResourceConstants.INVALID_MEASURE_REF,
					expr );

		return expr.replaceFirst( "\\Qmeasure[\"\\E", "" )
				.replaceFirst( "\\Q\"]\\E", "" );

	}
	
	/**
	 * Return the binding name of data["binding"]
	 * @param expr
	 * @return
	 */
	public static String getBindingName( String expr )
	{
		if ( expr == null )
			return null;
		if ( !expr.matches( "\\Qdata[\"\\E.*\\Q\"]\\E" ) )
			return null;
		return expr.replaceFirst( "\\Qdata[\"\\E", "" )
				.replaceFirst( "\\Q\"]\\E", "" );
	
	}

	/**
	 * 
	 * @param level
	 * @param attribute
	 * @return
	 */
	public static String getAttributeColumnName( String level, String attribute )
	{
		return level + "/" + attribute;
	}
	
	/**
	 * This method returns a list of ICubeAggrDefn instances which describes the
	 * aggregations that need to be calcualted in cube query.
	 * 
	 * @param bindings
	 * @return
	 * @throws DataException 
	 */
	public static ICubeAggrDefn[] getAggrDefns( List bindings ) throws DataException
	{
		if ( bindings == null || bindings.size( ) == 0 )
			return new ICubeAggrDefn[0];

		List cubeAggrDefns = new ArrayList( );
		for ( Iterator it = bindings.iterator( ); it.hasNext( ); )
		{
			IBinding binding = ( (IBinding) it.next( ) );
			if ( binding.getExpression( ) instanceof IScriptExpression )
			{
				//TODO fix me. together with CursorModelTest and CursorNavigatorTest.
				//String measure = getMeasure( ( (IScriptExpression) binding.getExpression( ) ).getText( ) );
				if ( binding.getAggrFunction( ) != null  )
					cubeAggrDefns.add( new CubeAggrDefn( binding.getBindingName( ),
							getMeasure( ( (IScriptExpression) binding.getExpression( ) ).getText( ) ),
							getAggrOnLevels ( binding.getAggregatOns( )),
							binding.getAggrFunction( ) ) );
			}
		}

		ICubeAggrDefn[] result = new ICubeAggrDefn[cubeAggrDefns.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = (ICubeAggrDefn) cubeAggrDefns.get( i );
		}

		return result;
	}

	/**
	 * 
	 * @param exprs
	 * @return
	 */
	public static List getAggrOnLevels( List exprs )
	{
		List levelNames = new ArrayList( );
		for( int i = 0; i < exprs.size( ); i++ )
		{
			String[] array = getTargetLevel(exprs.get( i ).toString( )); 
			if ( array!= null )
				levelNames.add( array[1] );
		}
		return levelNames;
	}
	
	private static class CubeAggrDefn implements ICubeAggrDefn
	{

		//
		private String name;
		private String measure;
		private List aggrLevels;
		private String aggrName;

		/*
		 * 
		 */
		CubeAggrDefn( String name, String measure, List aggrLevels,
				String aggrName )
		{
			assert name != null;
			assert measure != null;
			assert aggrLevels != null;

			this.name = name;
			this.measure = measure;
			this.aggrLevels = aggrLevels;
			this.aggrName = aggrName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn#getAggrLevels()
		 */
		public List getAggrLevels( )
		{
			return this.aggrLevels;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn#getMeasure()
		 */
		public String getMeasure( )
		{
			return this.measure;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn#getName()
		 */
		public String getName( )
		{
			return this.name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn#aggrName()
		 */
		public String getAggrName( )
		{
			return this.aggrName;
		}

	}
}
