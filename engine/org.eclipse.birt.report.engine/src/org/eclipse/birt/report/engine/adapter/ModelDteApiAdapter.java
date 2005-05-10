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

package org.eclipse.birt.report.engine.adapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;


/**
 * A singleton adapter class that creates data engine API interface objects 
 * from the model.api objects for data set and data source definition.
 */
public class ModelDteApiAdapter
{
    private static ModelDteApiAdapter sm_instance;

    public static ModelDteApiAdapter getInstance()
	{
        if ( sm_instance == null )
            sm_instance = new ModelDteApiAdapter();
		return sm_instance;
	}
	
	private ModelDteApiAdapter()
    {
    }
    
    public IBaseDataSourceDesign createDataSourceDesign( DataSourceHandle dataSource ) 
    	throws EngineException
    {
        if ( dataSource instanceof OdaDataSourceHandle )
            return newExtendedDataSource( (OdaDataSourceHandle) dataSource );
        
        if ( dataSource instanceof ScriptDataSourceHandle )
            return newScriptDataSource( (ScriptDataSourceHandle) dataSource );

        // any other types are not supported
        assert false;
        return null;
    }
    
    public IBaseDataSetDesign createDataSetDesign( DataSetHandle dataSet ) 
    	throws EngineException
    {
        if ( dataSet instanceof OdaDataSetHandle )
            return newExtendedDataSet( (OdaDataSetHandle) dataSet );       
        
        if ( dataSet instanceof ScriptDataSetHandle )
            return newScriptDataSet( (ScriptDataSetHandle) dataSet );

        // any other types are not supported
        assert false;
        return null;
    }
    
