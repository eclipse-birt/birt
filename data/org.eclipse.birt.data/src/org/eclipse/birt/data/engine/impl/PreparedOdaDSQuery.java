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
import java.util.logging.Level;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceFactory;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IParameterMetaData;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

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
		logger.logp( Level.FINER,
				PreparedOdaDSQuery.class.getName( ),
				"PreparedExtendedDSQuery",
				"PreparedExtendedDSQuery starts up" );
	}	
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.PreparedQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor()
	{
		return new ExtendedDSQueryExecutor();
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
    	ExtendedDSQueryExecutor exec = new ExtendedDSQueryExecutor();
    	return exec.getParameterMetaData();
	}
    
    /**
     * 
	 * Concrete class of DSQueryExecutor used in PreparedExtendedDSQuery
	 * 
	 */
	public class ExtendedDSQueryExecutor extends DSQueryExecutor
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
		    	    
		    // calls ODI Data Source Factory to provide an ODI data source object
		    // that matches the given properties
		    return DataSourceFactory.getFactory( ).getDataSource( driverName,
					driverProps,
					this.dataSource.getDesign( ),
					this.dataSet.getDesign( ) );
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
		    odiDSQuery.setParameterDefnAndValBindings( getMergedParameters(extDataSet, getQueryScope(), true) );
		  
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
		protected IResultIterator executeOdiQuery(  )
				 throws DataException
		{
			dataSet.afterOpen();
			OdaDataSetRuntime odaDataSet = (OdaDataSetRuntime) dataSet;
			assert odaDataSet != null;
			
			assert odiPreparedQuery != null;
			return odiPreparedQuery.execute( );			
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
		 * Get the ParameterDefinition List of an odiQuery. The list may include
		 * parameters defined in dataset and query
		 * 
		 * @param dataSetRT
		 * @param scope
		 * @return
		 * @throws DataException
		 */
		private Collection getMergedParameters( OdaDataSetRuntime dataSetRT,
				Scriptable scope, boolean evaluateValue ) throws DataException
		{
			assert dataSetRT != null;
			
			Context cx = Context.enter( );
			List result = new ArrayList();
			List dataSetParams = dataSetRT.getParameters();
			try
			{
			    Collection paramBinds = mergeInputParameterBindings(dataSetRT.getInputParamBindings( ),
				        ( (IQueryDefinition) getReportQueryDefn() ).getInputParamBindings( ));

				Iterator paramBindIterator = paramBinds.iterator( );
				while ( paramBindIterator.hasNext( ) )
				{
					
					IInputParameterBinding iParamBind = (IInputParameterBinding) paramBindIterator.next( );
					Object evaluateResult = evaluateValue?evaluateInputParameterValue( scope, cx, iParamBind ):iParamBind.getExpr();
					
					addParamDefnAndValBindingToList( result, dataSetParams, iParamBind, evaluateResult );
				}
			}
			finally
			{
				Context.exit( );
			}
			mergeInputParamBindingAndDataSetParams( result, dataSetParams );
			return result;
		}

		/**
		 * @param scope
		 * @param cx
		 * @param iParamBind
		 * @return
		 * @throws DataException
		 */
		private Object evaluateInputParameterValue( Scriptable scope, Context cx, IInputParameterBinding iParamBind ) throws DataException
		{
			// Evaluate Expression:
			// If the expression has been prepared, 
			// use its handle to getValue() from outerResultIterator
			// else use Rhino to evaluate in corresponding scope
			Object evaluateResult = null;
			Scriptable evaluateScope = scope;
			
			if ( outerResults != null )
			{
				try
				{
					evaluateScope = outerResults.getQueryScope( );
					if (iParamBind.getExpr().getHandle() != null)
						evaluateResult = outerResults
								.getResultIterator().getValue(iParamBind.getExpr());
				}
				catch (BirtException e)
				{
					//do not expect a exception here.
					DataException dataEx= new DataException( ResourceConstants.UNEXPECTED_ERROR, e );
					logger.logp( Level.FINE,
							PreparedOdaDSQuery.class.getName( ),
							"getMergedParameters",
							"Error occurs in IQueryResults.getResultIterator()",
							e );
					throw dataEx;
				}
			}
			
			if (evaluateResult == null)
				evaluateResult = ScriptEvalUtil.evalExpr( iParamBind.getExpr( ),
						cx,
						evaluateScope,
						"ParamBinding(" + iParamBind.getName( ) + ")",
						0 );
						
			if ( evaluateResult == null)
			    throw new DataException(ResourceConstants.DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL);
			return evaluateResult;
		}
		
		/**
		 * @param result
		 * @param dataSetParams
		 * @param iParamBind
		 * @param value 
		 * @throws DataException
		 */
		private void addParamDefnAndValBindingToList( List result, List dataSetParams, IInputParameterBinding iParamBind, Object value ) throws DataException
		{
			ParameterDefinition pd = createNewParameterDefinition(iParamBind);
			
			IParameterDefinition paramDefn = getParameterDefn(dataSetParams, iParamBind.getPosition(), iParamBind.getName());
			String defaultValue = null;
			try
			{
				defaultValue = DataTypeUtil.toString( value );
			}
			catch ( BirtException e )
			{
				throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR,
						e );
			}
			if ( paramDefn == null )
			{
				pd.setInputOptional( false );
				result.add(getParamDefnAndValBinding( pd, defaultValue ));
			}
			else
			{
				result.add(getParamDefnAndValBinding( paramDefn, defaultValue ));
			}
		}

		/**
		 * @param result
		 * @param dataSetParams
		 */
		private void mergeInputParamBindingAndDataSetParams( List result, List dataSetParams )
		{
			for( int i = 0; i < dataSetParams.size(); i ++)
			{
				IParameterDefinition pdInstance = (IParameterDefinition)dataSetParams.get(i);
				if ( !isParameterBindingExists( result, pdInstance ) )
					result.add( new ParamDefnAndValBinding( pdInstance, pdInstance.getDefaultInputValue()) );
			}
		}

		/**
		 * @param result
		 * @param pdInstance
		 * @return
		 */
		private boolean isParameterBindingExists( List result, IParameterDefinition pdInstance )
		{
			boolean exist = false;
			for ( int j = 0; j < result.size(); j ++)
			{
				if( ((ParamDefnAndValBinding)result.get(j)).getParamDefn().equals(pdInstance))
				{
					exist = true;
					break;
				}
			}
			return exist;
		}

		/**
		 * Merge ParameterBindings defined in dataset and query. The
		 * ParameterBinding defined in query has higher priority than that
		 * defined in dataset.
		 * 
		 * @param bindsFromDataset
		 * @param bindsFromQueryDefn
		 * @return
		 */
		private Collection mergeInputParameterBindings(Collection bindsFromDataset, Collection bindsFromQueryDefn)
	    {
	        ArrayList paramBinds = new ArrayList();
	     	paramBinds.addAll(bindsFromQueryDefn);
			Iterator it = bindsFromDataset.iterator();
			IInputParameterBinding temp = null;
			while(it.hasNext())
			{
			    temp = (IInputParameterBinding)it.next();
			    if(isInputParameterBindingExist(paramBinds,temp)) {
			        continue;
			    }
			    paramBinds.add(temp);
			}
			return paramBinds;
	    }
	    
		/**
		 * Detect whether an IInputParameterBinding defined in query exists in
		 * data set.
		 * 
		 * @param list
		 * @param parameterBinding
		 * @return
		 */
   	    private boolean isInputParameterBindingExist(Collection list, IInputParameterBinding parameterBinding)
	    {
	        Iterator it = list.iterator();
	        IInputParameterBinding ipb = null;
	        while(it.hasNext())
	        {
	            ipb = (IInputParameterBinding)it.next();
	            if(ipb.getPosition()<= 0)
	            {
	                if(ipb.getName().equalsIgnoreCase(parameterBinding.getName()))
	                    return true;
	            }
	            else{
	                if(ipb.getPosition() == parameterBinding.getPosition())
	                    return true;
	            }
	        }
	        return false;
	    }

   	    /**
		 * 
		 * Create a new ParameterDefinition according to given
		 * IInputParameterBinding
		 * 
		 * @param iParamBind
		 * @return
		 */
		private ParameterDefinition createNewParameterDefinition(
				IInputParameterBinding iParamBind )
		{
		    ParameterDefinition pd = null;
		    if(iParamBind.getPosition()<0)
			    pd = new ParameterDefinition(iParamBind.getName(),DataType.UNKNOWN_TYPE,true,false);
			else
			    pd = new ParameterDefinition(iParamBind.getPosition(),DataType.UNKNOWN_TYPE,true,false);
		    return pd;
		}
		
		/**
		 * Detect whether an Parameter is defined in DataSet parameter lists. If
		 * that is true than return the index of that parameter in the list,
		 * otherwise return -1. The input parameter can be idenitify by either
		 * its position or its name. Once both of them provided, position holds
		 * higher priority on the identification of a parameter
		 * 
		 * @param parameterList
		 * @param position
		 * @param name
		 * @return
		 */
		private IParameterDefinition getParameterDefn(List parameterList, int position, String name )
		{
		    Object[] pds = parameterList.toArray(); 
		    for(int i = 0; i < pds.length; i++)
		    {
		    	IParameterDefinition paramDefn = (IParameterDefinition)pds[i];
		        if(position <= 0)
			    {
		            if(paramDefn.getName().equalsIgnoreCase(name))
		                return paramDefn;
			    }
			    else
			    {
			        if(paramDefn.getPosition()==position)
			        {
			            return paramDefn;
			        }
			    }
		    }
		    return null;
		}
		
		/**
		 * Set parameter default value, here Integer data type is to be
		 * processed especially. There is a strange problem that expression text
		 * of "1" will be evalued to "1.0". It's not sure that whether this
		 * problem is caused by present Rhino used of 1.6. To avoid failing
		 * convert in data base layer, it could be done here for this case.
		 * 
		 * @param paramDefn
		 * @param evaValue
		 */
		private ParamDefnAndValBinding getParamDefnAndValBinding( IParameterDefinition paramDefn,
				String evaValue )
		{
		    assert evaValue != null;

            if (paramDefn.getType() == DataType.INTEGER_TYPE)

            {
                String value = null;

                Integer integerValue = DataTypeUtil.toIntegerValue(evaValue);

                if (integerValue != null)
                    value = integerValue.toString();
                else
                    value = evaValue;
                return new ParamDefnAndValBinding( paramDefn,value );
            }
            else
            {
            	return new ParamDefnAndValBinding( paramDefn,evaValue );
            }
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
		 	
		    odiDSQuery.setParameterDefnAndValBindings( getMergedParameters(odaDataSet,  getQueryScope(),false ) );
		    
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
