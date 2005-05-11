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
import java.util.Iterator;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.ColumnHint;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.odaconsumer.PreparedStatement;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

// Structure to hold definition of a custom column
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
	
	// input/output parameter hints
	private Collection paramHints;
    
	// input parameter values
	private Collection inputParamValues;
	
	// Properties added by addProperty()
	private ArrayList propNames;
	private ArrayList propValues;

    DataSourceQuery( DataSource dataSource, String queryType, String queryText )
    {
        this.dataSource = dataSource;
        this.queryText = queryText;
        this.queryType = queryType;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getDataSource()
     */
    public IDataSource getDataSource()
    {
        return dataSource;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getQueryText()
     */
    public String getQueryText()
    {
        return queryText;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getQueryType()
     */
    public String getQueryType()
    {
        return queryType;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultHints(java.util.Collection)
     */
    public void setResultHints(Collection columnDefns)
    {
        resultHints = columnDefns;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getResultHints()
     */
    public Collection getResultHints()
    {
        return resultHints;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultProjection(java.lang.String[])
     */
    public void setResultProjection(String[] fieldNames) throws DataException
    {
        if ( fieldNames == null || fieldNames.length == 0 )
            return;		// nothing to set
        this.projectedFields = fieldNames;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getResultProjection()
     */
    public String[] getResultProjection()
    {
    	return projectedFields;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setParameterHints()
     */
	public void setParameterHints( Collection parameterDefns )
	{
        if ( parameterDefns == null || parameterDefns.isEmpty() )
			return; 	// nothing to set
        
        // assign to placeholder, for use later during prepare()
		paramHints = parameterDefns;
	}

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getParameterHints()
     */
	public Collection getParameterHints()
	{
	    return paramHints;
	}

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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
    
    /**
     * Gets the customer columns as a Collection of CustomColumnDefn objects.
     *
     */
    public Collection getCustomFields() 
    {
        return customFields;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#prepare()
     */
    public IPreparedDSQuery prepare() throws DataException
    {
        if ( odaStatement != null )
            throw new DataException( ResourceConstants.QUERY_HAS_PREPARED );

        odaStatement = dataSource.getConnection().prepareStatement( queryText, queryType );
        
        // Add custom properties
        addProperties();
        
        // Add parameter hints. This step must be done before odaStatement.setColumnsProjection()
        // for some jdbc driver need to carry out a query execution before the metadata can be achieved
        // and only when the Parameters are successfully set the query execution can succeed.
        addParameterHints();
        // Ordering is important for the following operations. Column hints should be defined
        // after custom fields are declared (since hints may be given to those custom fields).
        // Column projection comes last because it needs hints and custom column information
        addCustomFields( odaStatement );
        addColumnHints( odaStatement );
        odaStatement.setColumnsProjection( this.projectedFields );

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
    
    // Adds Odi column hints to ODA statement 
    private void addColumnHints( PreparedStatement stmt ) throws DataException
	{
    	assert stmt != null;
    	if ( resultHints == null || resultHints.size() == 0 )
    		return;
    	Iterator it = resultHints.iterator();
    	while ( it.hasNext())
    	{
    		IDataSourceQuery.ResultFieldHint hint = 
    				(IDataSourceQuery.ResultFieldHint) it.next();
    		ColumnHint colHint = new ColumnHint( hint.getName() );
    		colHint.setAlias( hint.getAlias() );
    		colHint.setDataType( DataType.getClass(hint.getDataType()));
    		if ( hint.getPosition() > 0 )
    			colHint.setPosition( hint.getPosition());

   			stmt.addColumnHint( colHint );
    	}
	}
    
    // Declares custom fields on Oda statement
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
    
    /** Adds custom properties to oda statement being prepared */
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
	private void addParameterHints() throws DataException
	{
		if ( paramHints == null )
		    return;	// nothing to add

		// iterate thru the collection to add parameter hints
		Iterator list = paramHints.iterator( );
		while ( list.hasNext( ) )
		{
		    IParameterDefinition paramDef = (IParameterDefinition) list.next( );
		    ParameterHint parameterHint = new ParameterHint( paramDef.getName(), 
															 paramDef.isInputMode(),
															 paramDef.isOutputMode() );
			parameterHint.setPosition( paramDef.getPosition( ) );
			parameterHint.setDataType( DataType.getClass( paramDef.getType() ));
			parameterHint.setIsInputOptional( paramDef.isInputOptional( ) );
			parameterHint.setDefaultInputValue( paramDef.getDefaultInputValue() );
			parameterHint.setIsNullable( paramDef.isNullable() );
			odaStatement.addParameterHint( parameterHint );
			
			//If the parameter is input parameter then add it to input value list.
			if(parameterHint.isInputMode())
			{
				if(parameterHint.getDefaultInputValue() == null)
					throw new DataException(ResourceConstants.DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL);
				this.setInputParamValue(parameterHint.getName(),parameterHint.getDefaultInputValue());
			}
		}
		this.setInputParameterBinding();
	}
	
    public IResultClass getResultClass() 
    {
        // Note the return value can be null if resultMetadata was 
        // not available during prepare() time
        return resultMetadata;
    }

    /* (non-Javadoc)
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
    
	private Collection getInputParamValues()
	{
	    if ( inputParamValues == null )
	        inputParamValues = new ArrayList();
	    return inputParamValues;
	}

	public void setInputParamValue( String inputParamName, Object paramValue )
			throws DataException
	{

		ParameterBinding pb = new ParameterBinding( inputParamName, paramValue );
		getInputParamValues().add( pb );
	}

	public void setInputParamValue( int inputParamPos, Object paramValue )
			throws DataException
	{
		ParameterBinding pb = new ParameterBinding( inputParamPos, paramValue );
		getInputParamValues().add( pb );
	}
    
    public IResultIterator execute( ) throws DataException
    {
    	assert odaStatement != null;

    	this.setInputParameterBinding();
		// Execute the prepared statement
		if ( ! odaStatement.execute() )
			throw new DataException(ResourceConstants.NO_RESULT_SET);
		ResultSet rs = odaStatement.getResultSet();
		
		// If we did not get a result set metadata at prepare() time, get it now
		if ( resultMetadata == null )
		{
			resultMetadata = rs.getMetaData();
            if ( resultMetadata == null )
    			throw new DataException(ResourceConstants.METADATA_NOT_AVAILABLE);
		}
		
		// Initialize CachedResultSet using the ODA result set
		return new CachedResultSet( this, resultMetadata, rs );
    }
    
    public void close()
    {
        if ( odaStatement != null )
        {
	        try
	        {
	            odaStatement.close();
	        }
	        catch ( DataException e )
	        {
	            // TODO log exception
	            e.printStackTrace();
	        }
	        odaStatement = null;
        }
        
        // TODO: close all CachedResultSets created by us
    }
    // set input parameter bindings
    private void setInputParameterBinding() throws DataException{
		//		 set input parameter bindings
		Iterator inputParamValueslist = getInputParamValues().iterator( );
		while ( inputParamValueslist.hasNext( ) )
		{
			ParameterBinding paramBind = (ParameterBinding) inputParamValueslist.next( );
			if ( paramBind.getPosition( ) != -1 )
				odaStatement.setParameterValue( paramBind.getPosition( ),
						paramBind.getValue() );
			else
				odaStatement.setParameterValue( paramBind.getName( ),
						paramBind.getValue() );
		}
    }

}