    IOdaDataSourceDesign newExtendedDataSource( OdaDataSourceHandle source ) throws EngineException
    {
        OdaDataSourceDesign dteSource = new OdaDataSourceDesign( source.getName() );
        
        // Adapt base class properties
        adaptBaseDataSource( source, dteSource );
        
        // Adapt extended data source elements
        
        // validate that a required attribute is specified
        String driverName = source.getDriverName();
        if ( driverName == null || driverName.length() == 0 )
        {
            throw new EngineException( "Missing driverName in data source definition, " + source.getName() ); //$NON-NLS-1$
        }
        dteSource.setDriverName( driverName );

        // static ROM properties defined by the ODA driver extension
        // of the odaDriverModel extension point
        Map staticProps = getExtensionProperties( source, 
			                	source.getExtensionName(), 
			                	source.getExtensionPropertyDefinitionList() );
	    if ( staticProps != null && ! staticProps.isEmpty() )
	    {	    
	        Iterator propNamesItr = staticProps.keySet().iterator();
	        while ( propNamesItr.hasNext() )
	        {
	            Object propName = propNamesItr.next();
	            assert ( propName != null && propName instanceof String );
	        
	            String propValue = (String) staticProps.get( propName );	        
                try
                {	
                    dteSource.addPublicProperty( (String) propName, propValue );
                }
                catch ( BirtException e )
                {
                    throw new EngineException( e.getMessage() );
                }
	        }
	    }

        // private driver properties / private runtime data
        Iterator elmtIter = source.privateDriverPropertiesIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                ExtendedPropertyHandle modelProp = (ExtendedPropertyHandle) elmtIter.next();
                try
                {
                    dteSource.addPrivateProperty( modelProp.getName(), modelProp.getValue() );
                }
                catch ( BirtException e )
                {
                    throw new EngineException( e.getMessage() );
                }
            }
        }
        
        return dteSource;
    }
    
    IScriptDataSourceDesign newScriptDataSource( ScriptDataSourceHandle source )
    {
        ScriptDataSourceDesign dteSource = new ScriptDataSourceDesign( source.getName() );
        
        // Adapt base class properties
        adaptBaseDataSource( source, dteSource );
        
        // Adapt script data source elements
        dteSource.setOpenScript( source.getOpen() );
        dteSource.setCloseScript( source.getClose() );
        return dteSource;
    }

    void adaptBaseDataSource( DataSourceHandle source,
            BaseDataSourceDesign dest )
    {
        dest.setBeforeOpenScript( source.getBeforeOpen() );
        dest.setAfterOpenScript( source.getAfterOpen() );
        dest.setBeforeCloseScript( source.getBeforeClose() );
        dest.setAfterCloseScript( source.getAfterClose() );
    }
        
    IOdaDataSetDesign newExtendedDataSet( OdaDataSetHandle modelDataSet ) throws EngineException
    {
        OdaDataSetDesign dteDataSet = new OdaDataSetDesign( modelDataSet.getName() );
            
        // Adapt base class properties
        adaptBaseDataSet( modelDataSet, dteDataSet );
            
        // Adapt extended data set elements
            
        // static query text and dynamic query script
        dteDataSet.setQueryText( modelDataSet.getQueryText() );
        dteDataSet.setQueryScript( modelDataSet.getQueryScript() );         
        
        // type of extended data set
        dteDataSet.setDataSetType( modelDataSet.getType() );
        
        // result set name
        dteDataSet.setPrimaryResultSetName( modelDataSet.getResultSetName() );
        
        // static ROM properties defined by the ODA driver extension
        // of the odaDriverModel extension point
        Map staticProps = getExtensionProperties( modelDataSet, 
				                modelDataSet.getExtensionName(), 
				                modelDataSet.getExtensionPropertyDefinitionList() );
	    if ( staticProps != null && ! staticProps.isEmpty() )
	    {	    
	        Iterator propNamesItr = staticProps.keySet().iterator();
	        while ( propNamesItr.hasNext() )
	        {
	            Object propName = propNamesItr.next();
	            assert ( propName != null && propName instanceof String );	        
	            String propValue = (String) staticProps.get( propName );	        
                dteDataSet.addPublicProperty( (String) propName, propValue );
	        }
	    }

        // private driver properties / private runtime data
        Iterator elmtIter = modelDataSet.privateDriverPropertiesIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                ExtendedPropertyHandle modelProp = (ExtendedPropertyHandle) elmtIter.next();
                dteDataSet.addPrivateProperty( modelProp.getName(), modelProp.getValue() );
            }
        }

        return dteDataSet;
    }
    
    IScriptDataSetDesign newScriptDataSet( ScriptDataSetHandle modelDataSet ) throws EngineException
    {
        ScriptDataSetDesign dteDataSet = new ScriptDataSetDesign( modelDataSet.getName() );
                
        // Adapt base class properties
        adaptBaseDataSet( modelDataSet, dteDataSet );
        
        // Adapt script data set elements
        dteDataSet.setOpenScript( modelDataSet.getOpen() );
        dteDataSet.setFetchScript( modelDataSet.getFetch() );
        dteDataSet.setCloseScript( modelDataSet.getClose() );
        dteDataSet.setDescribeScript( modelDataSet.getDescribe() );
        
        return dteDataSet;
    }
    
    void adaptBaseDataSet( DataSetHandle modelDataSet,
            				BaseDataSetDesign dteDataSet ) throws EngineException
    {
        dteDataSet.setDataSource( modelDataSet.getDataSourceName() );
        dteDataSet.setBeforeOpenScript( modelDataSet.getBeforeOpen() );
        dteDataSet.setAfterOpenScript( modelDataSet.getAfterOpen() );
        dteDataSet.setOnFetchScript( modelDataSet.getOnFetch() );
        dteDataSet.setBeforeCloseScript( modelDataSet.getBeforeClose() );
        dteDataSet.setAfterCloseScript( modelDataSet.getAfterClose() );

        // dataset parameters definition
        HashMap paramBindingCandidates = new HashMap();
        
        Iterator elmtIter = modelDataSet.parametersIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
            	DataSetParameterHandle modelParam = (DataSetParameterHandle ) elmtIter.next();
				dteDataSet.addParameter( newParam( modelParam ) );

				// collect input parameter default values as
				// potential parameter binding if no explicit ones are
				// defined for a parameter
				if ( modelParam.isInput( ) )
				{
				    String defaultValueExpr = modelParam.getDefaultValue( );
				    if ( defaultValueExpr != null )
				        paramBindingCandidates.put( modelParam.getName(), defaultValueExpr );
				}
            }
        }

        // input parameter bindings
        elmtIter = modelDataSet.paramBindingsIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                ParamBindingHandle modelParamBinding = (ParamBindingHandle) elmtIter.next();
                // replace default value of the same parameter, if defined
		        paramBindingCandidates.put( modelParamBinding.getParamName(), modelParamBinding.getExpression() );
            }
        }
        
        // assign merged parameter bindings to the data set
        if ( paramBindingCandidates.size() > 0 )
        {
            elmtIter = paramBindingCandidates.keySet().iterator();
            while ( elmtIter.hasNext() )
            {
                Object paramName = elmtIter.next();
                assert( paramName != null && paramName instanceof String );
                String expression = (String) paramBindingCandidates.get( paramName );
				dteDataSet.addInputParamBinding( 
				        newInputParamBinding( (String) paramName, expression ) );
            }
        }
        
        // computed columns
        elmtIter = modelDataSet.computedColumnsIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                ComputedColumnHandle modelCmptdColumn = 
                    (ComputedColumnHandle) elmtIter.next();
                IComputedColumn dteCmptdColumn = newComputedColumn( modelCmptdColumn );
                dteDataSet.addComputedColumn( dteCmptdColumn );
            }
        }

        // filter conditions
        elmtIter = modelDataSet.filtersIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                FilterConditionHandle modelFilter = (FilterConditionHandle) elmtIter.next();
                IFilterDefinition dteFilter = newFilter( modelFilter );
                dteDataSet.addFilter( dteFilter );
            }
        }

        // merging result set column and column hints into DtE columnDefn;
        // first create new columnDefn based on model's column hints
 
        elmtIter = modelDataSet.columnHintsIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                ColumnHintHandle modelColumnHint = (ColumnHintHandle) elmtIter.next();
                dteDataSet.addResultSetHint( newColumnDefn( modelColumnHint ) );
            }
        }

        // now merge model's result set column info into existing columnDefn 
        // with same column name, otherwise create new columnDefn
        // based on the model's result set column
        List columnDefns = dteDataSet.getResultSetHints();
        elmtIter = modelDataSet.resultSetIterator();
        if ( elmtIter != null )
        {
            while ( elmtIter.hasNext() )
            {
                ResultSetColumnHandle modelColumn = (ResultSetColumnHandle) elmtIter.next();
                ColumnDefinition existDefn = findColumnDefn( columnDefns, modelColumn.getColumnName() );
                if ( existDefn != null )
                    updateColumnDefn( existDefn, modelColumn );
                else
                    dteDataSet.addResultSetHint( newColumnDefn( modelColumn ) );
            }
        }

    }
    
    /**
     * Creates a new DtE API IParameterDefinition from a model's DataSetParameterHandle.
     */
    IParameterDefinition newParam( DataSetParameterHandle modelParam )
    {
        ParameterDefinition dteParam = new ParameterDefinition();

        dteParam.setName( modelParam.getName() );
        if ( modelParam.getPosition() != null )
            dteParam.setPosition( modelParam.getPosition().intValue() );
        dteParam.setType( toDteDataType( modelParam.getDataType() ) );
        dteParam.setInputMode( modelParam.isInput() );
        dteParam.setOutputMode( modelParam.isOutput() );
        dteParam.setNullable( modelParam.allowNull() );
        dteParam.setInputOptional( modelParam.isOptional() );
        dteParam.setDefaultInputValue( modelParam.getDefaultValue() );
        
        return dteParam;
    }

    /** Creates a new DtE API InputParamBinding from a model's binding.
     * Could return null if no expression is bound.
     */
    IInputParameterBinding newInputParamBinding( ParamBindingHandle modelInputParamBndg )
    {
		// model provides binding by name only
		return newInputParamBinding( modelInputParamBndg.getParamName( ),
									 modelInputParamBndg.getExpression( ) );
    }
    
    private IInputParameterBinding newInputParamBinding( String paramName, String paramValue )
    {
        if ( paramValue == null )
			return null; // no expression is bound
    	ScriptExpression paramValueExpr = new ScriptExpression( paramValue );
		return new InputParameterBinding( paramName, paramValueExpr );
    }
    
    /** Creates a new DtE API Computed Column from a model computed column.
     * Could return null if no expression is defined.
     * @throws EngineException
     */
    IComputedColumn newComputedColumn( ComputedColumnHandle modelCmptdColumn ) throws EngineException
    {
        // no expression to define a computed column        
        if ( modelCmptdColumn.getExpression() == null ) {
        	throw new EngineException( MessageConstants.MISSING_COMPUTED_COLUMN_EXPRESSION_EXCEPTION,
					modelCmptdColumn.getColumnName( ) );
        }
        
        return new ComputedColumn( modelCmptdColumn.getColumnName(), 
                					modelCmptdColumn.getExpression() );
    }
    
    /** Creates a new DtE API IJSExprFilter or IColumnFilter from 
     * a model's filter condition.
     * Could return null if no expression nor column operator is defined.
     */
    IFilterDefinition newFilter( FilterConditionHandle modelFilter )
    {
        String filterExpr = modelFilter.getExpr();
        if ( filterExpr == null || filterExpr.length() == 0 )
               return null; 	// no filter defined

        // converts to DtE exprFilter if there is no operator
        String filterOpr = modelFilter.getOperator();
        if ( filterOpr == null || filterOpr.length() == 0 )
            return new FilterDefinition( new ScriptExpression( filterExpr ) );
        
        /* has operator defined, try to convert filter condition
         * to operator/operand style column filter with 0 to 2 operands
         */

        String column = filterExpr;
        int dteOpr = toDteFilterOperator( filterOpr );
        String operand1 = modelFilter.getValue1();
        String operand2 = modelFilter.getValue2();
        return new FilterDefinition( new ConditionalExpression( 
        			column, dteOpr, operand1, operand2 ));
    }
    
    IColumnDefinition newColumnDefn( ResultSetColumnHandle modelColumn )
    {
        ColumnDefinition newColumn = new ColumnDefinition( modelColumn.getColumnName() );
        updateColumnDefn( newColumn, modelColumn );
        return newColumn;
    }
        
    void updateColumnDefn( ColumnDefinition dteColumn, ResultSetColumnHandle modelColumn )
    {
        assert dteColumn.getColumnName().equals( modelColumn.getColumnName() );
        if ( modelColumn.getPosition() != null )
            dteColumn.setColumnPosition( modelColumn.getPosition().intValue() );
        dteColumn.setDataType( toDteDataType( modelColumn.getDataType() ) );
    }
    
    IColumnDefinition newColumnDefn( ColumnHintHandle modelColumnHint )
    {
        ColumnDefinition newColumn = new ColumnDefinition( modelColumnHint.getColumnName() );
        newColumn.setAlias( modelColumnHint.getAlias() );
        
        String exportConstant = modelColumnHint.getExport();
        if ( exportConstant != null )
        {
            int exportHint = IColumnDefinition.DONOT_EXPORT;	// default value
            if ( exportConstant.equals( DesignChoiceConstants.EXPORT_TYPE_IF_REALIZED ) )
                exportHint = IColumnDefinition.EXPORT_IF_REALIZED;
            else if ( exportConstant.equals( DesignChoiceConstants.EXPORT_TYPE_ALWAYS ) )
                exportHint = IColumnDefinition.ALWAYS_EXPORT;
            else 
                assert exportConstant.equals( DesignChoiceConstants.EXPORT_TYPE_NONE );

            newColumn.setExportHint( exportHint );
        }

        String searchConstant = modelColumnHint.getSearching();
        if ( searchConstant != null )
        {
        	int searchHint = IColumnDefinition.NOT_SEARCHABLE;
        	if ( searchConstant.equals( DesignChoiceConstants.SEARCH_TYPE_INDEXED ) )
        	    searchHint = IColumnDefinition.SEARCHABLE_IF_INDEXED;
        	else if ( searchConstant.equals( DesignChoiceConstants.SEARCH_TYPE_ANY ) )
        	    searchHint = IColumnDefinition.ALWAYS_SEARCHABLE;
        	else
        	    assert searchConstant.equals( DesignChoiceConstants.SEARCH_TYPE_NONE );

        	newColumn.setSearchHint( searchHint );
        }

        return newColumn;
    }

    /** Find the DtE columnDefn from the given list of columnDefns
     * that matches the given columnName.
     */
    private ColumnDefinition findColumnDefn( List columnDefns, String columnName )
    {
        assert columnName != null;
        if ( columnDefns == null )
            return null;	// no list to find from
        Iterator iter = columnDefns.iterator();
        if ( iter == null )
            return null;
        
        // iterate thru each columnDefn, and looks for a match of
        // specified column name
        while( iter.hasNext() )
        {
            ColumnDefinition column = (ColumnDefinition) iter.next();
            if ( columnName.equals( column.getColumnName() ) )
                return column;
        }
        return null;
    }
    
    int toDteDataType( String modelDataType )
    {
        if ( modelDataType == null )
            return DataType.UNKNOWN_TYPE;
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY ) )
            return DataType.ANY_TYPE;
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER ) )
            return DataType.INTEGER_TYPE;
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING ) )
            return DataType.STRING_TYPE;
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME ) )
            return DataType.DATE_TYPE;
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL ) )
            return DataType.DECIMAL_TYPE;
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT ) )
            return DataType.DOUBLE_TYPE;

        // types that are not yet supported, model should have checked
        if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRUCTURE ) ||
             modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TABLE ) )
        {
            assert false;
        }
        return DataType.UNKNOWN_TYPE;
    }
    
    // Convert model operator value to DtE IColumnFilter enum value
    int toDteFilterOperator( String modelOpr )
    {
        if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_EQ ) )
	        return IConditionalExpression.OP_EQ;
        if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NE ) )
            return IConditionalExpression.OP_NE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LT ) )
		    return IConditionalExpression.OP_LT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LE ) )
		    return IConditionalExpression.OP_LE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_GE ) )
		    return IConditionalExpression.OP_GE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_GT ) )
		    return IConditionalExpression.OP_GT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) )
		    return IConditionalExpression.OP_BETWEEN;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) )
		    return IConditionalExpression.OP_NOT_BETWEEN;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NULL ) )
		    return IConditionalExpression.OP_NULL;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL ) )
		    return IConditionalExpression.OP_NOT_NULL;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TRUE ) )
		    return IConditionalExpression.OP_TRUE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_FALSE ) )
		    return IConditionalExpression.OP_FALSE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LIKE ) )
		    return IConditionalExpression.OP_LIKE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_N ) )
		    return IConditionalExpression.OP_TOP_N;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N ) )
		    return IConditionalExpression.OP_BOTTOM_N;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT ) )
		    return IConditionalExpression.OP_TOP_PERCENT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT ) )
		    return IConditionalExpression.OP_BOTTOM_PERCENT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_ANY ) )
		    return IConditionalExpression.OP_ANY;
		
		assert false;	// unknown filter operator
		return IConditionalExpression.OP_NONE;
    }

    /* Gets the data handle's static ROM extension properties 
     * name and value pairs in String values and returns them in a Map
     */
    private Map getExtensionProperties( ReportElementHandle dataHandle, 
            		String extensionName, List driverPropList )
    {
        if ( driverPropList == null || driverPropList.isEmpty() )
            return null;		// nothing to add

		Map properties = new HashMap();
        Iterator elmtIter =  driverPropList.iterator();
		while ( elmtIter.hasNext() )
		{
		    IPropertyDefn modelExtProp = (IPropertyDefn) elmtIter.next();
		
		    // first strips extension name prefix and separator from 
		    // property's fully qualified name
		    String propName = stripPropNamePrefix( modelExtProp.getName(),
		        					extensionName );
		    assert( propName != null && propName.length() > 0 );
		
		    // use fully qualified name to get property value
		    Object propValueObj = dataHandle.getProperty( modelExtProp.getName() );                
		    String propValue = ( propValueObj == null ) ?
		        					null : propValueObj.toString();
		    properties.put( propName, propValue );
		}
		return properties;
    }
    
    /* Strips the given prefix and separator characters
     * and returns the remaining string.
     */
    private String stripPropNamePrefix( String fromString, String prefix )
    {
        if ( fromString == null || fromString.length() == 0 )
            return fromString;	// nothing to strip from
        
        if ( prefix == null || prefix.length() == 0 )
            return fromString;	// nothing to strip
        
        if ( ! fromString.startsWith( prefix ) )
            return fromString;	// already stripped
        
        // verify that given fromString has characters beyond
        // the prefix separator
        String separator = "::";
        int prefixLen = prefix.length();
        int prefixSeprLen = prefixLen + separator.length();
        assert( fromString.length() > prefixSeprLen &&
                separator.equals( fromString.substring( prefixLen, prefixSeprLen ) ));
        
        return fromString.substring( prefixSeprLen );
    }
}
