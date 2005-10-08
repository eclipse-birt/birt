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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * An iterator on a result set from a prepared and executed report query.
 * Multiple ResultIterator objects could be associated with
 * the same QueryResults object.
 */
class ResultIterator implements IResultIterator
{
	protected org.eclipse.birt.data.engine.odi.IResultIterator 
									odiResult;
	protected QueryResults			queryResults;
	protected Scriptable			scope;
	protected boolean				started = false;
	protected boolean				beforeFirstRow;
	protected PreparedQuery			query;
	
	// used in (usesDetails == false)
	final private boolean 			useDetails;
	final private int 				lowestGroupLevel;
	private int 					savedStartingGroupLevel;

	protected static Logger logger = Logger.getLogger( ResultIterator.class.getName( ) );
	
	/**
	 * Constructor for report query (which produces a QueryResults)
	 */
	ResultIterator( QueryResults queryResults,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
	    assert queryResults!= null && odiResult != null && scope != null;
	    this.queryResults = queryResults;
		this.odiResult = odiResult;		
		this.scope = scope;
				
		this.query = queryResults.getQuery();
		assert query != null;
		
		IBaseQueryDefinition queryDefn = query.getQueryDefn( );
		assert queryDefn != null;
		this.useDetails = queryDefn.usesDetails( );
		this.lowestGroupLevel = queryDefn.getGroups( ).size( );
		start();
	    logger.logp( Level.FINER,
	    		ResultIterator.class.getName( ),
				"ResultIterator",
				"ResultIterator starts up" );
	}
	
	
	/**
	 * Internal method to start the iterator; must be called before any other method can be used
	 */
	private void start() throws DataException
	{
	    started = true;
	    
	    // Note that the odiResultIterator currently has its cursor located AT 
	    // its first row. This iterator starts out with cursor BEFORE first row.
	    beforeFirstRow = true;
	}
	
	/**
	 * Returns the Rhinoe scope associated with this iterator
	 */
	public Scriptable getScope()
	{
	    return scope;
	}
	
	/**
	 * Checks to make sure the iterator has started. Throws exception if it has not.
	 */
	private void checkStarted() throws DataException
	{
	    if ( ! started )
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
	 * Returns the QueryResults of this result iterator.
	 * A convenience method for the API consumer.
	 * @return	The QueryResults that contains this result iterator.
	 */
	public IQueryResults getQueryResults()
	{ 
		return queryResults;
	}
	
	/**
	 * Moves down one element from its current position of the iterator.
	 * This method applies to a result whose ReportQuery is defined to 
	 * use detail or group rows. 
	 * @return 	true if next element exists and 
	 * 			has not reached the limit on the maximum number of rows 
	 * 			that can be accessed. 
	 * @throws 	DataException if error occurs in Data Engine
	 */
	public boolean next()
		throws DataException
	{ 
	    checkStarted();
	    
	    boolean hasNext = false;
	    try
		{
	    	if ( beforeFirstRow )
	    	{
	    		beforeFirstRow = false;
	    		hasNext = odiResult.getCurrentResult() != null;
	    	}
	    	else
	    	{
	    		hasNext = odiResult.next();
	    	}
	    	if ( useDetails == false && hasNext )
			{
	    		savedStartingGroupLevel = odiResult.getStartingGroupLevel( );
				odiResult.last( lowestGroupLevel );
			}
		}
	    catch ( DataException e )
		{
	    	throw e;
		}
	    
	    logger.logp( Level.FINE,
	    		ResultIterator.class.getName( ),
				"next",
				"Moves down to the next element" );
	    return hasNext;
	}

	static Object evaluateCompiledExpression( CompiledExpression expr, 
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Context cx, Scriptable scope ) throws DataException
	{
	    try
		{
	    	// Special case for DirectColRefExpr: it's faster to directly access
	    	// column value using the Odi IResultIterator.
		    if ( expr instanceof ColumnReferenceExpression )
		    {
		        // Direct column reference
		        ColumnReferenceExpression colref = (ColumnReferenceExpression) expr;
			    if ( colref.isIndexed())
			    {
			        int idx = colref.getColumnindex();
			        // Special case: row[0] refers to internal rowID
			        if ( idx == 0 )
						return new Integer( odiResult.getCurrentResultIndex( ) );
					else if ( odiResult.getCurrentResult( ) != null )
						return odiResult.getCurrentResult( ).getFieldValue( idx );
					else
						return null;
			    }
			    else
			    {
			        String name = colref.getColumnName();
			        // Special case: row._rowPosition refers to internal rowID
			        if ( JSRowObject.ROW_POSITION.equals( name ) )
						return new Integer( odiResult.getCurrentResultIndex( ) );
					else if ( odiResult.getCurrentResult( ) != null )
						return odiResult.getCurrentResult( ).getFieldValue( name );
					else
						return null;
			    }
		    }
		    else
		    {
				return ScriptEvalUtil.convertNativeObjToJavaObj( 
						expr.evaluate(cx, scope ) );
		    }
		}
	    catch ( DataException e )
		{
	    	throw e;
		}
		
	}
	
	/**
	 * Returns the value of a query result expression. 
	 * A given data expression could be for one of the Selected Columns
	 * (if detail rows are used), or of an Aggregation specified 
	 * in the prepared ReportQueryDefn spec.
	 * When requesting for the value of a Selected Column, its value
	 * in the current row of the iterator will be returned.
	 * <p>
	 * Throws an exception if a result expression value is requested 
	 * out of sequence from the prepared ReportQueryDefn spec.  
	 * E.g. A group aggregation is defined to be after_last_row. 
	 * It would be out of sequence if requested before having
	 * iterated/skipped to the last row of the group. 
	 * In future release, this could have intelligence to auto recover 
	 * and performs dependent operations to properly evaluate 
	 * any out-of-sequence result values. 
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */
	public Object getValue( IBaseExpression dataExpr )
		throws DataException
	{ 
	    logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"getValue",
				"get of value IBaseExpression: " + LogUtil.toString( dataExpr) );
	    checkStarted();
	    
	    // Must advance to first row before calling getValue
	    if ( beforeFirstRow )
	    {
	    	DataException e = new DataException( ResourceConstants.NO_CURRENT_ROW );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getValue",
					"Cursor has no current row.",
					e );
	        throw e;
	    }

