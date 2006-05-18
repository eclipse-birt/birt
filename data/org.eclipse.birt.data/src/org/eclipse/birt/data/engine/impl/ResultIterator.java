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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.IRDSave;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
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
	// context of data engine
	private DataEngineContext 		context;
	private RDSaveUtil 				rdSaveUtil;
	private Scriptable 				scope;
	
	org.eclipse.birt.data.engine.odi.IResultIterator odiResult;
	
	// needed service
	private IServiceForResultSet 	resultService;
	
	// util to findGroup
	private GroupUtil 				groupUtil;
	
	// util to get row id
	private RowIDUtil 				rowIDUtil;
	
	// used in (usesDetails == false)
	private boolean 				useDetails;
	private int 					lowestGroupLevel;
	private int 					savedStartingGroupLevel;
	
	// used for evaluate binding column value
	private int 					lastRowIndex = -1;
	private Map 					boundColumnValueMap;
	private BindingColumnsEvalUtil 	bindingColumnsEvalUtil;
	
	private int state = NOT_STARTED;
	
	private static final int NOT_STARTED = 0;
	private static final int BEFORE_FIRST_ROW = 1;
	private static final int ON_ROW = 2;
	private static final int AFTER_LAST_ROW = 3;
	private static final int CLOSED = -1;
	
	// log instance
	private static Logger logger = Logger.getLogger( ResultIterator.class.getName( ) );

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
	ResultIterator( IServiceForResultSet rService,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
		assert rService != null
				&& rService.getQueryResults( ) != null && odiResult != null
				&& scope != null;

		this.resultService = rService;
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
		return resultService.getQueryResults( );
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
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
	 */
	public int getRowId( ) throws BirtException
	{
		checkStarted( );
		
		if ( rowIDUtil == null )
			rowIDUtil = new RowIDUtil( );
		return rowIDUtil.getRowID( this.odiResult );
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
		
		if ( state == BEFORE_FIRST_ROW )
			state = ON_ROW;

		int currRowIndex = odiResult.getCurrentResultIndex( );
		
		if ( rowIndex < 0 || rowIndex >= this.odiResult.getRowCount( ) )
			throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
					new Integer( rowIndex ) );
		else if ( rowIndex < currRowIndex )
			throw new DataException( ResourceConstants.BACKWARD_SEEK_ERROR );
		else if ( rowIndex == currRowIndex )
			return;

		int gapRows = rowIndex - currRowIndex;
		for ( int i = 0; i < gapRows; i++ )
			this.next( );
	}
	
	/**
	 * @return save util used in report document GENERATION time
	 */
	private RDSaveUtil getRdSaveUtil( )
	{
		if ( this.rdSaveUtil == null )
		{
			rdSaveUtil = new RDSaveUtil( this.context,
					this.resultService.getQueryDefn( ),
					this.resultService.getQueryResults( ).getID( ),
					this.odiResult );
		}
		
		return this.rdSaveUtil;
	}
	
	//------new method for bound column name------
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getValue(java.lang.String)
	 */
	public Object getValue( String exprName ) throws BirtException
	{
		checkStarted( );
		
		logger.logp( Level.FINE,
				ResultIterator.class.getName( ),
				"getValue",
				"get of value binding column: " + LogUtil.toString( exprName ) );
		
		if ( this.resultService.getBaseExpression( exprName ) == null
				&& this.resultService.getAutoBindingExpr( exprName ) == null )
			throw new DataException( ResourceConstants.INVALID_BOUND_COLUMN_NAME,
					exprName );
		
		// currRowIndex value will be changed driven by this.next method.
		int currRowIndex = this.odiResult.getCurrentResultIndex( );
		if ( lastRowIndex < currRowIndex )
		{
			if ( bindingColumnsEvalUtil == null )
			{
				bindingColumnsEvalUtil = new BindingColumnsEvalUtil( this.odiResult,
						this.scope,
						this.getRdSaveUtil( ),
						this.resultService.getAllBindingExprs( ),
						this.resultService.getAllAutoBindingExprs( ) );
			}
			
			lastRowIndex = currRowIndex;
			boundColumnValueMap = bindingColumnsEvalUtil.getColumnsValue( );
		}
		
		Object exprValue = boundColumnValueMap.get( exprName );
		if ( exprValue instanceof BirtException )
			throw (BirtException) exprValue;

		return exprValue;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(java.lang.String)
	 */
	public Boolean getBoolean( String name ) throws BirtException
	{
		return DataTypeUtil.toBoolean( getValue( name ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getInteger(java.lang.String)
	 */
	public Integer getInteger( String name ) throws BirtException
	{
		return DataTypeUtil.toInteger( getValue( name ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDouble(java.lang.String)
	 */
	public Double getDouble( String name ) throws BirtException
	{
		return DataTypeUtil.toDouble( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getString(java.lang.String)
	 */
	public String getString( String name ) throws BirtException
	{
		return DataTypeUtil.toString( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String name ) throws BirtException
	{
		return DataTypeUtil.toBigDecimal( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDate(java.lang.String)
	 */
	public Date getDate( String name ) throws BirtException
	{
		return DataTypeUtil.toDate( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBlob(java.lang.String)
	 */
	public Blob getBlob( String name ) throws BirtException
	{
		return DataTypeUtil.toBlob( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBytes(java.lang.String)
	 */
	public byte[] getBytes( String name ) throws BirtException
	{
		return DataTypeUtil.toBytes( getValue( name ) );
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
		
		QueryResults results = resultService.execSubquery( odiResult,
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
		resultService = null;
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
			groupUtil = new GroupUtil( this.resultService.getQueryDefn( ), this );
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

				columnNames[i] = getGroupKeyExpression( group );
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
		 * @param columnExprs
		 * @param i
		 * @return
		 * @throws BirtException
		 */
		private boolean groupKeyValuesEqual(
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
				Object[] groupKeyValues, String[] columnExprs, int i )
				throws BirtException
		{
			Object fieldValue = null;
			
			Context cx = Context.enter( );
			fieldValue = ScriptEvalUtil.evalExpr( new ScriptExpression( columnExprs[i] ),
					cx,
					ResultIterator.this.scope,
					"Filter",
					0 );
			Context.exit( );

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
		private String getGroupKeyExpression( GroupDefinition group )
		{
			String columnName;
			if ( group.getKeyColumn( ) != null )
			{
				columnName = "row[\"" + group.getKeyColumn( ) +"\"]";
			}
			else
			{
				columnName = group.getKeyExpression( );
			}	
		
			return columnName;
		}
	}
	
	/**
	 * Util class to help ResultIterator to save data into report document
	 */
	class RDSaveUtil
	{
		// context info
		private DataEngineContext context;
		private String queryResultID;
		private IBaseQueryDefinition queryDefn;

		// report document save and load instance
		private IRDSave rdSave;

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

		private boolean isBasicSaved;
		
		/**
		 * @param context
		 * @param queryResultID
		 * @param odiResult
		 */
		public RDSaveUtil( DataEngineContext context,
				IBaseQueryDefinition queryDefn, String queryResultID,
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult )
		{
			this( context,
					queryDefn,
					queryResultID,
					odiResult,
					null,
					-1,
					-1,
					null );
		}

		/**
		 * @param context
		 * @param queryResultID
		 * @param odiResult
		 * @param subQueryName
		 * @param subQueryIndex
		 * @param subQueryInfo
		 */
		RDSaveUtil( DataEngineContext context, IBaseQueryDefinition queryDefn,
				String queryResultID,
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
				String subQueryName, int groupLevel, int subQueryIndex,
				int[] subQueryInfo )
		{
			this.context = context;
			this.queryDefn = queryDefn;
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

			if ( isBasicSaved == false )
			{
				isBasicSaved = true;
				this.getRdSave( )
						.saveResultIterator( (CachedResultSet) this.odiResult,
								this.groupLevel,
								this.subQueryInfo );
			}
			
			this.getRdSave( )
					.saveExprValue( odiResult.getCurrentResultIndex( ),
							dataExpr.getID( ),
							value );
		}
		
		/**
		 * @param name
		 * @param value
		 * @throws DataException
		 */
		void doSaveExpr( String name, Object value )
				throws DataException
		{
			if ( needsSaveToDoc( ) == false )
				return;
			
			if ( isBasicSaved == false )
			{
				isBasicSaved = true;
				this.getRdSave( )
						.saveResultIterator( this.odiResult,
								this.groupLevel,
								this.subQueryInfo );
			}
			
			this.getRdSave( )
					.saveExprValue( odiResult.getCurrentResultIndex( ),
							name,
							value );
		}

		/**
		 * @throws DataException
		 */
		void doSaveFinish( ) throws DataException
		{
			if ( needsSaveToDoc( ) == false )
				return;

			if ( isBasicSaved == false )
			{
				isBasicSaved = true;
				this.getRdSave( )
						.saveResultIterator( (CachedResultSet) this.odiResult,
								this.groupLevel,
								this.subQueryInfo );
			}

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

			if ( ( (ISubqueryDefinition) resultIt.resultService.getQueryDefn( ) ).applyOnGroup( ) )
				// init RDSave util of sub query
				resultIt.rdSaveUtil = new RDSaveUtil( resultIt.context,
						resultIt.resultService.getQueryDefn( ),
						resultIt.getQueryResults( ).getID( ),
						resultIt.odiResult,
						subQueryName,
						results.getGroupLevel( ),
						odiResult.getCurrentGroupIndex( results.getGroupLevel( ) ),
						odiResult.getGroupStartAndEndIndex( results.getGroupLevel( ) ) );
			else
				resultIt.rdSaveUtil = new RDSaveUtil( resultIt.context,
						resultIt.resultService.getQueryDefn( ),
						resultIt.getQueryResults( ).getID( ),
						resultIt.odiResult,
						subQueryName,
						1,
						odiResult.getCurrentResultIndex( ),
						getSpecialSubQueryInfo( odiResult.getRowCount( ) ) );
		}
		
		/**
		 * Generate sub query definition for such a sub query which is applied
		 * to each row of parent query.
		 * 
		 * @param count
		 * @return [0, 1, 1, 2, 2, 3...]
		 */
		private int[] getSpecialSubQueryInfo( int count )
		{
			int[] subQueryInfo = new int[count * 2];
			for ( int i = 0; i < count; i++ )
			{
				subQueryInfo[2 * i] = i;
				subQueryInfo[2 * i + 1] = i + 1;
			}
			return subQueryInfo;
		}
		
		/**
		 * @return
		 */
		private boolean needsSaveToDoc( )
		{
			if ( state == NOT_STARTED || state == CLOSED )
				return false;
			
			if ( context == null
					|| context.getMode( ) == DataEngineContext.DIRECT_PRESENTATION
					|| context.getMode( ) == DataEngineContext.MODE_PRESENTATION )
				return false;

			return true;
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		private IRDSave getRdSave( ) throws DataException
		{
			if ( rdSave == null )
			{
				rdSave = RDUtil.newSave( this.context,
						this.queryDefn,
						this.queryResultID,
						odiResult.getRowCount( ),
						this.subQueryName,
						this.subQueryIndex );
			}

			return rdSave;
		}
	}
	
}
