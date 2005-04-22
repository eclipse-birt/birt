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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceFactory;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A prepared query which uses a Script Data Source.
 */

class PreparedScriptDSQuery extends PreparedDataSourceQuery 
	implements	IPreparedQuery
{
	PreparedScriptDSQuery( DataEngineImpl dataEngine, IQueryDefinition queryDefn, 
			IBaseDataSetDesign dataSetDesign ) throws DataException
	{
		super( dataEngine, queryDefn, dataSetDesign );
		logger.logp( Level.FINER,
				PreparedScriptDSQuery.class.getName( ),
				"PreparedScriptDSQuery",
				"PreparedScriptDSQuery starts up." );
	}

	
	/**
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#newExecutor()
	 */
	protected Executor newExecutor()
	{
		return new ScriptDSQueryExecutor();
	}
	
	class ScriptDSQueryExecutor extends DSQueryExecutor
	{
		private ResultClass resultClass;
		private CustomDataSet customDataSet;
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiDataSource()
		 */
		protected IDataSource createOdiDataSource( )
		{
			// An empty odi data source is used for script data set
			return DataSourceFactory.getFactory().newDataSource( null );
		}
	
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery()  throws DataException
		{
			assert odiDataSource != null;
			ICandidateQuery candidateQuery = odiDataSource.newCandidateQuery( );
			return candidateQuery;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#populateOdiQuery()
		 */
		protected void populateOdiQuery( ) throws DataException
		{
			super.populateOdiQuery( );
			
			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			assert candidateQuery != null;
			
			List resultHints = dataSet.getResultSetHints( );
			
			List columnsList = new ArrayList( );
			Iterator it = resultHints.iterator( );
			for ( int j = 0; it.hasNext( ); j++ )
			{
				IColumnDefinition columnDefn = (IColumnDefinition) it.next( );
				
				// All columns are declared as custom to allow as to set column value
				// at runtime
				ResultFieldMetadata columnMetaData = new ResultFieldMetadata( j + 1,
						columnDefn.getColumnName( ),
						columnDefn.getColumnName( ),
						DataType.getClass( columnDefn.getDataType( ) ),
						null /* nativeTypeName */, 
						true );
				columnsList.add( columnMetaData );
			}
			resultClass = new ResultClass( columnsList );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#prepareOdiQuery()
		 */
		protected void prepareOdiQuery(  ) throws DataException
		{
			assert odiQuery != null;
			assert resultClass != null;
			assert dataSet instanceof ScriptDataSetRuntime;
			
			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			customDataSet = new CustomDataSet( );
			candidateQuery.setCandidates( customDataSet );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected IResultIterator executeOdiQuery( )
				throws DataException
		{	
			// prepareOdiQuery must be called before			
			customDataSet.open();
			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			return candidateQuery.execute( );
		}
	
		private final class CustomDataSet implements ICustomDataSet
		{
			/* (non-Javadoc)
			 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#getResultClass()
			 */
			public IResultClass getResultClass( )
			{
				return resultClass;
			}
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#open()
			 */
			public void open( ) throws DataException
			{
				((ScriptDataSetRuntime) dataSet).runOpenScript();
			}
			
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#fetch()
			 */
			public IResultObject fetch( ) throws DataException
			{
				Object[] fields = new Object[resultClass.getFieldCount( )];
				ResultObject resultObject = new ResultObject( resultClass,
						fields );
				
				rowObject.setRowObject( resultObject, true );
				Object evaResult = ((ScriptDataSetRuntime) dataSet).runFetchScript();
	
				if ( evaResult instanceof Boolean == false )
					throw new DataException( ResourceConstants.INVALID_FETCH_SCIRPT );
				
				if ( ( (Boolean) evaResult ).booleanValue( ) == false )
					resultObject = null;
	
				return resultObject;
			}
			
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#close()
			 */
			public void close( ) throws DataException
			{
				((ScriptDataSetRuntime) dataSet).runCloseScript();
			}
	
		}
	}
}