/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.RDSave;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * When there is no data set, this instance will be created.
 */
public class PreparedDummyQuery implements IPreparedQuery
{
	private DataEngineContext context;
	private IQueryDefinition queryDefn;
	private ExprManager exprManager;
	private Scriptable sharedScope;

	/**
	 * @param context 
	 * @param queryDefn
	 */
	PreparedDummyQuery( DataEngineContext context, IQueryDefinition queryDefn,
			Scriptable sharedScope )
	{
		assert queryDefn != null;
		
		this.context = context;
		this.queryDefn = queryDefn;
		this.sharedScope = sharedScope;
		this.exprManager = new ExprManager( );
		this.exprManager.addExpr( queryDefn.getResultSetExpressions( ), 0 );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getReportQueryDefn()
	 */
	public IQueryDefinition getReportQueryDefn( )
	{
		return queryDefn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
	 */
	public Collection getParameterMetaData( ) throws BirtException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execute( Scriptable queryScope ) throws BirtException
	{
		return execute( null, queryScope );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.eclipse.birt.data.engine.api.IQueryResults, org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execute( IQueryResults outerResults,
			Scriptable queryScope ) throws BirtException
	{
		
		return new QueryResults( this, exprManager, getScope( queryScope ) );
	}

	/**
	 * @param queryScope
	 * @return
	 */
	private Scriptable getScope( Scriptable queryScope )
	{
		Scriptable topScope = null;
		if ( queryScope != null )
			topScope = queryScope;
		else
			topScope = sharedScope;

		Scriptable executionScope = null;
		Context cx = Context.enter( );
		try
		{
			executionScope = cx.newObject( topScope );
			executionScope.setParentScope( topScope );
			executionScope.setPrototype( sharedScope );
		}
		finally
		{
			Context.exit( );
		}
		return executionScope;
	}
	
	/**
	 * 
	 */
	private class QueryResults implements IQueryResults
	{
		private IPreparedQuery preparedQuery;
		private ExprManager exprManager;
		private Scriptable queryScope;
		
		private String queryResultID;

		/**
		 * @param preparedQuery
		 */
		private QueryResults( IPreparedQuery preparedQuery,
				ExprManager exprManager, Scriptable queryScope )
		{
			this.preparedQuery = preparedQuery;
			this.exprManager = exprManager;
			this.queryScope = queryScope;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getID()
		 */
		public String getID( )
		{
			if ( queryResultID == null )
				queryResultID = IDUtil.nextQursID( );
			
			return queryResultID;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
		 */
		public IPreparedQuery getPreparedQuery( )
		{
			return this.preparedQuery;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData( ) throws BirtException
		{
			return null;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
		 */
		public IResultIterator getResultIterator( ) throws BirtException
		{
			return new ResultIterator( this, exprManager, queryScope );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
		 */
		public void close( ) throws BirtException
		{
		}
	}

	/**
	 * 
	 */
	private class ResultIterator implements IResultIterator
	{
		private QueryResults queryResults;
		private ExprManager exprManager;
		private Scriptable queryScope;

		private RDSaveUtil rdSaveUtil;
		
		private final static int NOT_START = 0;
		private final static int IN_ROW = 1;
		private final static int ENDED = 2;

		private int openStatus = NOT_START;

		/**
		 * @throws BirtException
		 */
		private void checkOpened( ) throws BirtException
		{
			if ( openStatus != IN_ROW )
				throw new DataException( "" );
		}

		/**
		 * @param queryResults
		 * @param queryScope
		 */
		private ResultIterator( QueryResults queryResults,
				ExprManager exprManager, Scriptable queryScope )
		{
			this.queryResults = queryResults;
			this.exprManager = exprManager;
			this.queryScope = queryScope;

			queryScope.put( "row",
					queryScope,
					new JSTempRowObject( exprManager, queryScope ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getQueryResults()
		 */
		public IQueryResults getQueryResults( )
		{
			return this.queryResults;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getScope()
		 */
		public Scriptable getScope( )
		{
			return this.queryScope;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData( ) throws BirtException
		{
			return new ResultMetaData( new ResultClass( new ArrayList( ) ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#next()
		 */
		public boolean next( ) throws BirtException
		{
			if ( this.openStatus == NOT_START )
			{
				this.openStatus = IN_ROW;
				return true;
			}
			else if ( this.openStatus == IN_ROW )
			{
				this.openStatus = ENDED;
				return false;
			}
			else
			{
				throw new DataException( "is ended" );
			}
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
		 */
		public int getRowIndex( ) throws BirtException
		{
			checkOpened( );

			return 0;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
		 */
		public void moveTo( int rowIndex ) throws BirtException
		{
			this.checkOpened( );

			if ( rowIndex > 0 )
				throw new DataException( "" );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getValue(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public Object getValue( IBaseExpression dataExpr ) throws BirtException
		{
			checkOpened( );

			Object value = ExprEvaluateUtil.evaluateRawExpression( dataExpr,
					queryScope );
			this.getRdSaveUtil( ).doSaveExpr( dataExpr.getID( ), value );
			return value;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public Boolean getBoolean( IBaseExpression dataExpr )
				throws BirtException
		{
			return DataTypeUtil.toBoolean( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getInteger(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public Integer getInteger( IBaseExpression dataExpr )
				throws BirtException
		{
			return DataTypeUtil.toInteger( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDouble(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public Double getDouble( IBaseExpression dataExpr )
				throws BirtException
		{
			return DataTypeUtil.toDouble( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getString(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public String getString( IBaseExpression dataExpr )
				throws BirtException
		{
			return DataTypeUtil.toString( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public BigDecimal getBigDecimal( IBaseExpression dataExpr )
				throws BirtException
		{
			return DataTypeUtil.toBigDecimal( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDate(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public Date getDate( IBaseExpression dataExpr ) throws BirtException
		{
			return DataTypeUtil.toDate( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBlob(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public Blob getBlob( IBaseExpression dataExpr ) throws BirtException
		{
			return DataTypeUtil.toBlob( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBytes(org.eclipse.birt.data.engine.api.IBaseExpression)
		 */
		public byte[] getBytes( IBaseExpression dataExpr ) throws BirtException
		{
			return DataTypeUtil.toBytes( this.getValue( dataExpr ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getValue(java.lang.String)
		 */
		public Object getValue( String name ) throws BirtException
		{
			checkOpened( );

			Object value = ExprEvaluateUtil.evaluateRawExpression( this.exprManager.getExpr( name ),
					queryScope );
			this.getRdSaveUtil( ).doSaveExpr( name, value );
			return value;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(java.lang.String)
		 */
		public Boolean getBoolean( String name ) throws BirtException
		{
			return DataTypeUtil.toBoolean( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getInteger(java.lang.String)
		 */
		public Integer getInteger( String name ) throws BirtException
		{
			return DataTypeUtil.toInteger( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDouble(java.lang.String)
		 */
		public Double getDouble( String name ) throws BirtException
		{
			return DataTypeUtil.toDouble( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getString(java.lang.String)
		 */
		public String getString( String name ) throws BirtException
		{
			return DataTypeUtil.toString( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(java.lang.String)
		 */
		public BigDecimal getBigDecimal( String name ) throws BirtException
		{
			return DataTypeUtil.toBigDecimal( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDate(java.lang.String)
		 */
		public Date getDate( String name ) throws BirtException
		{
			return DataTypeUtil.toDate( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBlob(java.lang.String)
		 */
		public Blob getBlob( String name ) throws BirtException
		{
			return DataTypeUtil.toBlob( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBytes(java.lang.String)
		 */
		public byte[] getBytes( String name ) throws BirtException
		{
			return DataTypeUtil.toBytes( this.getValue( name ) );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#skipToEnd(int)
		 */
		public void skipToEnd( int groupLevel ) throws BirtException
		{
			this.checkOpened( );

			if ( groupLevel > 0 )
				throw new DataException( "invalid group level value" );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getStartingGroupLevel()
		 */
		public int getStartingGroupLevel( ) throws BirtException
		{
			this.checkOpened( );

			return 0;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getEndingGroupLevel()
		 */
		public int getEndingGroupLevel( ) throws BirtException
		{
			this.checkOpened( );

			return 0;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getSecondaryIterator(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public IResultIterator getSecondaryIterator( String subQueryName,
				Scriptable scope ) throws BirtException
		{
			this.checkOpened( );

			return null;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#close()
		 */
		public void close( ) throws BirtException
		{
			this.openStatus = ENDED;
			this.getRdSaveUtil( ).doSaveFinish( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[])
		 */
		public boolean findGroup( Object[] groupKeyValues )
				throws BirtException
		{
			this.checkOpened( );

			return false;
		}
		
		/**
		 * @return
		 */
		private RDSaveUtil getRdSaveUtil( )
		{
			if ( this.rdSaveUtil == null )
			{
				rdSaveUtil = new RDSaveUtil( context,
						queryDefn,
						queryResults.getID( ) );
			}

			return this.rdSaveUtil;
		}
		
	}

	/**
	 *
	 */
	private static class JSTempRowObject extends ScriptableObject
	{
		private ExprManager exprManager;
		private Scriptable scope;

		/** */
		private static final long serialVersionUID = -7841512175200620757L;

		/**
		 * @param exprManager
		 * @param scope
		 */
		private JSTempRowObject( ExprManager exprManager, Scriptable scope )
		{
			this.exprManager = exprManager;
			this.scope = scope;
		}

		/*
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "row";
		}

		/*
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 *      org.mozilla.javascript.Scriptable)
		 */
		public Object get( String name, Scriptable start )
		{
			IBaseExpression baseExpr = exprManager.getExpr( name );

			try
			{
				Object value = ExprEvaluateUtil.evaluateRawExpression( baseExpr,
						scope );
				return JavascriptEvalUtil.convertToJavascriptValue( value,
						scope );
			}
			catch ( BirtException e )
			{
				return null;
			}
		}
	}
	
	/**
	 *
	 */
	private class RDSaveUtil
	{
		// context info
		private DataEngineContext context;
		private String queryResultID;
		private IBaseQueryDefinition queryDefn;

		// report document save and load instance
		private RDSave rdSave;
		
		private boolean isBasicSaved;
		
		/**
		 * @param context
		 * @param queryDefn
		 * @param queryResultID
		 */
		RDSaveUtil( DataEngineContext context, IBaseQueryDefinition queryDefn,
				String queryResultID )
		{
			this.context = context;
			this.queryDefn = queryDefn;
			this.queryResultID = queryResultID;
		}
		
		/**
		 * @param name
		 * @param value
		 * @throws DataException
		 */
		void doSaveExpr( String name, Object value ) throws DataException
		{
			if ( needsSaveToDoc( ) == false )
				return;

			if ( isBasicSaved == false )
			{
				isBasicSaved = true;
				this.getRdSave( )
						.saveResultIterator( new DummyCachedResult( ),
						-1,
						null );
			}

			this.getRdSave( ).saveExprValue( 0, name, value );
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
				this.getRdSave( ).saveResultIterator( new DummyCachedResult( ),
						-1,
						null );
			}

			this.getRdSave( ).saveFinish( 0 );
		}
		
		/**
		 * @return
		 */
		private boolean needsSaveToDoc( )
		{
			if ( context == null
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
						this.queryDefn,
						this.queryResultID,
						1,
						null,
						-1 );
			}

			return rdSave;
		}		
	}
	
	/**
	 *
	 */
	private class DummyCachedResult extends CachedResultSet
	{
		/*
		 * @see org.eclipse.birt.data.engine.executor.transform.CachedResultSet#doSave(java.io.OutputStream,
		 *      java.io.OutputStream, java.io.OutputStream, boolean)
		 */
		public void doSave( OutputStream resultClassStream,
				OutputStream dataSetDataStream, OutputStream groupInfoStream,
				boolean isSubQuery ) throws DataException
		{
			try
			{
				IOUtil.writeInt( resultClassStream, 0 );
				if ( dataSetDataStream != null )
					IOUtil.writeInt( dataSetDataStream, 0 );
				IOUtil.writeInt( groupInfoStream, 0 );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR,
						e,
						"Result Class" );
			}
		}
	}

}
