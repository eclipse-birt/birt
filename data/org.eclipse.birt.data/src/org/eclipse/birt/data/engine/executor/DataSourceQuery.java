/*
 *************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.executor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSource.CacheConnection;
import org.eclipse.birt.data.engine.executor.QueryExecutionStrategyUtil.Strategy;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.executor.transform.SimpleResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.CancelManager;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.ICancellable;
import org.eclipse.birt.data.engine.impl.IQueryContextVisitor;
import org.eclipse.birt.data.engine.impl.QueryContextVisitorUtil;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odaconsumer.ColumnHint;
import org.eclipse.birt.data.engine.odaconsumer.ExceptionHandler;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.odaconsumer.PreparedStatement;
import org.eclipse.birt.data.engine.odaconsumer.QuerySpecHelper;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IParameterMetaData;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.basequery.CombinedQuery;

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
	
	ParameterBinding( String name, int position, Object value )
	{
		this.name = name;
		this.value = value;
		this.position = position;
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
public class DataSourceQuery extends BaseQuery implements IDataSourceQuery, IPreparedDSQuery
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
    
	private QuerySpecification querySpecificaton;
	
	// input parameter values
	private Collection inputParamValues;
	
	// Properties added by addProperty()
	private ArrayList propNames;
	private ArrayList propValues;
	
	private DataEngineSession session;
	
	private IQueryContextVisitor qcv;
	
	private static Logger logger = Logger.getLogger( DataSourceQuery.class.getName( ) );


	/**
	 * Constructor. 
	 * 
	 * @param dataSource
	 * @param queryType
	 * @param queryText
	 */
    DataSourceQuery( DataSource dataSource, String queryType, String queryText, DataEngineSession session, IQueryContextVisitor qcv )
    {
        this.dataSource = dataSource;
        this.queryText = queryText;
        this.queryType = queryType;
        this.session = session;
        this.qcv = qcv;
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
    @SuppressWarnings("restriction")
    public IPreparedDSQuery prepare() throws DataException
    {
    	long start = System.currentTimeMillis( );
        if ( odaStatement != null )
            throw new DataException( ResourceConstants.QUERY_HAS_PREPARED );

        // create and populate a query specification for preparing a statement
        populateQuerySpecification();
        
		if ( this.querySpecificaton != null
				&& this.querySpecificaton.getBaseQuery( ) instanceof CombinedQuery )
		{
			odaStatement = dataSource.prepareStatement( null,
					queryType,
					this.querySpecificaton );
		}
		else
		{
			odaStatement = dataSource.prepareStatement( queryText,
					queryType,
					this.querySpecificaton );
		}
        
        // Add custom properties to odaStatement
        addPropertiesToPreparedStatement( );
        
        // Adds input and output parameter hints to odaStatement.
        // This step must be done before odaStatement.setColumnsProjection()
        // for some jdbc driver need to carry out a query execution before the metadata can be achieved
        // and only when the Parameters are successfully set the query execution can succeed.
        addParameterDefns();
     
        //Here the "max rows" means the max number of rows that can fetch from data source.
      	odaStatement.setMaxRows( this.getRowFetchLimit( ) );
      		
        IOdaDataSetDesign design = null;
    	if( session.getDataSetCacheManager( ).getCurrentDataSetDesign( ) instanceof IOdaDataSetDesign )
    		design = (IOdaDataSetDesign)session.getDataSetCacheManager( ).getCurrentDataSetDesign( );
    	
    	ICancellable queryCanceller = new OdaQueryCanceller( odaStatement, dataSource, session.getStopSign(), this );
    	
        if ( design != null )
		{
			if ( canAccessResultSetByName( design ) )
			{
				// Ordering is important for the following operations. Column hints
				// should be defined
				// after custom fields are declared (since hints may be given to
				// those custom fields).
				// Column projection comes last because it needs hints and
				// custom
				// column information
				addCustomFields( design.getPrimaryResultSetName( ), odaStatement );
				addColumnHints( design.getPrimaryResultSetName( ), odaStatement );

				if ( this.projectedFields != null )
					odaStatement.setColumnsProjection( design.getPrimaryResultSetName( ), this.projectedFields );
			}
			else if( canAccessResultSetByNumber( design ) )
			{
				addCustomFields( design.getPrimaryResultSetNumber( ), odaStatement );
				addColumnHints( design.getPrimaryResultSetNumber( ), odaStatement );

				if ( this.projectedFields != null )
					odaStatement.setColumnsProjection( design.getPrimaryResultSetNumber( ), this.projectedFields );
			}
			else
			{
				this.session.getCancelManager( ).register( queryCanceller );
				if ( !session.getStopSign( ).isStopped( ) )
				{
					prepareColumns( );
				}
				this.session.getCancelManager( ).deregister( queryCanceller );
			}
		}else
		{
			this.session.getCancelManager( ).register( queryCanceller );
			if ( !session.getStopSign( ).isStopped( ) )
			{
				prepareColumns( );
			}
			this.session.getCancelManager( ).deregister( queryCanceller );
		}
        
		
		
        // If ODA can provide result metadata, get it now
        try
        {
        	
        	this.session.getCancelManager( ).register( queryCanceller );
        	
        	if( !session.getStopSign().isStopped() )
        		resultMetadata = getMetaData( (IOdaDataSetDesign)session.getDataSetCacheManager( ).getCurrentDataSetDesign( ), odaStatement );
        	if( design != null )
        	{
            	List modelResultHints = design.getResultSetHints( );
            	resultMetadata = mergeResultHint( modelResultHints , resultMetadata );        		
        	}

        	if ( queryCanceller.collectException( ) != null )
    		{
    			if ( !( queryCanceller.collectException( ).getCause( ) instanceof UnsupportedOperationException ) )
    				throw queryCanceller.collectException( );
    		}
    		
    		this.session.getCancelManager( ).deregister( queryCanceller );

        }
        catch ( DataException e )
        {
            // Assume metadata not available at prepare time; ignore the exception
        	resultMetadata = null;
        }
        logger.fine( "Prepare ODA Query uses:" + ( System.currentTimeMillis( ) - start) + " ms " );
        return this;
    }

	private boolean canAccessResultSetByName( IOdaDataSetDesign design )
			throws DataException
	{
		return design.getPrimaryResultSetName( ) != null && odaStatement.supportsNamedResults( );
	}

    private boolean canAccessResultSetByNumber( IOdaDataSetDesign design )
            throws DataException
    {
        return design.getPrimaryResultSetNumber( ) > 0 && odaStatement.supportsMultipleResultSets( );
    }
	
	private void prepareColumns( ) throws DataException
	{
		addCustomFields( odaStatement );
		addColumnHints( odaStatement );

		if ( this.projectedFields != null )
			odaStatement.setColumnsProjection( this.projectedFields );
	}

    /**
     * 
     * @param design
     * @param odaStatement
     * @return
     * @throws DataException
     */
    private IResultClass getMetaData( IOdaDataSetDesign design, PreparedStatement odaStatement ) throws DataException
    {
    	IResultClass result = null;
    	if ( design != null )
		{
			if ( canAccessResultSetByName( design ) )
			{
				try
				{
					result = odaStatement.getMetaData( design.getPrimaryResultSetName( ) );
				}
				catch ( DataException e )
				{
					throw new DataException( ResourceConstants.ERROR_HAPPEN_WHEN_RETRIEVE_RESULTSET,
							design.getPrimaryResultSetName( ) );
				}
				
			}
			else if ( canAccessResultSetByNumber( design ) )
			{
				try
				{
					result = odaStatement.getMetaData( design.getPrimaryResultSetNumber( ) );
				}
				catch ( DataException e )
				{
					throw new DataException( ResourceConstants.ERROR_HAPPEN_WHEN_RETRIEVE_RESULTSET,
							design.getPrimaryResultSetNumber( ) );
				}
			}
		}
		if( result == null )
			result = odaStatement.getMetaData();
		
		if ( design != null )
		{
			List hintList = design.getResultSetHints( );
			for ( int i = 0; i < hintList.size( ); i++ )
			{
				IColumnDefinition columnDefinition = (IColumnDefinition) hintList.get( i );
				for ( int j = 1; j <= result.getFieldCount( ); j++ )
				{
					ResultFieldMetadata resultFieldMetadata = result.getFieldMetaData( j );
					if ( columnDefinition.getColumnName( )
							.equals( resultFieldMetadata.getName( ) ) )
					{
						resultFieldMetadata.setAlias( columnDefinition.getAlias( ) );
						resultFieldMetadata.setAnalysisType( columnDefinition.getAnalysisType( ) );
						resultFieldMetadata.setAnalysisColumn( columnDefinition.getAnalysisColumn( ) );
						resultFieldMetadata.setIndexColumn( columnDefinition.isIndexColumn( ) );
						resultFieldMetadata.setCompressedColumn( columnDefinition.isCompressedColumn( ) );
						break;
					}
				}
			}
		}
		return result;
    }
    
    /*
     * Prepare a query specification with the property and input parameter values,
     * for use by an ODA driver before IQuery#prepare.
     */
    @SuppressWarnings("restriction")
    private QuerySpecification populateQuerySpecification() throws DataException
    {
		if ( this.querySpecificaton == null )
		{
			QuerySpecHelper querySpecHelper = new QuerySpecHelper( dataSource.getDriverName( ),
					queryType );
			this.querySpecificaton = querySpecHelper.getFactoryHelper( )
					.createQuerySpecification( );
		}
		// add custom properties
		addPropertiesToQuerySpec( querySpecificaton );

		// add parameter defns
		addParametersToQuerySpec( querySpecificaton );

		return querySpecificaton;
    }
    
    /** 
     * Adds custom properties to the QuerySpecification.
     */
    @SuppressWarnings("restriction")
    private void addPropertiesToQuerySpec( QuerySpecification querySpec )
    {
        if ( propNames == null )
            return;   // nothing to add
        
        assert propValues != null;        
        Iterator it_name = propNames.iterator();
        Iterator it_val = propValues.iterator();
        while ( it_name.hasNext() )
        {
            assert it_val.hasNext();
            String name = (String) it_name.next();
            String val = (String) it_val.next();
            querySpec.setProperty( name, val );
        }  
    }
    
    /** 
     * Adds custom properties to prepared oda statement;
     * use the same properties already set in querySpec before prepare  
     */
    @SuppressWarnings("restriction")
    private void addPropertiesToPreparedStatement( ) throws DataException
	{
        if( this.querySpecificaton == null || this.querySpecificaton.getProperties().isEmpty() )
            return;     // no properties to add
        
    	assert odaStatement != null;
    	Map<String,Object> propertyMap = this.querySpecificaton.getProperties();
    	Iterator<Entry<String, Object>> iter = propertyMap.entrySet().iterator();
    	while( iter.hasNext() )
    	{
    	    Entry<String, Object> property = iter.next();
    	    String value = ( property.getValue() == null ) ? null : property.getValue().toString();
            odaStatement.setProperty( property.getKey(), value );
    	}
	}
    
    /**
     * Adds input parameter values to the QuerySpecification.
     */
    @SuppressWarnings("restriction")
    private void addParametersToQuerySpec( QuerySpecification querySpec )
        throws DataException
    {
        if ( this.parameterHints == null )
            return; // nothing to add
        
        // iterate thru the collection to add parameter hints
        Iterator it = this.parameterHints.iterator( );
        while ( it.hasNext( ) )
        {
            ParameterHint parameterHint = (ParameterHint) it.next();
            
            //If the parameter is input parameter, add its value to query spec
            if ( parameterHint.isInputMode( ) )
            {
                Object inputValue = getParameterInputValue( parameterHint );
                
                QuerySpecHelper.setParameterValue( querySpec, parameterHint, inputValue );
            }           
        }
    }
    
	/** 
	 * Adds input and output parameter hints to odaStatement
	 */
	private void addParameterDefns() throws DataException
	{
		assert odaStatement!= null;
		
		if ( this.parameterHints == null )
		    return;	// nothing to add

		// iterate thru the collection to add parameter hints
		Iterator it = this.parameterHints.iterator( );
		while ( it.hasNext( ) )
		{
			ParameterHint parameterHint = (ParameterHint) it.next();
			odaStatement.addParameterHint( parameterHint );
			
			//If the parameter is input parameter then add it to input value list.
			if ( parameterHint.isInputMode( ) )
			{
                Object inputValue = getParameterInputValue( parameterHint );
				if ( parameterHint.getPosition( ) <= 0 || odaStatement.supportsNamedParameter( ))
				{
					this.setInputParamValue( parameterHint.getName( ), parameterHint.getPosition( ),
							inputValue );
					
				}
				else
				{
					this.setInputParamValue( parameterHint.getPosition( ),
							inputValue );
				}
			}			
		}
		this.setInputParameterBinding();
	}
    
    private Object getParameterInputValue( ParameterHint parameterHint )
        throws DataException
    {
        assert parameterHint.isInputMode( );
        Class paramHintDataType = parameterHint.getDataType();
        
        // since a Date may have extended types,
        // use the type of Date that is most effective for data conversion
        if( paramHintDataType == Date.class )
            paramHintDataType = parameterHint.getEffectiveDataType( 
                                    dataSource.getDriverName(), queryType );
        
        Object inputValue = parameterHint.getDefaultInputValue( );
        if ( inputValue != null )
        {
			if ( inputValue.getClass( ).isArray( )
					&& !( inputValue instanceof byte[] ) )
        	{
        		//if multi-value type report parameter is linked with data set parameter
        		//only take the first provided value to pass it to data set, except for byte[] Object
        		if ( Array.getLength( inputValue ) == 0 )
        		{
        			inputValue = null;
        		}
        		else
        		{
        			inputValue = Array.get( inputValue, 0 );
        		}
        	}
        }
        // neither IBlob nor IClob will be converted
        if ( paramHintDataType != IBlob.class && paramHintDataType != IClob.class )
            inputValue = convertToValue( inputValue,
                                        paramHintDataType );
        return inputValue;
    }
	
	/**
	 * @param inputParamName
	 * @param paramValue
	 * @throws DataException
	 */
	private void setInputParamValue( String inputParamName, int position, Object paramValue )
			throws DataException
	{

		ParameterBinding pb = new ParameterBinding( inputParamName, position, paramValue );
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
    
    private void addCustomFields( String rsetName, PreparedStatement stmt ) throws DataException
	{
    	if ( this.customFields != null )
    	{
    		Iterator it = this.customFields.iterator( );
    		while ( it.hasNext( ) )
    		{
    			CustomField customField = (CustomField) it.next( );
    			stmt.declareCustomColumn( rsetName, customField.getName( ),
    				DataType.getClass( customField.getDataType() ) );
    		}
    	}
	}
    
    private void addCustomFields( int rsetNumber, PreparedStatement stmt ) throws DataException
	{
    	if ( this.customFields != null )
    	{
    		Iterator it = this.customFields.iterator( );
    		while ( it.hasNext( ) )
    		{
    			CustomField customField = (CustomField) it.next( );
    			stmt.declareCustomColumn( rsetNumber, customField.getName( ),
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
    		ColumnHint colHint = prepareOdiHint( (IDataSourceQuery.ResultFieldHint) it.next() );
   			stmt.addColumnHint( colHint );
    	}
    	stmt.checkColumnsNaming();
	}

    private void addColumnHints( String rsetName, PreparedStatement stmt ) throws DataException
	{
    	assert stmt != null;
    	if ( resultHints == null || resultHints.size() == 0 )
    		return;
    	Iterator it = resultHints.iterator();
    	while ( it.hasNext())
    	{
    		ColumnHint colHint = prepareOdiHint( (IDataSourceQuery.ResultFieldHint) it.next() );
   			stmt.addColumnHint( rsetName, colHint );
    	}
    	stmt.checkColumnsNaming();
	}

    private void addColumnHints( int rsetNumber, PreparedStatement stmt ) throws DataException
	{
    	assert stmt != null;
    	if ( resultHints == null || resultHints.size() == 0 )
    		return;
    	Iterator it = resultHints.iterator();
    	while ( it.hasNext())
    	{
    		ColumnHint colHint = prepareOdiHint( (IDataSourceQuery.ResultFieldHint) it.next() );
   			stmt.addColumnHint( rsetNumber, colHint );
    	}
    	stmt.checkColumnsNaming();
	}

	private ColumnHint prepareOdiHint( IDataSourceQuery.ResultFieldHint odiHint )
	{
		ColumnHint colHint = new ColumnHint( odiHint.getName() );
		colHint.setAlias( odiHint.getAlias() );
		if ( odiHint.getDataType( ) == DataType.ANY_TYPE )
			colHint.setDataType( null );
		else
			colHint.setDataType( DataType.getClass( odiHint.getDataType( ) ) );  
		colHint.setNativeDataType( odiHint.getNativeDataType() );
		if ( odiHint.getPosition() > 0 )
			colHint.setPosition( odiHint.getPosition());
		return colHint;
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
	
	private IResultClass copyResultClass( IResultClass meta )
			throws DataException
	{
		List<ResultFieldMetadata> list = new ArrayList<ResultFieldMetadata>( );
		for ( int i = 1; i <= meta.getFieldCount( ); i++ )
		{
			if ( !meta.getFieldName( i ).equals( ExprMetaUtil.POS_NAME ) )
			{
				int m_driverPosition = meta.getFieldMetaData( i ).getDriverPosition( );
				String m_name = meta.getFieldMetaData( i ).getName( );
				String m_label = meta.getFieldMetaData( i ).getLabel( );
				Class m_dataType = meta.getFieldMetaData( i ).getDataType( );
				String m_nativeTypeName = meta.getFieldMetaData( i ).getNativeTypeName( );
				boolean m_isCustom = meta.getFieldMetaData( i ).isCustom( );
				Class m_driverProvidedDataType = meta.getFieldMetaData( i ).getDriverProvidedDataType( );
				int m_analysisType = meta.getFieldMetaData( i ).getAnalysisType( );
				String m_analysisColumn = meta.getFieldMetaData( i ).getAnalysisColumn( );
				boolean m_indexColumn = meta.getFieldMetaData( i ).isIndexColumn( );
				boolean m_compressedColumn = meta.getFieldMetaData( i ).isCompressedColumn( );
					
				ResultFieldMetadata metadata = new ResultFieldMetadata( m_driverPosition, m_name, 
						m_label, m_dataType,
						m_nativeTypeName,m_isCustom, m_analysisType, m_analysisColumn, m_indexColumn, m_compressedColumn );
				metadata.setDriverProvidedDataType( m_driverProvidedDataType );
				metadata.setAlias( meta.getFieldMetaData( i ).getAlias( ) );
				
				if( m_isCustom )
					metadata.setCustomPosition( meta.getFieldMetaData( i ).getCustomPosition( ) );
				
				list.add( metadata );
			}
		}
		IResultClass resultClass = new ResultClass( list );

		return resultClass;
	}
	
	
	private IResultClass mergeResultHint ( List modelResultHints, IResultClass meta )
	{
		if ( modelResultHints == null || modelResultHints.isEmpty( ) )
			return meta;
		IResultClass newResultClass;
		try
		{
			newResultClass = copyResultClass( meta );
		}
		catch ( Exception ex )
		{
			return meta;
		}
		boolean changed = false;
		int count = newResultClass.getFieldCount( );
		try
		{
			for ( int i = 1; i <= count; i++ )
			{
				String fieldName = newResultClass.getFieldName( i );
				Class odaType = newResultClass.getFieldMetaData( i )
						.getDataType( );
				for ( int j = 0; j < modelResultHints.size( ); j++ )
				{
					if ( ( (IColumnDefinition) modelResultHints.get( j ) ).getColumnName( )
							.equals( fieldName ) )
					{
						int apiType = ( (IColumnDefinition) modelResultHints.get( j ) ).getDataType( );
						if ( apiType > 0
								&& DataTypeUtil.toApiDataType( odaType ) != apiType )
						{
							newResultClass.getFieldMetaData( i )
									.setDataType( DataType.getClass( apiType ) );
							changed = true;
						}
						break;
					}
				}
			}
		}
		catch ( Exception ex )
		{
		}
		
		if( changed )
			return newResultClass;
		else
			return meta;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#execute()
	 */
    public IResultIterator execute( IEventHandler eventHandler )
			throws DataException
	{
    	assert odaStatement != null;
    	
    	IResultIterator ri = null;

    	this.setInputParameterBinding();
    	
    	IOdaDataSetDesign design = null;
    	if( session.getDataSetCacheManager( ).getCurrentDataSetDesign( ) instanceof IOdaDataSetDesign )
    		design = (IOdaDataSetDesign)session.getDataSetCacheManager( ).getCurrentDataSetDesign( );
    	

		if ( session.getDataSetCacheManager( ).doesSaveToCache( ) )
		{
			int fetchRowLimit = 0;
			if ( design != null )
			{
				fetchRowLimit = session.getDataSetCacheManager( )
						.getCurrentDataSetDesign( )
						.getRowFetchLimit( );
			}

			int cacheCountConfig = 0;
			if ( design.getFilters( ).isEmpty( ) )
			{
				cacheCountConfig = session.getDataSetCacheManager( )
						.getCacheCountConfig( );
			}
			
			if ( cacheCountConfig > 0 )
			{
				if ( fetchRowLimit != 0 && fetchRowLimit < cacheCountConfig )
				{

					odaStatement.setMaxRows( fetchRowLimit );
				}
				else
				{
					odaStatement.setMaxRows( cacheCountConfig );
				}
			}
			else
			{
				if ( fetchRowLimit != 0 )
				{
					odaStatement.setMaxRows( fetchRowLimit );
				}
			}
		}
		
    	ICancellable queryCanceller = new OdaQueryCanceller( odaStatement, dataSource, session.getStopSign(), this );
    	this.session.getCancelManager( ).register( queryCanceller );
    	
    	if( !session.getStopSign().isStopped())
    	{    
			long startTime = System.currentTimeMillis( );
    		odaStatement.execute( );
			long endTime = System.currentTimeMillis( );
			if( logger.isLoggable( Level.FINE ) )
				logger.log( Level.FINE, "ODA query execution time: " + 
					( endTime - startTime ) + " ms;\n   Executed query: " + odaStatement.getEffectiveQueryText() );
    	}
		
		QueryContextVisitorUtil.populateEffectiveQueryText( qcv,
				odaStatement.getEffectiveQueryText( ) );
		
		logger.fine( "Effective Query Text:" + odaStatement.getEffectiveQueryText( ) );
		if ( queryCanceller.collectException( ) != null )
		{
			if ( !( queryCanceller.collectException( ).getCause( ) instanceof UnsupportedOperationException ) )
				throw queryCanceller.collectException( );
		}
		
		
		ResultSet rs = null;
		
		if ( design != null )
		{
			if ( canAccessResultSetByName( design ) )
			{
				try
				{

					rs = odaStatement.getResultSet( design.getPrimaryResultSetName( ) );
				}
				catch ( DataException e )
				{
					throw new DataException( ResourceConstants.ERROR_HAPPEN_WHEN_RETRIEVE_RESULTSET,
							design.getPrimaryResultSetName( ) );
				}
			}
			else if ( canAccessResultSetByNumber( design ) )
			{
				try
				{
					rs = odaStatement.getResultSet( design.getPrimaryResultSetNumber( ) );
				}
				catch ( DataException e )
				{
					throw new DataException( ResourceConstants.ERROR_HAPPEN_WHEN_RETRIEVE_RESULTSET,
							design.getPrimaryResultSetNumber( ) );
				}
			}
		}
		if( rs == null && !session.getStopSign( ).isStopped( ) )
		{
			rs = odaStatement.getResultSet( );
		}
		
		// If we did not get a result set metadata at prepare() time, get it now
		if ( resultMetadata == null )
		{
			List modelResultHints = design.getResultSetHints( );
			resultMetadata = rs.getMetaData( );
			if ( resultMetadata == null )
				throw new DataException( ResourceConstants.METADATA_NOT_AVAILABLE );
			resultMetadata = mergeResultHint( modelResultHints , resultMetadata );
		}
		
		// Initialize CachedResultSet using the ODA result set
		if ( session.getDataSetCacheManager( ).doesSaveToCache( ) == false )
		{
			if ( ( ( session.getEngineContext( ).getMode( ) == DataEngineContext.DIRECT_PRESENTATION || session.getEngineContext( )
					.getMode( ) == DataEngineContext.MODE_GENERATION ) )
					&& this.getQueryDefinition( ) instanceof IQueryDefinition )
			{
				IQueryDefinition queryDefn = (IQueryDefinition) this.getQueryDefinition( );
				
				Strategy strategy = QueryExecutionStrategyUtil.getQueryExecutionStrategy( this.session, queryDefn,
						queryDefn.getDataSetName( ) == null
						? null
						: ( (DataEngineImpl) this.session.getEngine( ) ).getDataSetDesign( queryDefn.getDataSetName( ) ) );
				if ( strategy  != Strategy.Complex )
				{
					SimpleResultSet simpleResult = new SimpleResultSet( this,
							rs,
							resultMetadata,
							eventHandler,
							this.getGrouping( ),
							this.session,
							strategy == Strategy.SimpleLookingFoward);
					
					return simpleResult.getResultSetIterator( );
				}
			}
	    	
			ri = new CachedResultSet( this,
					resultMetadata,
					rs,
					eventHandler,
					session );
		}
		else
			ri = new CachedResultSet( this,
					resultMetadata,
					new DataSetToCache( rs, resultMetadata, session ),
					eventHandler, session );
		
		if ( ri != null )
			( (CachedResultSet) ri ).setOdaResultSet( rs );

		return ri;
    }
    
	private static class OdaQueryCanceller implements ICancellable
    {
    	private PreparedStatement statement;
    	private DataSource dataSource;
    	private StopSign stop;
    	private DataException exception;
    	private DataSourceQuery dsQuery;
    	
    	OdaQueryCanceller( PreparedStatement statement, DataSource dataSource, StopSign stop, DataSourceQuery dsQuery )
    	{
    		this.statement = statement;
    		this.stop = stop;
    		this.dataSource = dataSource;
    		this.dsQuery = dsQuery;
    	}
    	
		/**
		 * Collect the exception throw during statement execution.
		 * 
		 * @return
		 */
		public DataException collectException()
		{
			return this.exception;
		}


		/**
		 * 
		 */
		public void cancel( )
		{
			try
			{
				CancelManager manager = this.dsQuery.session.getCancelManager( );
				if( manager != null )
					manager.deregister( this );
					
				this.statement.cancel( );
			}
			catch ( Exception e )
			{
				try
				{
					this.statement.close( );
				}
				catch ( Exception e1 )
				{
					this.exception = new DataException( e.getLocalizedMessage( ), e );
				}
			}
			
			try
			{
				CacheConnection conn = this.dataSource.getAvailableConnection();
				if( conn!= null )
					conn.close();
				
			}
			catch( Exception e )
			{
				//Ignore.
			}
		}

		/**
		 * 
		 */
		public boolean doCancel( )
		{
			return this.stop.isStopped( );
		}
    }
    
    /**
     *  set input parameter bindings
     */
    private void setInputParameterBinding() throws DataException
    {
    	assert odaStatement!= null;
    	
    	//		 set input parameter bindings
		Iterator inputParamValueslist = getInputParamValues().iterator( );
		while ( inputParamValueslist.hasNext( ) )
		{
			ParameterBinding paramBind = (ParameterBinding) inputParamValueslist.next( );
			if ( paramBind.getPosition( ) <= 0 || odaStatement.supportsNamedParameter( ))
			{
				try
				{
					odaStatement.setParameterValue( paramBind.getName( ),
							paramBind.getValue( ) );
				}
				catch ( DataException e )
				{
					if ( paramBind.getPosition( ) <= 0 )
					{
						throw e;
					}
					else
					{
						odaStatement.setParameterValue( paramBind.getPosition( ),
								paramBind.getValue( ) );
					}
				}
			}
			else
			{
				odaStatement.setParameterValue( paramBind.getPosition( ),
						paramBind.getValue() );
			}
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
	 * In ODA layer, it references the index positions of input
	 * parameters and output parameters in a single sequential list. 
	 * However, in ODI layer, we need to clearly distinguish them
	 * since only retrieving output parameter is supported and it should be based
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
			throw new DataException( ResourceConstants.INVALID_OUTPUT_PARAMETER_INDEX, Integer.valueOf( index ) );
		
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
			throw new DataException( ResourceConstants.OUTPUT_PARAMETER_OUT_OF_BOUND,
					Integer.valueOf( index ) );

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
     * convert the String value to Object according to its data type.
     *  
     * @param inputValue
     * @param type
     * @return
     * @throws DataException
     */
    private static Object convertToValue( Object inputValue, Class typeClass )
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

	public void setQuerySpecification( QuerySpecification spec )
	{
		this.querySpecificaton = spec;		
	}
	
	public DataEngineSession getSession( )
	{
		return this.session;
	}
    
}
