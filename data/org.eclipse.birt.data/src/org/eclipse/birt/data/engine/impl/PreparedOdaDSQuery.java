/*
 ******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 * 
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceFactory;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IParameterMetaData;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * A prepared query which access an ODA data source.
 */
public class PreparedOdaDSQuery extends PreparedDataSourceQuery
		implements	IPreparedQuery
{
	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @param dataSetDesign
	 * @throws DataException
	 */
	PreparedOdaDSQuery( DataEngineImpl dataEngine, IQueryDefinition queryDefn,
			IBaseDataSetDesign dataSetDesign, Map appContext )
			throws DataException
	{
		super( dataEngine, queryDefn, dataSetDesign, appContext );
	}	
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor()
	{
		return new OdaDSQueryExecutor();
	}	

	/**
	 * @param publicProps
	 * @param privateProps
	 * @return
	 */
	private static Map copyProperties( Map publicProps, Map privateProps )
	{
	    if ( publicProps.isEmpty() && privateProps.isEmpty() )
	        return null;		// nothing to copy
	    
	    Map driverProps = new HashMap();
    	if ( ! publicProps.isEmpty() )
    	    driverProps.putAll( publicProps );
    	if ( ! privateProps.isEmpty() )
    	    driverProps.putAll( privateProps );

    	return driverProps;
	}
	
	/**
	 * @param odiDSQuery
	 * @param props
	 * @throws DataException
	 */
	private static void addProperty( IDataSourceQuery odiDSQuery, Map props )
			throws DataException
	{
	    if ( props == null  )
	        return;		// nothing to add
	    
	    Set entries = props.entrySet();
	    Iterator it = entries.iterator();
	    while ( it.hasNext() )
	    {
	    	Map.Entry entry = (Map.Entry)it.next();
	        String propName = (String) entry.getKey();
	        String value = (String) entry.getValue();
	        if ( propName == null || propName.length() == 0 )
	            continue;	// skip empty property name
	        
            odiDSQuery.addProperty( propName, value );
	    }
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
	 */
    public Collection getParameterMetaData() throws DataException
	{
    	OdaDSQueryExecutor exec = new OdaDSQueryExecutor();
    	return exec.getParameterMetaData();
	}
    
    /**
     * 
	 * Concrete class of DSQueryExecutor used in PreparedExtendedDSQuery
	 * 
	 */
	public class OdaDSQueryExecutor extends DSQueryExecutor
	{
		// prepared query
		private IPreparedDSQuery odiPreparedQuery;
		
		/**
		 * @return prepared query
		 */
		public IPreparedDSQuery getPreparedOdiQuery( )
		{
			return odiPreparedQuery;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiDataSource()
		 */
		protected IDataSource createOdiDataSource( ) throws DataException
		{
			OdaDataSourceRuntime extDS = (OdaDataSourceRuntime) dataSource;
			assert extDS != null;

			// Obtains an odi data source matching the dynamic definition
			// of the extended data source
		    String driverName = extDS.getExtensionID();
		    if ( driverName == null || driverName.length() == 0 )
		        throw new DataException( ResourceConstants.MISSING_DATASOURCE_EXT_ID,
		        		extDS.getName( ) );
		    
		    // merge public and private driver properties into a single Map
		    Map driverProps = 
		        copyProperties( extDS.getPublicProperties(), 
		        		extDS.getPrivateProperties() );
		    	    
		    // calls ODI Data Source Factory to provide an ODI data source
			// object that matches the given properties
		    PreparedOdaDSQuery self = PreparedOdaDSQuery.this;
			return DataSourceFactory.getFactory( )
					.getDataSource( driverName,
							driverProps,
							this.dataSource.getDesign( ),
							this.dataSet.getDesign( ),
							self.queryDefn.getInputParamBindings( ),
							DataSetCacheUtil.getCacheOption( self.dataEngine.getContext( ),
									appContext ),
							self.dataEngine.getContext( ).getCacheCount( ) );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery( ) throws DataException
		{
			OdaDataSetRuntime extDataSet = (OdaDataSetRuntime) dataSet;
			assert extDataSet != null;
			assert odiDataSource != null;
			
			IDataSourceQuery odiQuery = null;
			String dataSetType = extDataSet.getExtensionID( );
			String dataText = extDataSet.getQueryText( );
			odiQuery = odiDataSource.newQuery( dataSetType, dataText );
			return odiQuery;
	 	}
		
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#populateOdiQuery()
		 */
		protected void populateOdiQuery( ) throws DataException
		{
			super.populateOdiQuery( );
			
			OdaDataSetRuntime extDataSet = (OdaDataSetRuntime) dataSet;
			assert extDataSet != null;
			
			IDataSourceQuery odiDSQuery = (IDataSourceQuery) odiQuery;
			assert odiDSQuery != null;
			
			// assign driver properties
		    addProperty( odiDSQuery, extDataSet.getPublicProperties() );
		    addProperty( odiDSQuery, extDataSet.getPrivateProperties() );
		   
     		// assign parameter hints and result column hints
		    odiDSQuery.setParameterHints( 
		    		resolveDataSetParameters( true) );
		  
		    if ( extDataSet.getResultSetHints() != null )
		    {
		    	List source = extDataSet.getResultSetHints(); 
		    	int count = source.size();
		    	ArrayList hints = new ArrayList( count );
		    	for ( int i = 0; i < count; i ++)
		    	{
		    		IColumnDefinition def = (IColumnDefinition) source.get(i);
		    		IDataSourceQuery.ResultFieldHint hint = 
		    				new IDataSourceQuery.ResultFieldHint( def.getColumnName());
		    		hint.setPosition( def.getColumnPosition());
		    		hint.setAlias( def.getAlias());
		    		hint.setDataType( def.getDataType());
		    		hints.add( hint );
		    	}
			    odiDSQuery.setResultHints( hints );
		    }	

		    // assign computed columns and projected columns
			// declare computed columns as custom fields
		    List ccList = extDataSet.getComputedColumns( );
			if ( ccList != null )
			{
				for ( int i = 0; i < ccList.size( ); i++ )
				{
					IComputedColumn cc = (IComputedColumn) ccList.get( i );
					odiDSQuery.declareCustomField( cc.getName( ),
							cc.getDataType( ) );
				}
			}
				
			// specify column projection, if any
	        odiDSQuery.setResultProjection( getReportQueryDefn().getColumnProjection() );
			
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected IResultIterator executeOdiQuery( IEventHandler eventHandler )
				 throws DataException
		{
			dataSet.afterOpen();
			OdaDataSetRuntime odaDataSet = (OdaDataSetRuntime) dataSet;
			assert odaDataSet != null;
			
			assert odiPreparedQuery != null;
			return odiPreparedQuery.execute( eventHandler );			
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#prepareOdiQuery()
		 */
		protected void prepareOdiQuery( ) throws DataException
		{
			IDataSourceQuery odiDSQuery = (IDataSourceQuery) odiQuery;
			assert odiDSQuery != null;

		    // prepare data set's odi query
			assert odiPreparedQuery == null;	// should not prepare more than once
			
			odiPreparedQuery = odiDSQuery.prepare();
		}
		
		
		/**
		 * Implements IPreparedQuery.getParameterMetadata. This method prepares
		 * the odi data source and query, and returns the query's parameter
		 * metadata as a Collection of ParameterMetadata objects.
		 * 
		 * @return
		 */
	    private Collection getParameterMetaData( ) throws DataException
		{
			// Create the data set runtime
			dataSet = newDataSetRuntime();
			assert dataSet != null;
			OdaDataSetRuntime odaDataSet = (OdaDataSetRuntime) dataSet;
			dataSource = findDataSource( );
			openDataSource( );
			
			// Run beforeOpen script now so the script can modify the DataSetRuntime properties
			dataSet.beforeOpen();

			// Create and populate odi query
			odiQuery = createOdiQuery( );
			IDataSourceQuery odiDSQuery = (IDataSourceQuery) odiQuery;
			assert odiDSQuery != null;
			
			// assign driver properties and parameter hints; these are the only
			// information we need from the data set to get param metadata
		    addProperty( odiDSQuery, odaDataSet.getPublicProperties() );
		    addProperty( odiDSQuery, odaDataSet.getPrivateProperties() );

		    odiDSQuery.setParameterHints(
		    		resolveDataSetParameters( false ) );
		    
		    // Prepare odi query; parameter metadata is available after the prepare call
			prepareOdiQuery( );
			
			assert odiPreparedQuery != null;

	        Collection odiParamsInfo = odiPreparedQuery.getParameterMetaData();
	        if ( odiParamsInfo == null || odiParamsInfo.isEmpty() )
	            return null;
	        
	        // iterates thru the most up-to-date collection, and
	        // wraps each of the ODI parameter metadata object
	        ArrayList paramMetaDataList = new ArrayList( odiParamsInfo.size() );
	        Iterator odiParamMDIter = odiParamsInfo.iterator();
	        while ( odiParamMDIter.hasNext() )
	        {
	        	IParameterMetaData odiMetaData = 
	                (IParameterMetaData) odiParamMDIter.next();
	        	ParameterMetaData apiMetaData = new ParameterMetaData( odiMetaData );
	            paramMetaDataList.add( apiMetaData );
	        }
	        return paramMetaDataList;
		}
	}
	
}
