/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.rd.RDSave;
import org.eclipse.birt.data.engine.impl.rd.RDUtil;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * An iterator on a result set from a prepared and executed report query.
 * Multiple ResultIterator objects could be associated with the same
 * QueryResults object.
 */
public class ResultIterator implements IResultIterator
{
	protected org.eclipse.birt.data.engine.odi.IResultIterator odiResult;
	protected IQueryResults queryResults;
	protected Scriptable scope;
	
	protected static final int NOT_STARTED = 0;
	protected static final int BEFORE_FIRST_ROW = 1;
	protected static final int ON_ROW = 2;
	protected static final int AFTER_LAST_ROW = 3;
	protected static final int CLOSED = -1;
	
	protected int state = NOT_STARTED;
	
	protected PreparedQuery query;

	// used in (usesDetails == false)
	protected boolean useDetails;
	protected int lowestGroupLevel;
	private int savedStartingGroupLevel;

	// context of data engine
	private DataEngineContext context;
	private RDSaveUtil rdSaveUtil;
	
	protected static Logger logger = Logger.getLogger( ResultIterator.class.getName( ) );

	/**
	 * Constructor for report query (which produces a QueryResults)
	 */
	ResultIterator( DataEngineContext context, IQueryResults queryResults,
			PreparedQuery query,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
		assert queryResults != null
				&& query != null && odiResult != null && scope != null;
		this.queryResults = queryResults;
		this.query = query;
		this.odiResult = odiResult;
		this.scope = scope;

		this.context = context;

		IBaseQueryDefinition queryDefn = query.getQueryDefn( );
		assert queryDefn != null;
		this.useDetails = queryDefn.usesDetails( );
		this.lowestGroupLevel = queryDefn.getGroups( ).size( );
		start( );
		logger.logp( Level.FINER,
				ResultIterator.class.getName( ),
				"ResultIterator",
				"ResultIterator starts up" );
	}

	/**
	 * Construction
	 */
	protected ResultIterator( )
	{

	}

	/**
	 * Internal method to start the iterator; must be called before any other
	 * method can be used
	 */
	protected void start( ) throws DataException
	{
		assert state == NOT_STARTED;

		// Note that the odiResultIterator currently has its cursor located AT
		// its first row. This iterator starts out with cursor BEFORE first row.
		state = BEFORE_FIRST_ROW;
	}

	/**
	 * Returns the Rhinoe scope associated with this iterator
	 */
	public Scriptable getScope( )
	{
		return scope;
	}