	    Object handle = dataExpr.getHandle();

	    if(handle instanceof CompiledExpression){
	    	CompiledExpression expr = (CompiledExpression) handle;
			Context cx = Context.enter( );
			try
			{
				return evaluateCompiledExpression( expr, odiResult, cx, scope );
			}
			finally
			{
				Context.exit( );
			}
	    }
	    else if(handle instanceof ConditionalExpression){
	    	ConditionalExpression ce = (ConditionalExpression) handle;
	    	Object resultExpr=getValue( ce.getExpression( ) );
	    	Object resultOp1=ce.getOperand1( )!=null?getValue( ce.getOperand1( ) ):null;
	    	Object resultOp2=ce.getOperand2( )!=null?getValue( ce.getOperand2( ) ):null;
	    	return ScriptEvalUtil.evalConditionalExpr( resultExpr,ce.getOperator(), resultOp1,resultOp2);
	    }
	    else{
	    	DataException e = new DataException( ResourceConstants.INVALID_EXPR_HANDLE );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getValue",
					"Invalid expression handle.",
					e );
			throw e;
	    }
	}
	
	/**
	 * Returns the value of a query result expression as a Boolean,
	 * by type casting the Object returned by getValue.
	 * <br>
	 * A convenience method for the API consumer.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression as a Boolean.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */
	public Boolean getBoolean( IBaseExpression dataExpr )
		throws DataException
	{
		try
		{
			return DataTypeUtil.toBoolean( getValue( dataExpr ));
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getBoolean",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}
	
	/**
	 * Returns the value of a query result expression as an Integer,
	 * by type casting the Object returned by getValue.
	 * <br>
	 * A convenience method for the API consumer.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression as an Integer.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */
	public Integer getInteger( IBaseExpression dataExpr )
			throws DataException
	{
		try
		{
			return DataTypeUtil.toInteger( getValue( dataExpr ));
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getInteger",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;		
		}
	}
	
	/**
	 * Returns the value of a query result expression as a Double,
	 * by type casting the Object returned by getValue.
	 * <br>
	 * A convenience method for the API consumer.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression as a Double.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */	
	public Double getDouble( IBaseExpression dataExpr )
			throws DataException
	{
		try
		{
			return DataTypeUtil.toDouble( getValue( dataExpr ));
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getDouble",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}
	
	/**
	 * Returns the value of a query result expression as a String,
	 * by type casting the Object returned by getValue.
	 * <br>
	 * A convenience method for the API consumer.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression as a String.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */	
	public String getString( IBaseExpression dataExpr )
		throws DataException
	{
		try
		{
			return DataTypeUtil.toString( getValue( dataExpr ));
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getString",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}
	
	/**
	 * Returns the value of a query result expression as a BigDecimal,
	 * by type casting the Object returned by getValue.
	 * <br>
	 * A convenience method for the API consumer.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression as a BigDecimal.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */	
	public BigDecimal getBigDecimal( IBaseExpression dataExpr )
		throws DataException
	{
		try
		{
			return DataTypeUtil.toBigDecimal( getValue( dataExpr ));
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getBigDecimal",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}
	
	/**
	 * Returns the value of a query result expression as a Date,
	 * by type casting the Object returned by getValue.
	 * <br>
	 * A convenience method for the API consumer.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given expression as a Date.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */	
	public Date getDate( IBaseExpression dataExpr )
		throws DataException
	{
		try
		{
			return DataTypeUtil.toDate( getValue( dataExpr ));
		}		
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getDate",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}
	
	/**
	 * Returns the value of a query result expression 
	 * representing Blob data.
	 * <br>
	 * If the expression value has an incompatible type,  
	 * a ClassCastException is thrown at runtime.
	 * @param dataExpr 	An IBaseExpression object provided in
	 * 					the ReportQueryDefn at the time of prepare.
	 * @return			The value of the given Blob expression.
	 * 					It could be null.
	 * @throws 			DataException if error occurs in Data Engine
	 */	
	public Blob getBlob( IBaseExpression dataExpr )
		throws DataException
	{
		try
		{
			return DataTypeUtil.toBlob(getValue( dataExpr ));
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getBlob",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBytes(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public byte[] getBytes( IBaseExpression dataExpr ) throws BirtException
	{
		try
		{
			return DataTypeUtil.toBytes( getValue( dataExpr ) );
		}
		catch ( BirtException e )
		{
	    	DataException e1 = new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
			logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getBytes",
					"An error is thrown by DataTypeUtil.",
					e1 );
			throw e1;
		}		
	}
	
	/**
	 * Advances the iterator, skipping rows to the last row in the current group 
	 * at the specified group level.
	 * This is for result sets that do not use detail rows to advance
	 * to next group.  Calling next() after skip() would position 
	 * the current row to the first row of the next group.
	 * @param groupLevel	An absolute value for group level. 
	 * 						A value of 0 applies to the whole result set.
	 * @throws 			DataException if error occurs in Data Engine
	 */
	public void skipToEnd( int groupLevel )
		throws DataException
	{ 
	    checkStarted();
	    try
		{
	    	odiResult.last( groupLevel );
		}
	    catch ( DataException e )
		{
	    	throw e;
		}
	    logger.logp( Level.FINER,
	    		ResultIterator.class.getName( ),
				"skipToEnd",
				"skipping rows to the last row in the current group" );
	}

	/**
	 * Returns the 1-based index of the outermost group
	 * in which the current row is the first row. 
	 * For example, if a query contain N groups 
	 * (group with index 1 being the outermost group, and group with 
	 * index N being the innermost group),
	 * and this function returns a value M, it indicates that the 
	 * current row is the first row in groups with 
	 * indexes (M, M+1, ..., N ).
	 * @return	1-based index of the outermost group in which 
	 * 			the current row is the first row;
	 * 			(N+1) if the current row is not at the start of any group;
	 * 			0 if the result set has no groups.
	 */
	public int getStartingGroupLevel() throws DataException
	{ 
		if ( useDetails == false )
		{
		    logger.logp( Level.FINE,
		    		ResultIterator.class.getName( ),
					"getStartingGroupLevel",
					"return the starting group level" );
			return savedStartingGroupLevel;
		}
		
		try
		{
		    logger.logp( Level.FINE,
		    		ResultIterator.class.getName( ),
					"getStartingGroupLevel",
					"return the starting group level" );
			return odiResult.getStartingGroupLevel();
		}
	    catch ( DataException e )
		{
	    	throw e;
		}
	}

	/**
	 * Returns the 1-based index of the outermost group
	 * in which the current row is the last row. 
	 * For example, if a query contain N groups 
	 * (group with index 1 being the outermost group, and group with 
	 * index N being the innermost group),
	 * and this function returns a value M, it indicates that the 
	 * current row is the last row in groups with 
	 * indexes (M, M+1, ..., N ). 
	 * @return	1-based index of the outermost group in which 
	 * 			the current row is the last row;
	 * 			(N+1) if the current row is not at the end of any group;
	 * 			0 if the result set has no groups.
	 */
	public int getEndingGroupLevel() throws DataException
	{ 
		try
		{
		    logger.logp( Level.FINE,
		    		ResultIterator.class.getName( ),
					"getEndingGroupLevel",
					"return the ending group level" );
			return odiResult.getEndingGroupLevel();
		}
	    catch ( DataException e )
		{
	    	throw e;
		}
	}

	/**
	 * Returns the secondary result specified by a SubQuery 
	 * that was defined in the prepared ReportQueryDefn.
	 * @throws 			DataException if error occurs in Data Engine
	 */
	public IResultIterator getSecondaryIterator( String subQueryName, Scriptable scope )
		throws DataException
	{ 
	    checkStarted( );
	    
	    QueryResults results = query.execSubquery( odiResult, subQueryName, scope );
	    logger.logp( Level.FINE,
	    		ResultIterator.class.getName( ),
				"getSecondaryIterator",
				"Returns the secondary result specified by a SubQuery" );
	    return results.getResultIterator();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() throws DataException
	{
		try
		{
			return new ResultMetaData( odiResult.getResultClass() );
		}
		catch ( DataException e )
		{
			throw e;
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
	 * providing a hint that the consumer is done with this result,
	 * whose resources can be safely released as appropriate.  
	 */
	public void close()
	{
	    try
	    {
	        if ( odiResult != null )
	        	odiResult.close();
		    if ( scope != null )
		    {
		        // Remove the "row" object created in the scope; it can 
		        // no longer be used since the underlying result set is gone
		        scope.delete( "row");
		        scope = null;
		    }
	    } 
	    catch ( Exception e)
	    {
	        // TODO: exception handling
	    }
	    
	    odiResult = null;
	    queryResults = null;
	    started = false;
	    logger.logp( Level.FINE,
	    		ResultIterator.class.getName( ),
				"close",
				"a ResultIterator is closed" );
	}

	/**
	 * Specifies the maximum number of rows that can be accessed 
	 * from this result iterator.  Default is no limit.
	 * @param maxLimit The new max row limit.  This value must not
	 * 					be greater than the max limit specified in the
	 * 					ReportQueryDefn.  A value of 0 means no limit.
	 * @throws 			DataException if error occurs in Data Engine
	 */
	void setMaxRows( int maxLimit ) throws DataException
	{
	}
	
	/**
	 * Retrieves the maximum number of rows that can be accessed 
	 * from this result iterator.
	 * @return	the current max row limit; a value of 0 means no limit.
	 */
	int getMaxRows()
	{
		return 0;
	}
	
	/**
	 * Only for CachedResultSet test case 
	 * @return
	 */
	org.eclipse.birt.data.engine.odi.IResultIterator getOdiResult()
	{
		return odiResult;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[])
	 */
	public boolean findGroup( Object[] groupKeyValues ) throws DataException
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

		//Return to first row.
		odiResult.first( 0 );
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
					// because group level is 1-based. We should use "i+1" to indicate current group.
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
	 * @throws DataException
	 */
	private boolean groupKeyValuesEqual( Object[] groupKeyValues, String[] columnNames, int i ) throws DataException
	{
		return ( odiResult.getCurrentResult( )
				.getFieldValue( columnNames[i] ) == null && groupKeyValues[i] == null )
				|| ( odiResult.getCurrentResult( )
						.getFieldValue( columnNames[i] ).equals( groupKeyValues[i] ) );
	}


	/**
	 * The method which extracts column name from group definition.
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
				columnName = columnName.trim().substring( 0, columnName.length( ) - 1 ).trim();	
			}
			if(columnName!=null && columnName.matches("\\Q\"\\E.*\\Q\"\\E"))
				columnName = columnName.substring(1,columnName.length()-1);
		}
		return columnName;
	}

}
