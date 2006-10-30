/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.dscache.DataSetResultCache;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odaconsumer.ColumnHint;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.odaconsumer.PreparedStatement;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IParameterMetaData;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 *	Structure to hold info of a custom field. 
 */
final class CustomField
{
    String 	name;
    int dataType = -1;
    
    CustomField( String name, int dataType)
    {
        this.name = name;
        this.dataType = dataType;
    }
    
    CustomField()
    {}
    
    public int getDataType()
    {
        return dataType;
    }
    
    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
}

/**
 * Structure to hold Parameter binding info
 * @author lzhu
 *
 */
class ParameterBinding
{
	private String name;
	private int position = -1;
	private Object value;
	
	ParameterBinding( String name, Object value )
	{
		this.name = name;
		this.value = value;
	}
	
	ParameterBinding( int position, Object value )
	{
		this.position = position;
		this.value = value;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public String getName()
	{
		return name;
	}

	public Object getValue()
	{
		return value;
	}
}

/**
 * Implementation of ODI's IDataSourceQuery interface
 */
class DataSourceQuery extends BaseQuery implements IDataSourceQuery, IPreparedDSQuery
{
    protected DataSource 		dataSource;
    protected String			queryText;
    protected String			queryType;
    protected PreparedStatement	odaStatement;
    
    // Collection of ColumnHint objects
    protected Collection		resultHints;
    
    // Collection of CustomField objects
    protected Collection		customFields;
    
    protected IResultClass		resultMetadata;
    
    // Names (or aliases) of columns in the projected result set
    protected String[]			projectedFields;
	
	// input/output parameter hints (collection of ParameterHint objects)
	private Collection parameterHints;
    
	// input parameter values
	private Collection inputParamValues;
	
	// Properties added by addProperty()
	private ArrayList propNames;
	private ArrayList propValues;
	
	private DataEngineSession session;
	/**
	 * Constructor. 
	 * 
	 * @param dataSource
	 * @param queryType
	 * @param queryText
	 */
    DataSourceQuery( DataSource dataSource, String queryType, String queryText, DataEngineSession session )
    {
        this.dataSource = dataSource;
        this.queryText = queryText;
        this.queryType = queryType;
        this.session = session;
    }

    /*
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultHints(java.util.Collection)
     */
    public void setResultHints(Collection columnDefns)
    {
        resultHints = columnDefns;
    }

    /*
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultProjection(java.lang.String[])
     */
    public void setResultProjection(String[] fieldNames) throws DataException
    {
        if ( fieldNames == null || fieldNames.length == 0 )
            return;		// nothing to set
        this.projectedFields = fieldNames;
    }
    
	public void setParameterHints( Collection parameterHints )
	{
        // assign to placeholder, for use later during prepare()
		this.parameterHints = parameterHints;
	}

    /*
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#addProperty(java.lang.String, java.lang.String)
     */
    public void addProperty(String name, String value ) throws DataException
    {
    	if ( name == null )
    		throw new NullPointerException("Property name is null");
    	
    	// Must be called before prepare() per interface spec
        if ( odaStatement != null )
            throw new DataException( ResourceConstants.QUERY_HAS_PREPARED );
    	
   		if ( propNames == null )
   		{
   			assert propValues == null;
   			propNames = new ArrayList();
   			propValues = new ArrayList();
   		}
   		assert propValues != null;
   		propNames.add( name );
   		propValues.add( value );
    }

