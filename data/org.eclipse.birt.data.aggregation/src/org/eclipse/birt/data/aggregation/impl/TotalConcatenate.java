/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.aggregation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Implements the built-in Total.concatenate aggregation
 */
public class TotalConcatenate extends AggrFunction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName( )
	{
		return IBuildInAggregation.TOTAL_CONCATENATE_FUNC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	public int getType( )
	{
		return SUMMARY_AGGR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	public int getDataType( )
	{
		return DataType.STRING_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getParameterDefn
	 * ()
	 */
	public IParameterDefn[] getParameterDefn( )
	{
		IParameterDefn paramDefn[] = new IParameterDefn[]{
				new ParameterDefn( Constants.EXPRESSION_NAME,
						Constants.EXPRESSION_DISPLAY_NAME,
						false,
						true,
						SupportedDataTypes.ANY,
						Messages.getString( "TotalConcatenate.paramDescription.expression" ) ),
				new ParameterDefn( Messages.getString( "TotalConcatenate.param.separator" ),
						Messages.getString( "TotalConcatenate.param.separator" ),
						false,
						false,
						SupportedDataTypes.CALCULATABLE,
						Messages.getString( "TotalConcatenate.paramDescription.separator" ) ),
				new ParameterDefn( Messages.getString( "TotalConcatenate.param.maxLength" ),
						Messages.getString( "TotalConcatenate.param.maxLength" ),
						true,
						false,
						SupportedDataTypes.CALCULATABLE,
						Messages.getString( "TotalConcatenate.paramDescription.maxLength" ) ),
				new ParameterDefn( Messages.getString( "TotalConcatenate.param.showAllValues" ),
						Messages.getString( "TotalConcatenate.param.showAllValues" ),
						true,
						false,
						SupportedDataTypes.CALCULATABLE,
						Messages.getString( "TotalConcatenate.paramDescription.showAllValues" ) )
		};
		return paramDefn;
	}

	public Accumulator newAccumulator( )
	{
		return new MyAccumulator( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription
	 * ()
	 */
	public String getDescription( )
	{
		return Messages.getString( "TotalConcatenate.description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName
	 * ()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "TotalConcatenate.displayName" ); //$NON-NLS-1$
	}

	private class MyAccumulator extends SummaryAccumulator
	{

		private Collection<String> values;

		private String separator;

		private int maxLength;

		final private static int DEFAULT_MAX_LENGTH = 1024;

		public void start( )
		{
			super.start( );
			values = null;
			separator = "";
			maxLength = DEFAULT_MAX_LENGTH;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang
		 * .Object[])
		 */
		public void onRow( Object[] args ) throws DataException
		{
			assert ( args.length >= 2 );
			if ( args[0] != null && args[1] != null )
			{
				try
				{
					if ( values == null )
					{
						setSeparator( args[1] );
						if ( args.length >= 3 )
						{
							setMaxLength( args[2] );
							if ( args.length == 4 )
							{
								setShowAllValues( args[3] );
							}
						}
					}
					values.add( DataTypeUtil.toString( args[0] ) );
				}
				catch ( BirtException e )
				{
					throw new DataException( e.getErrorCode( ), e );
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.data.engine.aggregation.SummaryAccumulator#
		 * getSummaryValue()
		 */
		public Object getSummaryValue( )
		{
			if ( values == null )
			{
				return null;
			}
			StringBuffer buffer = new StringBuffer( );
			Iterator<String> valueIterator = values.iterator( );
			while ( valueIterator.hasNext( ) )
			{
				String currentValue = valueIterator.next( );
				// reach the max length of the concatenated string
				if ( buffer.length( ) > this.maxLength - currentValue.length( ) )
				{
					break;
				}
				if ( currentValue != null && currentValue.trim( ).length( ) > 0 )
				{
					buffer.append( currentValue ).append( separator );
				}
			}
			// delete the last separator character
			if ( buffer.length( ) > 0 )
			{
				return buffer.toString( ).substring( 0,
						buffer.length( ) - separator.length( ) );
			}
			return buffer.toString( );
		}

		/**
		 * Set the separator of the concatenated string
		 * 
		 * @param source
		 * @throws BirtException
		 */
		private void setSeparator( Object source ) throws BirtException
		{
			String value = DataTypeUtil.toString( source );
			if ( value == null || value.length( ) == 0 )
			{
				throw new DataException( Messages.getString( "aggregation.InvalidSeparator" )
						+ getName( ) );
			}
			// should not trim the separator string
			separator = value;
		}

		/**
		 * Set the max length of the concatenated string by calculating the
		 * string character number
		 * 
		 * @param source
		 * @throws DataException
		 */
		private void setMaxLength( Object source ) throws DataException
		{
			try
			{
				if ( source == null
						|| DataTypeUtil.toString( source ).trim( ).length( ) == 0 )
				{
					maxLength = DEFAULT_MAX_LENGTH;
				}
				else
				{
					int value = DataTypeUtil.toInteger( source );
					if ( value < 1 )
					{
						throw new DataException( Messages.getString( "aggregation.InvalidParameterValue" )
								+ getName( ) );
					}
					maxLength = value;
				}
			}
			catch ( BirtException e )
			{
				throw new DataException( e.getErrorCode( ), e );
			}
		}

		/**
		 * Decide whether should show all values whatever some strings are the
		 * same
		 * 
		 * @param source
		 * @throws BirtException
		 */
		private void setShowAllValues( Object source ) throws BirtException
		{
			boolean showAllValues;
			if ( source == null
					|| DataTypeUtil.toString( source ).trim( ).length( ) == 0 )
			{
				showAllValues = false;
			}
			else if ( !( source instanceof Boolean ) )
			{
				throw new DataException( Messages.getString( "aggregation.InvalidParameterType" )
						+ getName( ) );
			}
			else
			{
				showAllValues = DataTypeUtil.toBoolean( source );
			}
			if ( showAllValues )// add each row's value to an ArrayList
			{
				values = new ArrayList<String>( );
			}
			else
			// add each row's value to a LinkedHashSet
			{
				values = new LinkedHashSet<String>( );
			}

		}
	}

}
