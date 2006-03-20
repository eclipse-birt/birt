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
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.expression.ColumnReferenceExpression;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.RDSave;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.eclipse.birt.data.engine.script.DataExceptionMocker;
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
	protected Scriptable scope;
	
	protected static final int NOT_STARTED = 0;
	protected static final int BEFORE_FIRST_ROW = 1;
	protected static final int ON_ROW = 2;
	protected static final int AFTER_LAST_ROW = 3;
	protected static final int CLOSED = -1;
	
	protected int state = NOT_STARTED;

	// used in (usesDetails == false)
	protected boolean useDetails;
	protected int lowestGroupLevel;
	private int savedStartingGroupLevel;

	// context of data engine
	private DataEngineContext context;
	private RDSaveUtil rdSaveUtil;
	
	private IResultService rService;
	
	// util to findGroup
	private GroupUtil groupUtil;
	
	protected static Logger logger = Logger.getLogger( ResultIterator.class.getName( ) );

	/**
	 * Constructor for report query (which produces a QueryResults)
	 * 
	 * @param context
	 * @param queryResults
	 * @param query
	 * @param odiResult
	 * @param scope
	 * @throws DataException
	 */
	ResultIterator( IResultService rService,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
		assert rService != null
				&& rService.getQueryResults( ) != null && odiResult != null
				&& scope != null;

		this.rService = rService;
		this.odiResult = odiResult;
		this.scope = scope;

		this.context = rService.getContext( );

		IBaseQueryDefinition queryDefn = rService.getQueryDefn( );
		assert queryDefn != null;
		this.useDetails = queryDefn.usesDetails( );
		this.lowestGroupLevel = queryDefn.getGroups( ).size( );
		this.start( );
		logger.logp( Level.FINER,
				ResultIterator.class.getName( ),
				"ResultIterator",
				"ResultIterator starts up" );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getScope()
	 */
	public Scriptable getScope( )
	{
		return scope;
	}

	/**
	 * Internal method to start the iterator; must be called before any other
	 * method can be used
	 */
	private void start( ) throws DataException
	{
		assert state == NOT_STARTED;

		// Note that the odiResultIterator currently has its cursor located AT
		// its first row. This iterator starts out with cursor BEFORE first row.
		state = BEFORE_FIRST_ROW;
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
	
	/*
	 * Returns the QueryResults of this result iterator. A convenience method
	 * for the API consumer.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getQueryResults()
	 */
	public IQueryResults getQueryResults( )
	{
		return rService.getQueryResults( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#next()
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
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
	 */
	public int getRowIndex( ) throws BirtException
	{
		checkStarted( );
		return odiResult.getCurrentResultIndex( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws BirtException
	{
		checkStarted( );
		throw new DataException( "unsupported in generation time" );
	}
	
	/**
	 * @param expr
	 * @param odiResult
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	public static Object evaluateCompiledExpression( CompiledExpression expr,
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
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getValue(org.eclipse.birt.data.engine.api.IBaseExpression)
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
		
		//the result might be a DataExceptionMocker.
		if ( exprValue instanceof DataExceptionMocker )
		{
			throw ((DataExceptionMocker) exprValue ).getCause( );
		}
		
		this.getRdSaveUtil( ).doSaveExpr( dataExpr, exprValue );
		
		return exprValue;
	}
	
	/**
	 * @return save util used in report document GENERATION time
	 */
	private RDSaveUtil getRdSaveUtil( )
	{
		if ( this.rdSaveUtil == null )
		{
			rdSaveUtil = new RDSaveUtil( this.context,
					this.rService.getQueryResults( ).getID( ),
					this.odiResult );
		}
		
		return this.rdSaveUtil;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public Boolean getBoolean( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toBoolean( getValue( dataExpr ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getInteger(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public Integer getInteger( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toInteger( getValue( dataExpr ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDouble(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public Double getDouble( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toDouble( getValue( dataExpr ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getString(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public String getString( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toString( getValue( dataExpr ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public BigDecimal getBigDecimal( IBaseExpression dataExpr )
			throws BirtException
	{
		return DataTypeUtil.toBigDecimal( getValue( dataExpr ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDate(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public Date getDate( IBaseExpression dataExpr ) throws BirtException
	{
		return DataTypeUtil.toDate( getValue( dataExpr ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBlob(org.eclipse.birt.data.engine.api.IBaseExpression)
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
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#skipToEnd(int)
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

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getStartingGroupLevel()
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

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws DataException
	{
		logger.logp( Level.FINE,
					ResultIterator.class.getName( ),
					"getEndingGroupLevel",
					"return the ending group level" );
		return odiResult.getEndingGroupLevel( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getSecondaryIterator(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public IResultIterator getSecondaryIterator( String subQueryName,
			Scriptable subScope ) throws DataException
	{
		checkStarted( );
		
		QueryResults results = rService.execSubquery( odiResult,
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
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#close()
	 */
	public void close( ) throws BirtException
	{
		// save results when neededs
		this.getRdSaveUtil( ).doSaveFinish( );

		if ( odiResult != null )
				odiResult.close( );

		odiResult = null;
		rService = null;
		state = CLOSED;
		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"close",
				"a ResultIterator is closed" );
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
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[])
	 */
	public boolean findGroup( Object[] groupKeyValues ) throws BirtException
	{
		if ( groupUtil == null )
			groupUtil = new GroupUtil( this.rService.getQueryDefn( ), this );
		return groupUtil.findGroup( groupKeyValues );
	}

	/**
	 * Util class to findGroup
	 */
	private class GroupUtil
	{
		private IBaseQueryDefinition queryDefn;
		private ResultIterator resultIterator;
		
		/**
		 * @param query
		 * @param resultIterator
		 */
		private GroupUtil( IBaseQueryDefinition queryDefn,
				ResultIterator resultIterator )
		{
			this.queryDefn = queryDefn;
			this.resultIterator = resultIterator;
		}
		
		/**
		 * @param query
		 * @param resultIterator
		 * @param groupKeyValues
		 * @return
		 * @throws BirtException
		 */
		public boolean findGroup( Object[] groupKeyValues )
				throws BirtException
		{
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult = resultIterator.getOdiResult( );
			
			List groups = queryDefn.getGroups( );
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
			if ( odiResult.getCurrentResult( ) == null )
				return false;
			do
			{
				for ( int i = 0; i < columnNames.length; i++ )
				{
					if ( groupKeyValuesEqual( odiResult,
							groupKeyValues,
							columnNames,
							i ) )
					{
						if ( i == columnNames.length - 1 )
							return true;
					}
					else
					{
						// because group level is 1-based. We should use "i+1"
						// to indicate current group.
						resultIterator.skipToEnd( i + 1 );
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
		private boolean groupKeyValuesEqual(
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
				Object[] groupKeyValues, String[] columnNames, int i )
				throws BirtException
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
				if ( fieldValue.getClass( )
						.equals( groupKeyValues[i].getClass( ) ) )
				{
					retValue = isTwoObjectEqual( fieldValue, groupKeyValues[i] );
				}
				else
				{
					Object convertedOb = DataTypeUtil.convert( groupKeyValues[i],
							fieldValue.getClass( ) );
					retValue = isTwoObjectEqual( fieldValue, convertedOb );
				}
			}

			return retValue;
		}

		/**
		 * @param value1
		 * @param value2
		 * @return
		 */
		private boolean isTwoObjectEqual( Object value1, Object value2 )
		{
			//The Date object should be processed individually 
			if( value1 instanceof Date && value2 instanceof Date)
				return ((Date)value1).getTime() == ((Date)value2).getTime();
			else
			    return  value1.equals( value2 );
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

				if ( columnName.trim( ).toUpperCase( ).startsWith( "ROW" ) )
				{
					String temp = columnName.trim( ).substring( 3 ).trim();
					if ( temp.startsWith( "." ) )
						columnName = temp.replaceFirst( "\\Q.\\E", "" );
					else if ( temp.startsWith( "[" ) )
					{
						columnName = temp.replaceFirst( "\\Q[\\E", "" );
						columnName = columnName.trim( ).substring( 0,
								columnName.length( ) - 1 ).trim( );
					}
				}
				if ( columnName != null
						&& columnName.matches( "\\Q\"\\E.*\\Q\"\\E" ) )
					columnName = columnName.substring( 1,
							columnName.length( ) - 1 );
			}
			// Dealing with columnNames enclosed by double quotation mark.
			if ( columnName.startsWith( "\\\"" )
					&& columnName.endsWith( "\\\"" ) )
			{
				columnName = "\""
						+ columnName.substring( 2, columnName.length( ) - 2 )
						+ "\"";
			}
			return columnName;
		}
	}
	
	/**
	 * Util class to help ResultIterator to save data into report document
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

			this.getRdSave( ).saveFinish( odiResult.getCurrentResultIndex( ) );
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