    /*
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#declareCustomField(java.lang.String, int)
     */
    public void declareCustomField( String fieldName, int dataType ) throws DataException
    {
        if ( fieldName == null || fieldName.length() == 0 )
            throw new DataException( ResourceConstants.CUSTOM_FIELD_EMPTY );
        
        if ( customFields == null )
        {
            customFields = new ArrayList();
        }
        else
        {
        	Iterator cfIt = customFields.iterator( );
			while ( cfIt.hasNext( ) )
			{
				CustomField cf = (CustomField) cfIt.next();
				if ( cf.name.equals( fieldName ) )
				{
					throw new DataException( ResourceConstants.DUP_CUSTOM_FIELD_NAME, fieldName );
				}
			}
        }
        
        customFields.add( new CustomField( fieldName, dataType ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#prepare()
     */
    public IPreparedDSQuery prepare() throws DataException
    {
        if ( odaStatement != null )
            throw new DataException( ResourceConstants.QUERY_HAS_PREPARED );

        odaStatement = dataSource.prepareStatement( queryText, queryType );
        
        // Add custom properties
        addProperties();
        
        // Add parameter defns. This step must be done before odaStatement.setColumnsProjection()
        // for some jdbc driver need to carry out a query execution before the metadata can be achieved
        // and only when the Parameters are successfully set the query execution can succeed.
        addParameterDefns();
        // Ordering is important for the following operations. Column hints should be defined
        // after custom fields are declared (since hints may be given to those custom fields).
        // Column projection comes last because it needs hints and custom column information
        addCustomFields( odaStatement );
        addColumnHints( odaStatement );
        
		odaStatement.setColumnsProjection( this.projectedFields );

		//Here the "max rows" means the max number of rows that can fetch from data source.
		odaStatement.setMaxRows( this.getRowFetchLimit( ) );
		
        // If ODA can provide result metadata, get it now
        try
        {
            resultMetadata = odaStatement.getMetaData();
        }
        catch ( DataException e )
        {
            // Assume metadata not available at prepare time; ignore the exception
        	resultMetadata = null;
        }
        
        return this;
    }

    /** 
     * Adds custom properties to oda statement being prepared 
     */
    private void addProperties() throws DataException
	{
    	assert odaStatement != null;
    	if ( propNames != null )
    	{
    		assert propValues != null;
    		
    		Iterator it_name = propNames.iterator();
    		Iterator it_val = propValues.iterator();
    		while ( it_name.hasNext())
    		{
    			assert it_val.hasNext();
    			String name = (String) it_name.next();
    			String val = (String) it_val.next();
    			odaStatement.setProperty( name, val );
    		}
    	}
	}
      
	/** 
	 * Adds input and output parameter hints to odaStatement
	 */
	private void addParameterDefns() throws DataException
	{
		if ( this.parameterHints == null )
		    return;	// nothing to add

		// iterate thru the collection to add parameter hints
		Iterator it = this.parameterHints.iterator( );
		while ( it.hasNext( ) )
		{
			ParameterHint parameterHint = (ParameterHint) it.next();
			odaStatement.addParameterHint( parameterHint );
			
			//If the parameter is input parameter then add it to input value list.
			if ( parameterHint.isInputMode( )
					&& parameterHint.getDefaultInputValue( ) != null )
			{
                Class paramHintDataType = parameterHint.getDataType();
                
                // since a Date may have extended types,
                // use the type of Date that is most effective for data conversion
                if( paramHintDataType == Date.class )
                    paramHintDataType = parameterHint.getEffectiveDataType( 
                                            dataSource.getDriverName(), queryType );
                
				Object inputValue = convertToValue( 
						parameterHint.getDefaultInputValue( ), 
                        paramHintDataType );
				if ( isParameterPositionValid(parameterHint.getPosition( )) )
					this.setInputParamValue( parameterHint.getPosition( ),
							inputValue );
				else
					this.setInputParamValue( parameterHint.getName( ),
							inputValue );
			}			
		}
		this.setInputParameterBinding();
	}
	
	/**
	 * Check whether the given parameter position is valid.
	 * 
	 * @param parameterPosition
	 * @return
	 */
	private boolean isParameterPositionValid(int parameterPosition)
	{
		return parameterPosition > 0;
	}
	
	/**
	 * @param inputParamName
	 * @param paramValue
	 * @throws DataException
	 */
	private void setInputParamValue( String inputParamName, Object paramValue )
			throws DataException
	{

		ParameterBinding pb = new ParameterBinding( inputParamName, paramValue );
		getInputParamValues().add( pb );
	}

	/**
	 * @param inputParamPos
	 * @param paramValue
	 * @throws DataException
	 */
	private void setInputParamValue( int inputParamPos, Object paramValue )
			throws DataException
	{
		ParameterBinding pb = new ParameterBinding( inputParamPos, paramValue );
		getInputParamValues().add( pb );
	}
	
	/**
	 * Declares custom fields on Oda statement
	 * 
	 * @param stmt
	 * @throws DataException
	 */
    private void addCustomFields( PreparedStatement stmt ) throws DataException
	{
    	if ( this.customFields != null )
    	{
    		Iterator it = this.customFields.iterator( );
    		while ( it.hasNext( ) )
    		{
    			CustomField customField = (CustomField) it.next( );
    			stmt.declareCustomColumn( customField.getName( ),
    				DataType.getClass( customField.getDataType() ) );
    		}
    	}
	}
    
    /**
     * Adds Odi column hints to ODA statement
     *  
     * @param stmt
     * @throws DataException
     */
    private void addColumnHints( PreparedStatement stmt ) throws DataException
	{
    	assert stmt != null;
    	if ( resultHints == null || resultHints.size() == 0 )
    		return;
    	Iterator it = resultHints.iterator();
    	while ( it.hasNext())
    	{
    		IDataSourceQuery.ResultFieldHint odiHint = 
    				(IDataSourceQuery.ResultFieldHint) it.next();
    		ColumnHint colHint = new ColumnHint( odiHint.getName() );
    		colHint.setAlias( odiHint.getAlias() );
    		if ( odiHint.getDataType( ) == DataType.ANY_TYPE )
				colHint.setDataType( null );
			else
				colHint.setDataType( DataType.getClass( odiHint.getDataType( ) ) );  
            colHint.setNativeDataType( odiHint.getNativeDataType() );
			if ( odiHint.getPosition() > 0 )
    			colHint.setPosition( odiHint.getPosition());

   			stmt.addColumnHint( colHint );
    	}
	}
       
	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getResultClass()
	 */
    public IResultClass getResultClass() 
    {
        // Note the return value can be null if resultMetadata was 
        // not available during prepare() time
        return resultMetadata;
    }

    /*
     * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getParameterMetaData()
     */
    public Collection getParameterMetaData()
			throws DataException
	{
        if ( odaStatement == null )
			throw new DataException( ResourceConstants.QUERY_HAS_NOT_PREPARED );
        
        Collection odaParamsInfo = odaStatement.getParameterMetaData();
        if ( odaParamsInfo == null || odaParamsInfo.isEmpty() )
            return null;
        
        // iterates thru the most up-to-date collection, and
        // wraps each of the odaconsumer parameter metadata object
        ArrayList paramMetaDataList = new ArrayList( odaParamsInfo.size() );
        Iterator odaParamMDIter = odaParamsInfo.iterator();
        while ( odaParamMDIter.hasNext() )
        {
            org.eclipse.birt.data.engine.odaconsumer.ParameterMetaData odaMetaData = 
                (org.eclipse.birt.data.engine.odaconsumer.ParameterMetaData) odaParamMDIter.next();
            paramMetaDataList.add( new ParameterMetaData( odaMetaData ) );
        }
        return paramMetaDataList;
	}
    
    /**
     * Return the input parameter value list
     * 
     * @return
     */
	private Collection getInputParamValues()
	{
	    if ( inputParamValues == null )
	        inputParamValues = new ArrayList();
	    return inputParamValues;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#execute()
	 */
    public IResultIterator execute( IEventHandler eventHandler )
			throws DataException
	{
    	assert odaStatement != null;

    	this.setInputParameterBinding();
		// Execute the prepared statement
		if ( !odaStatement.execute( ) )
			throw new DataException( ResourceConstants.NO_RESULT_SET );
		ResultSet rs = odaStatement.getResultSet();
		
		// If we did not get a result set metadata at prepare() time, get it now
		if ( resultMetadata == null )
		{
			resultMetadata = rs.getMetaData();
            if ( resultMetadata == null )
    			throw new DataException(ResourceConstants.METADATA_NOT_AVAILABLE);
		}
		
		// Initialize CachedResultSet using the ODA result set
		if ( session.getDataSetCacheManager( ).doesSaveToCache( ) == false )
		{
			if ( !hasOutputParams( ) )
				return new CachedResultSet( this,
						resultMetadata,
						rs,
						eventHandler, session );
			else
			{
				IDataSetPopulator populator = new OdaResultSet( rs );
				return new CachedResultSet( this,
						resultMetadata,
						populator,
						eventHandler, session );
			}
		}
		else
			return new CachedResultSet( this,
					resultMetadata,
					new DataSetResultCache( rs, resultMetadata, session ),
					eventHandler, session );
    }
   
    /**
     * whether there is output paramters in data set design
     * @return
     * @throws DataException
     */
    private boolean hasOutputParams( )
	{
		boolean hasOutputParam = false;

		if ( this.parameterHints != null )
		{
			Iterator it = this.parameterHints.iterator( );
			while ( it.hasNext( ) )
			{
				ParameterHint hint = (ParameterHint) it.next( );

				if ( hint.isOutputMode( ) )
				{
					hasOutputParam = true;
					break;
				}
			}			
		}
		
		return hasOutputParam;
	}
    
    /**
     *  set input parameter bindings
     */
    private void setInputParameterBinding() throws DataException{
		//		 set input parameter bindings
		Iterator inputParamValueslist = getInputParamValues().iterator( );
		while ( inputParamValueslist.hasNext( ) )
		{
			ParameterBinding paramBind = (ParameterBinding) inputParamValueslist.next( );
			if ( isParameterPositionValid(paramBind.getPosition( )) )
				odaStatement.setParameterValue( paramBind.getPosition( ),
						paramBind.getValue() );
			else
				odaStatement.setParameterValue( paramBind.getName( ),
						paramBind.getValue() );
		}
    }
    
	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getParameterValue(int)
	 */
	public Object getOutputParameterValue( int index ) throws DataException
	{
		assert odaStatement != null;
		
		int newIndex = getCorrectParamIndex( index );
		return odaStatement.getParameterValue( newIndex );
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getParameterValue(java.lang.String)
	 */
	public Object getOutputParameterValue( String name ) throws DataException
	{
		assert odaStatement != null;
				
		checkOutputParamNameValid( name );
		return odaStatement.getParameterValue( name );
	}
    
	/**
	 * In oda layer, it does not differentiate the value retrievation of input
	 * parameter value and ouput parameter value. They will be put in a same
	 * sequence list. However, in odi layer, we need to clearly distinguish them
	 * since only retrieving output parameter is suppored and it should be based
	 * on its own output parameter index. Therefore, this method will do such a
	 * conversion from the output parameter index to the parameter index.
	 * 
	 * @param index based on output parameter order
	 * @return index based on the whole parameters order
	 * @throws DataException
	 */
	private int getCorrectParamIndex( int index ) throws DataException
	{
		if ( index <= 0 )
			throw new DataException( ResourceConstants.INVALID_OUTPUT_PARAMETER_INDEX, new Integer(index) );
		
		int newIndex = 0; // 1-based
		int curOutputIndex = 0; // 1-based
		
		Collection collection = getParameterMetaData( );
		if ( collection != null )
		{
			Iterator it = collection.iterator( );
			while ( it.hasNext( ) )
			{
				newIndex++;
				
				IParameterMetaData metaData = (IParameterMetaData) it.next( );
				if ( metaData.isOutputMode( ).booleanValue( ) == true )
				{
					curOutputIndex++;
					
					if ( curOutputIndex == index )
						break;
				}
			}
		}

		if ( curOutputIndex < index )
			throw new DataException( ResourceConstants.OUTPUT_PARAMETER_OUT_OF_BOUND,new Integer(index));

		return newIndex;
	}
	
	/**
	 * Validate the name of output parameter
	 * 
	 * @param name
	 * @throws DataException
	 */
	private void checkOutputParamNameValid( String name ) throws DataException
	{
		assert name != null;

		boolean isValid = false;

		Collection collection = getParameterMetaData( );
		if ( collection != null )
		{
			Iterator it = collection.iterator( );
			while ( it.hasNext( ) )
			{
				IParameterMetaData metaData = (IParameterMetaData) it.next( );

				String paramName = metaData.getName( );
				if ( paramName.equals( name ) )
				{
					isValid = metaData.isOutputMode( ).booleanValue( );
					break;
				}
			}
		}

		if ( isValid == false )
			throw new DataException( ResourceConstants.INVALID_OUTPUT_PARAMETER_NAME, name );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
	 */
    public void close()
    {
        if ( odaStatement != null )
        {
        	this.dataSource.closeStatement( odaStatement );
	        odaStatement = null;
        }
        
        this.dataSource = null;
        // TODO: close all CachedResultSets created by us
    }
    
    /**
     * convert the String value to Object according to it's datatype.
     *  
     * @param inputValue
     * @param type
     * @return
     * @throws DataException
     */
    private static Object convertToValue( String inputValue, Class typeClass )
			throws DataException
	{
		try
		{
			return DataTypeUtil.convert( inputValue, typeClass);
		}
		catch ( Exception ex )
		{
			throw new DataException( ResourceConstants.CANNOT_CONVERT_PARAMETER_TYPE,
					ex,
					new Object[]{
							inputValue, typeClass
					} );
		}
	}
    
}
