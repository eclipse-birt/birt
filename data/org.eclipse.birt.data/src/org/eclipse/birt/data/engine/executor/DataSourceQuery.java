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
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.birt.data.engine.api.DataType;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterDefinition;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.ColumnHint;
import org.eclipse.birt.data.engine.odaconsumer.InputParameterHint;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odaconsumer.PreparedStatement;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.oda.OdaException;

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

class ParameterBinding{
	private String name;
	private int position=-1;
	private String value;
	
	ParameterBinding(String name,String value){
		this.name=name;
		this.value=value;
	}
	
	ParameterBinding(int position,String value){
		this.position=position;
		this.value=value;
	}
	
	public int getPosition(){
		return position;
	}
	
	public String getName(){
		return name;
	}

	public String getValue(){
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
    
	// Collection of Input Parameters
	private Collection inputParams = new ArrayList( );
	
	private Collection inputParamHints;

    DataSourceQuery( DataSource dataSource, String queryType, String queryText )
    {
        this.dataSource = dataSource;
        this.queryText = queryText;
        this.queryType = queryType;
    }
    
    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getDataSource()
     */
    public IDataSource getDataSource()
    {
        return dataSource;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getQueryText()
     */
    public String getQueryText()
    {
        return queryText;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getQueryType()
     */
    public String getQueryType()
    {
        return queryType;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultHints(java.util.Collection)
     */
    public void setResultHints(Collection columnDefns)
    {
        resultHints = columnDefns;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getResultHints()
     */
    public Collection getResultHints()
    {
        return resultHints;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultProjection(java.lang.String[])
     */
    public void setResultProjection(String[] fieldNames) throws DataException
    {
        if ( fieldNames == null || fieldNames.length == 0 )
            return;		// nothing to set
        this.projectedFields = fieldNames;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getResultProjection()
     */
    public String[] getResultProjection()
    {
    	return projectedFields;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setInputParamHints(java.util.Collection)
     */
    public void setInputParamHints(Collection parameterDefns)
    {
        if ( parameterDefns == null || parameterDefns.isEmpty( ) )
			return; // nothing to set
		inputParamHints=parameterDefns;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getInputParamHints()
     */
    public Collection getInputParamHints()
    {
        return inputParamHints;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setOutputParamHints(java.util.Collection)
     */
    public void setOutputParamHints(Collection parameterDefns)
    {
        if ( parameterDefns == null || parameterDefns.isEmpty() )
            return;		// nothing to set
        
        // TODO implement this
        assert false;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#getOutputParamHints()
     */
    public Collection getOutputParamHints()
    {
        // TODO implement this
        assert false;
        return null;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#addProperty(java.lang.String, java.lang.String)
     */
    public void addProperty(String name, String value)
    {
        // TODO 
        // How to implement this? We need ODA consumer to allow us to set properties on the Statement
        // before it is prepared
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#declareCustomField(java.lang.String, int)
     */
    public void declareCustomField(String fieldName, int dataType) throws DataException
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
					throw new DataException( ResourceConstants.CUSTOM_FIELD_DUPLICATED );
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

        
        try
        {
            odaStatement = dataSource.getConnection().prepareStatement( queryText, queryType );
            
            // Ordering is important for the following operations. Column hints should be defined
            // after custom fields are declared (since hints may be given to those custom fields).
            // Column projection comes last because it needs hints and custom column information
            addCustomFields( odaStatement );
            addColumnHints( odaStatement );
            odaStatement.setColumnsProjection( this.projectedFields );
        }
        catch ( OdaException e )
        {
            odaStatement = null;
            throw new DataException( ResourceConstants.DS_COLUMN_HINTS_ERROR, e );
        }

        // If ODA can provide result metadata, get it now
        try
        {
            resultMetadata = odaStatement.getMetaData();
        }
        catch ( OdaException e )
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
    		try
			{
    			stmt.addColumnHint( colHint );
			}
    		catch (OdaException e )
			{
    			throw new DataException( ResourceConstants.DS_COLUMN_HINTS_ERROR,
						e );
			}
    	}
	}
    
    // Declares custom fields on Oda statement
    private void addCustomFields( PreparedStatement stmt ) throws OdaException
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
   
    public IResultClass getResultClass() 
    {
        // Note the return value can be null if resultMetadata was 
        // not available during prepare() time
        return resultMetadata;
    }

	public void setInputParamValue( String inputParamName, Object paramValue )
			throws DataException
	{

		ParameterBinding pb = new ParameterBinding( inputParamName,
				 paramValue.toString( )  );
		inputParams.add( pb );
	}

	public void setInputParamValue( int inputParamPos, Object paramValue )
			throws DataException
	{
		ParameterBinding pb = new ParameterBinding( inputParamPos,
				 paramValue.toString( )  );
		inputParams.add( pb );
	}
    
    public IResultIterator execute( ) throws DataException
    {
    	assert odaStatement != null;
		
		try
		{
			Iterator list;
			if ( inputParamHints != null )
			{
				list = inputParamHints.iterator( );
				while ( list.hasNext( ) )
				{
					InputParameterDefinition paramDef = (InputParameterDefinition) list.next( );
					InputParameterHint parameterHint = new InputParameterHint(
							paramDef.getName( ) );
					parameterHint.setPosition( paramDef.getPosition( ) );
					parameterHint.setIsOptional( paramDef.isOptional( ) );
					odaStatement.addInputParameterHint( parameterHint );
				}
			}
			list = inputParams.iterator( );
			while ( list.hasNext( ) )
			{
				ParameterBinding paramBind = (ParameterBinding) list.next( );
				if ( paramBind.getPosition( ) != -1 )
					odaStatement.setParameterValue( paramBind.getPosition( ),
							paramBind.getValue() );
				else
					odaStatement.setParameterValue( paramBind.getName( ),
							paramBind.getValue() );
			}
			
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
		
		catch ( OdaException e )
		{
		    throw new DataException( ResourceConstants.DS_PARAMETER_ERROR, e );
		}
		
		
    }
    
    public void close()
    {
        if ( odaStatement != null )
        {
	        try
	        {
	            odaStatement.close();
	        }
	        catch ( OdaException e )
	        {
	            // TODO log exception
	            e.printStackTrace();
	        }
	        odaStatement = null;
        }
        
        // TODO: close all CachedResultSets created by us
    }

}