	/**
	 * Checks to make sure the iterator has started. Throws exception if it has
	 * not.
	 */
	private void checkStarted( ) throws DataException
	{
		if ( state == NOT_STARTED || state == CLOSED )
		{
			DataException e = new DataException( ResourceConstants.RESULT_CLOSED );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"checkStarted",
					"ResultIterator has been closed.",
					e );
			throw e;
		}
	}

	/**
	 * Returns the QueryResults of this result iterator. A convenience method
	 * for the API consumer.
	 * 
	 * @return The QueryResults that contains this result iterator.
	 */
	public IQueryResults getQueryResults( )
	{
		return queryResults;
	}

	/**
	 * Moves down one element from its current position of the iterator. This
	 * method applies to a result whose ReportQuery is defined to use detail or
	 * group rows.
	 * 
	 * @return true if next element exists and has not reached the limit on the
	 *         maximum number of rows that can be accessed.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public boolean next( ) throws BirtException
	{
		checkStarted( );

		boolean hasNext = false;
		
		if ( state == BEFORE_FIRST_ROW )
		{
			state = ON_ROW;
			hasNext = odiResult.getCurrentResult( ) != null;
		}
		else
		{
			hasNext = odiResult.next( );
		}
		
		if ( useDetails == false && hasNext )
		{
			savedStartingGroupLevel = odiResult.getStartingGroupLevel( );
			odiResult.last( lowestGroupLevel );
		}

		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"next",
				"Moves down to the next element" );
		
		if ( ! hasNext )
			state = AFTER_LAST_ROW;
		
		return hasNext;
	}

	static Object evaluateCompiledExpression( CompiledExpression expr,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
		// Special case for DirectColRefExpr: it's faster to directly access
		// column value using the Odi IResultIterator.
		if ( expr instanceof ColumnReferenceExpression )
		{
			// Direct column reference
			ColumnReferenceExpression colref = (ColumnReferenceExpression) expr;
			if ( colref.isIndexed( ) )
			{
				int idx = colref.getColumnindex( );
				// Special case: row[0] refers to internal rowID
				if ( idx == 0 )
					return new Integer( odiResult.getCurrentResultIndex( ) );
				else if ( odiResult.getCurrentResult( ) != null )
					return odiResult.getCurrentResult( )
							.getFieldValue( idx );
				else
					return null;
			}
			else
			{
				String name = colref.getColumnName( );
				// Special case: row._rowPosition refers to internal rowID
				if ( JSRowObject.ROW_POSITION.equals( name ) )
					return new Integer( odiResult.getCurrentResultIndex( ) );
				else if ( odiResult.getCurrentResult( ) != null )
					return odiResult.getCurrentResult( )
							.getFieldValue( name );
				else
					return null;
			}
		}
		else
		{
			Context cx = Context.enter();
			try
			{
				return  expr.evaluate( cx, scope );
			}
			finally
			{
				Context.exit();
			}
		}

	}

	/**
	 * Returns the value of a query result expression. A given data expression
	 * could be for one of the Selected Columns (if detail rows are used), or of
	 * an Aggregation specified in the prepared ReportQueryDefn spec. When
	 * requesting for the value of a Selected Column, its value in the current
	 * row of the iterator will be returned.
	 * <p>
	 * Throws an exception if a result expression value is requested out of
	 * sequence from the prepared ReportQueryDefn spec. E.g. A group aggregation
	 * is defined to be after_last_row. It would be out of sequence if requested
	 * before having iterated/skipped to the last row of the group. In future
	 * release, this could have intelligence to auto recover and performs
	 * dependent operations to properly evaluate any out-of-sequence result
	 * values.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression. It could be null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public Object getValue( IBaseExpression dataExpr ) throws BirtException
	{
		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"getValue",
				"get of value IBaseExpression: " + LogUtil.toString( dataExpr ) );
		checkStarted( );

		Object exprValue = null;
		
		Object handle = dataExpr.getHandle( );
		if ( handle instanceof CompiledExpression )
		{
			CompiledExpression expr = (CompiledExpression) handle;
			Object value = evaluateCompiledExpression( expr, odiResult, scope );
			
			try
			{
				exprValue = DataTypeUtil.convert( value, dataExpr.getDataType( ) );
			}
			catch ( BirtException e )
			{
				throw new DataException( ResourceConstants.INCONVERTIBLE_DATATYPE,
						new Object[]{
								value,
								value.getClass( ),
								DataType.getClass( dataExpr.getDataType( ) )
						} );
			}
		}
		else if ( handle instanceof ConditionalExpression )
		{
			ConditionalExpression ce = (ConditionalExpression) handle;
			Object resultExpr = getValue( ce.getExpression( ) );
			Object resultOp1 = ce.getOperand1( ) != null
					? getValue( ce.getOperand1( ) ) : null;
			Object resultOp2 = ce.getOperand2( ) != null
					? getValue( ce.getOperand2( ) ) : null;
			String op1Text = ce.getOperand1( ) != null ? ce.getOperand1( )
					.getText( ) : null;
			String op2Text = ce.getOperand2( ) != null ? ce.getOperand2( )
					.getText( ) : null;
			exprValue = ScriptEvalUtil.evalConditionalExpr( resultExpr,
					ce.getOperator( ),
					ScriptEvalUtil.newExprInfo( op1Text, resultOp1 ),
					ScriptEvalUtil.newExprInfo( op2Text, resultOp2 ) );
		}
		else
		{
			DataException e = new DataException( ResourceConstants.INVALID_EXPR_HANDLE );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getValue",
					"Invalid expression handle.",
					e );
			throw e;
		}
		
		this.getRdSaveUtil( ).doSaveExpr( dataExpr, exprValue );
		
		return exprValue;
	}
	
	/**
	 * @return
	 */
	private RDSaveUtil getRdSaveUtil( )
	{
		if ( this.rdSaveUtil == null )
		{
			rdSaveUtil = new RDSaveUtil( this.context,
					this.queryResults.getID( ),
					this.odiResult );
		}
		
		return this.rdSaveUtil;
	}
	
	/**
	 * Returns the value of a query result expression as a Boolean, by type
	 * casting the Object returned by getValue. <br>
	 * A convenience method for the API consumer. <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression as a Boolean. It could be null.
	 * @throws DataException
	 *             if error occurs in Data Engine
	 */
	public Boolean getBoolean( IBaseExpression dataExpr ) throws DataException
	{
		try
		{
			return DataTypeUtil.toBoolean( getValue( dataExpr ) );
		}
		catch ( BirtException e )
		{
			DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR,
					e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getBoolean",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}

	/**
	 * Returns the value of a query result expression as an Integer, by type
	 * casting the Object returned by getValue. <br>
	 * A convenience method for the API consumer. <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression as an Integer. It could be
	 *         null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public Integer getInteger( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toInteger( getValue( dataExpr ) );
	}

	/**
	 * Returns the value of a query result expression as a Double, by type
	 * casting the Object returned by getValue. <br>
	 * A convenience method for the API consumer. <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression as a Double. It could be null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public Double getDouble( IBaseExpression dataExpr ) throws BirtException
	{
			return DataTypeUtil.toDouble( getValue( dataExpr ) );
	}

	/**
	 * Returns the value of a query result expression as a String, by type
	 * casting the Object returned by getValue. <br>
	 * A convenience method for the API consumer. <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression as a String. It could be null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public String getString( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toString( getValue( dataExpr ) );
	}

	/**
	 * Returns the value of a query result expression as a BigDecimal, by type
	 * casting the Object returned by getValue. <br>
	 * A convenience method for the API consumer. <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression as a BigDecimal. It could be
	 *         null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public BigDecimal getBigDecimal( IBaseExpression dataExpr )
			throws BirtException
	{
		return DataTypeUtil.toBigDecimal( getValue( dataExpr ) );
	}

	/**
	 * Returns the value of a query result expression as a Date, by type casting
	 * the Object returned by getValue. <br>
	 * A convenience method for the API consumer. <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given expression as a Date. It could be null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public Date getDate( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toDate( getValue( dataExpr ) );
	}

	/**
	 * Returns the value of a query result expression representing Blob data.
	 * <br>
	 * If the expression value has an incompatible type, a ClassCastException is
	 * thrown at runtime.
	 * 
	 * @param dataExpr
	 *            An IBaseExpression object provided in the ReportQueryDefn at
	 *            the time of prepare.
	 * @return The value of the given Blob expression. It could be null.
	 * @throws BirtException
	 *             if error occurs in Data Engine
	 */
	public Blob getBlob( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toBlob( getValue( dataExpr ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBytes(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public byte[] getBytes( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toBytes( getValue( dataExpr ) );
	}

	/**
	 * Advances the iterator, skipping rows to the last row in the current group
	 * at the specified group level. This is for result sets that do not use
	 * detail rows to advance to next group. Calling next() after skip() would
	 * position the current row to the first row of the next group.
	 * 
	 * @param groupLevel
	 *            An absolute value for group level. A value of 0 applies to the
	 *            whole result set.
	 * @throws DataException
	 *             if error occurs in Data Engine
	 */
	public void skipToEnd( int groupLevel ) throws DataException
	{
		checkStarted( );
		odiResult.last( groupLevel );
		logger.logp( Level.FINER,
				ResultIterator.class.getName( ),
				"skipToEnd",
				"skipping rows to the last row in the current group" );
	}

	/**
	 * Returns the 1-based index of the outermost group in which the current row
	 * is the first row. For example, if a query contain N groups (group with
	 * index 1 being the outermost group, and group with index N being the
	 * innermost group), and this function returns a value M, it indicates that
	 * the current row is the first row in groups with indexes (M, M+1, ..., N ).
	 * 
	 * @return 1-based index of the outermost group in which the current row is
	 *         the first row; (N+1) if the current row is not at the start of
	 *         any group; 0 if the result set has no groups.
	 */
	public int getStartingGroupLevel( ) throws DataException
	{
		if ( useDetails == false )
		{
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getStartingGroupLevel",
					"return the starting group level" );
			return savedStartingGroupLevel;
		}

		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"getStartingGroupLevel",
				"return the starting group level" );
		return odiResult.getStartingGroupLevel( );
	}

	/**
	 * Returns the 1-based index of the outermost group in which the current row
	 * is the last row. For example, if a query contain N groups (group with
	 * index 1 being the outermost group, and group with index N being the
	 * innermost group), and this function returns a value M, it indicates that
	 * the current row is the last row in groups with indexes (M, M+1, ..., N ).
	 * 
	 * @return 1-based index of the outermost group in which the current row is
	 *         the last row; (N+1) if the current row is not at the end of any
	 *         group; 0 if the result set has no groups.
	 */
	public int getEndingGroupLevel( ) throws DataException
	{
		logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getEndingGroupLevel",
					"return the ending group level" );
		return odiResult.getEndingGroupLevel( );
	}
	
	/**
	 * Returns the secondary result specified by a SubQuery that was defined in
	 * the prepared ReportQueryDefn.
	 * 
	 * @throws DataException
	 *             if error occurs in Data Engine
	 */
	public IResultIterator getSecondaryIterator( String subQueryName,
			Scriptable subScope ) throws DataException
	{
		checkStarted( );
		
		QueryResults results = query.execSubquery( odiResult,
				subQueryName,
				subScope );
		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"getSecondaryIterator",
				"Returns the secondary result specified by a SubQuery" );

		IResultIterator resultIt = results.getResultIterator( );
		
		this.getRdSaveUtil( ).processForSubQuery( this.getQueryResults( )
				.getID( ),
				(ResultIterator) resultIt,
				subQueryName );
		
		return resultIt;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws DataException
	{
		try
		{
			return new ResultMetaData( odiResult.getResultClass( ) );
		}
		finally
		{
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getResultMetaData",
					"Returns the result metadata" );
		}
	}

	/**
	 * Closes this result and any associated secondary result iterator(s),
	 * providing a hint that the consumer is done with this result, whose
	 * resources can be safely released as appropriate.
	 * 
	 * @throws DataException
	 */
	public void close( ) throws BirtException
	{
		// save results when neededs
		this.getRdSaveUtil( ).doSaveFinish( );

		if ( odiResult != null )
				odiResult.close( );

		odiResult = null;
		queryResults = null;
		state = CLOSED;
		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"close",
				"a ResultIterator is closed" );
	}
	
	/**
	 * Specifies the maximum number of rows that can be accessed from this
	 * result iterator. Default is no limit.
	 * 
	 * @param maxLimit
	 *            The new max row limit. This value must not be greater than the
	 *            max limit specified in the ReportQueryDefn. A value of 0 means
	 *            no limit.
	 * @throws DataException
	 *             if error occurs in Data Engine
	 */
	void setMaxRows( int maxLimit ) throws DataException
	{
	}

	/**
	 * Retrieves the maximum number of rows that can be accessed from this
	 * result iterator.
	 * 
	 * @return the current max row limit; a value of 0 means no limit.
	 */
	int getMaxRows( )
	{
		return 0;
	}

	/**
	 * Only for CachedResultSet test case
	 * 
	 * @return
	 */
	org.eclipse.birt.data.engine.odi.IResultIterator getOdiResult( )
	{
		return odiResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[])
	 */
	public boolean findGroup( Object[] groupKeyValues ) throws BirtException
	{
		List groups = query.getQueryDefn( ).getGroups( );
		if ( groupKeyValues.length > groups.size( ) )
			throw new DataException( ResourceConstants.INCORRECT_GROUP_KEY_VALUES );

		GroupDefinition group = null;

		String[] columnNames = new String[groupKeyValues.length];

		for ( int i = 0; i < columnNames.length; i++ )
		{
			group = (GroupDefinition) groups.get( i );

			columnNames[i] = getGroupKeyColumnName( group );
		}

		// Return to first row.
		odiResult.first( 0 );
		if( odiResult.getCurrentResult() == null )
			return false;
		do
		{
			for ( int i = 0; i < columnNames.length; i++ )
			{
				if ( groupKeyValuesEqual( groupKeyValues, columnNames, i ) )
				{
					if ( i == columnNames.length - 1 )
						return true;
				}
				else
				{
					// because group level is 1-based. We should use "i+1" to
					// indicate current group.
					this.skipToEnd( i + 1 );
					break;
				}
			}
		} while ( odiResult.next( ) );

		return false;
	}

	/**
	 * @param groupKeyValues
	 * @param columnNames
	 * @param i
	 * @return
	 * @throws BirtException
	 */
	private boolean groupKeyValuesEqual( Object[] groupKeyValues,
			String[] columnNames, int i ) throws BirtException
	{
		Object fieldValue = odiResult.getCurrentResult( )
				.getFieldValue( columnNames[i] );

		boolean retValue = false;
		if ( fieldValue == groupKeyValues[i] )
		{
			retValue = true;
		}
		else if ( fieldValue != null && groupKeyValues[i] != null )
		{
			if ( fieldValue.getClass( ).equals( groupKeyValues[i].getClass( ) ) )
			{
				retValue = fieldValue.equals( groupKeyValues[i] );
			}
			else
			{
				Object convertedOb = DataTypeUtil.convert( groupKeyValues[i],
						fieldValue.getClass( ) );
				retValue = fieldValue.equals( convertedOb );
			}
		}

		return retValue;
	}

	/**
	 * The method which extracts column name from group definition.
	 * 
	 * @param group
	 * @return
	 */
	private String getGroupKeyColumnName( GroupDefinition group )
	{
		String columnName;
		if ( group.getKeyColumn( ) != null )
		{
			columnName = group.getKeyColumn( );
		}
		else
		{
			columnName = group.getKeyExpression( );
			if ( columnName.toUpperCase( ).startsWith( "ROW." ) )
				columnName = columnName.toUpperCase( )
						.replaceFirst( "\\QROW.\\E", "" );
			else if ( columnName.toUpperCase( ).startsWith( "ROW[" ) )
			{
				columnName = columnName.toUpperCase( )
						.replaceFirst( "\\QROW\\E\\s*\\Q[\\E", "" );
				columnName = columnName.trim( ).substring( 0,
						columnName.length( ) - 1 ).trim( );
			}
			if ( columnName != null
					&& columnName.matches( "\\Q\"\\E.*\\Q\"\\E" ) )
				columnName = columnName.substring( 1, columnName.length( ) - 1 );
		}
		return columnName;
	}

	/**
	 * Help resultiterator to save data
	 */
	private class RDSaveUtil
	{
		// context info
		private DataEngineContext context;
		private String queryResultID;

		// report document save and load instance
		private RDSave rdSave;

		// the name of sub query
		private String subQueryName;
		// the index of sub query in its corresponding group level
		private int subQueryIndex;
		// the group level
		private int groupLevel;
		// the group information of its sub query
		private int[] subQueryInfo;

		// odi result
		private org.eclipse.birt.data.engine.odi.IResultIterator odiResult;

		/**
		 * @param context
		 * @param queryResultID
		 * @param odiResult
		 */
		public RDSaveUtil( DataEngineContext context, String queryResultID,
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult )
		{
			this( context, queryResultID, odiResult, null, -1, -1, null );
		}

		/**
		 * @param context
		 * @param queryResultID
		 * @param odiResult
		 * @param subQueryName
		 * @param subQueryIndex
		 * @param subQueryInfo
		 */
		RDSaveUtil( DataEngineContext context, String queryResultID,
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
				String subQueryName, int groupLevel, int subQueryIndex, int[] subQueryInfo )
		{
			this.context = context;
			this.queryResultID = queryResultID;
			this.odiResult = odiResult;
			this.subQueryName = subQueryName;
			this.groupLevel = groupLevel;
			this.subQueryIndex = subQueryIndex;
			this.subQueryInfo = subQueryInfo;
		}

		/**
		 * @return
		 */
		String getSubQueryID( )
		{
			if ( this.subQueryName == null )
				return null;

			return this.subQueryName + "/" + this.subQueryIndex;
		}

		/**
		 * @param dataExpr
		 * @param value
		 * @throws DataException
		 */
		void doSaveExpr( IBaseExpression dataExpr, Object value )
				throws DataException
		{
			if ( needsSaveToDoc( ) == false )
				return;

			this.getRdSave( )
					.saveExprValue( odiResult.getCurrentResultIndex( ),
							dataExpr.getID( ),
							value );
		}

		/**
		 * @throws DataException
		 */
		void doSaveFinish( ) throws DataException
		{
			if ( needsSaveToDoc( ) == false )
				return;

			this.getRdSave( )
					.saveResultIterator( (CachedResultSet) this.odiResult,
							this.groupLevel,
							this.subQueryInfo );

			this.getRdSave( ).saveFinish( );
		}
		
		/**
		 * @param resultIt
		 * @param subQueryName
		 * @throws DataException
		 */
		private void processForSubQuery( String parentQueryID,
				ResultIterator resultIt, String subQueryName )
				throws DataException
		{			
			if ( needsSaveToDoc( ) == false )
				return;
			
			QueryResults results = (QueryResults) resultIt.getQueryResults( );

			// set query result id
			if ( getSubQueryID( ) == null )
				results.setID( parentQueryID );
			else
				results.setID( parentQueryID + "/" + this.getSubQueryID( ) );

			// init RDSave util of sub query
			resultIt.rdSaveUtil = new RDSaveUtil( resultIt.context,
					resultIt.getQueryResults( ).getID( ),
					resultIt.odiResult,
					subQueryName,
					results.getGroupLevel( ),
					odiResult.getCurrentGroupIndex( results.getGroupLevel( ) ),
					odiResult.getGroupStartAndEndIndex( results.getGroupLevel( ) ) );
		}
		
		/**
		 * @return
		 */
		private boolean needsSaveToDoc( )
		{
			if ( state == NOT_STARTED || state == CLOSED
					|| context == null
					|| context.getMode( ) != DataEngineContext.MODE_GENERATION )
				return false;

			return true;
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		private RDSave getRdSave( ) throws DataException
		{
			if ( rdSave == null )
			{
				rdSave = RDUtil.newSave( this.context,
						this.queryResultID,
						( (CachedResultSet) odiResult ).getRowCount( ),
						this.subQueryName,
						this.subQueryIndex );
			}

			return rdSave;
		}
	}
	
}
