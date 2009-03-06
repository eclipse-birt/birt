/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb;

import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl.Connection;
import org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl.DBProfileStatement;
import org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl.Driver;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.modelbase.sql.datatypes.CharacterStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.ExactNumericDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.NumericalDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.PredefinedDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.PrimitiveType;
import org.eclipse.datatools.modelbase.sql.query.QueryStatement;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionVariable;
import org.eclipse.datatools.modelbase.sql.query.helper.DataTypeHelper;
import org.eclipse.datatools.modelbase.sql.query.helper.StatementHelper;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * An internal utility class to process the metadata of a SQL query statement.
 */
public class SQLQueryUtility
{

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static final String PROP_PRIVATE_PARAMETERMETADATA = DBProfileStatement.PROP_PRIVATE_PARAMETERMETADATA;
	private static final String CONST_PARAMS_DELIMITER = DBProfileStatement.CONST_PARAMS_DELIMITER;
	private static final String CONST_PARAM_NAME_DELIMITER = DBProfileStatement.CONST_PARAM_NAME_DELIMITER;


    static void updateDataSetDesign( DataSetDesign dataSetDesign, 
            final QueryStatement queryStmt, IConnectionProfile connProfile, String dataSetName )
    {
    	if ( dataSetName != null )
    		dataSetDesign.setName( dataSetName );
        // initialize
        dataSetDesign.setQueryText( EMPTY_STRING );
        
        // obtain query's current runtime metadata, and maps it to the dataSetDesign
        boolean wasProfileConnected = ( connProfile.getConnectionState() == IConnectionProfile.CONNECTED_STATE );
        IConnection customConn = null;
        try
        {
            // instantiate the custom ODA runtime driver class
            IDriver customDriver = new Driver();
            
            // use runtime driver to obtain and open a live connection
            customConn = customDriver.getConnection( null );
            assert( customConn instanceof Connection );
            ((Connection) customConn).open( connProfile );
            
            // update the data set design with the 
            // query's current runtime metadata
            updateQueryMetaData( dataSetDesign, customConn, queryStmt );
        }
        catch( OdaException e )
        {
            e.printStackTrace();

            // queryText should have been updated in data set design; if not, get it from queryStmt
            String queryText = dataSetDesign.getQueryText();
            if( queryText == null || queryText.length() == 0 )
                updateQueryText( dataSetDesign, queryStmt );

            // not able to get current metadata, reset previous derived metadata
            dataSetDesign.setResultSets( null );
            dataSetDesign.setParameters( null );
        }
        finally
        {
            // if connection was not already open prior to this method, cleanup and close it
            if( ! wasProfileConnected ) 
                closeConnection( customConn );
        }
    }

    /**
     * Set system help context
     * 
     * @param control
     * @param contextId
     */
	public static void setSystemHelp( Control control, String contextId )
	{
		PlatformUI.getWorkbench( )
				.getHelpSystem( )
				.setHelp( control, contextId );
	}
	
   /**
     * Updates specified design with raw SQL query text ready for prepare by a JDBC driver; 
     * any extra stuff in QueryStatement are stripped, such as comments are preserved in designer state
     * @param dataSetDesign
     * @param queryStmt
     */
    private static void updateQueryText( DataSetDesign dataSetDesign,
                               final QueryStatement queryStmt )
    {
        dataSetDesign.setQueryText( getPreparableSQL( queryStmt ) );
    }
    
    /**
     * Updates the specified dataSetDesign with its query's runtime metadata,
     * obtained from the ODA runtime connection.
     */
    private static void updateQueryMetaData( DataSetDesign dataSetDesign,
                               IConnection conn, final QueryStatement queryStmt )
        throws OdaException
    {
        updateQueryText( dataSetDesign, queryStmt );

        String queryText = dataSetDesign.getQueryText();
        IQuery query = conn.newQuery( null );
        query.prepare( queryText );     // syntax error could cause exception thrown
                
        try
        {
            IResultSetMetaData md = query.getMetaData();
            updateResultSetDesign( md, dataSetDesign );
        }
        catch( OdaException e )
        {
            // no result set definition available, reset previous derived metadata
            dataSetDesign.setResultSets( null );
            e.printStackTrace();
        }
        
        // proceed to get parameter design definition;
        // update parameter definition with metadata obtained from query statement model
        updateParameterDesign( dataSetDesign, queryStmt );
    }

    /**
     * Returns the SQL query text ready for prepare by a JDBC driver.
     * @param queryStmt
     * @return
     */
    static String getPreparableSQL( final QueryStatement queryStmt )
    {
        if( queryStmt == null )
            return null;
        QueryStatement queryStmtCopy = copyQueryStatement( queryStmt );
        convertNamedVariablesToMarkers( queryStmtCopy );

        // strip out comments
        String queryText = StatementHelper.getSQLWithoutComments( queryStmtCopy );
        return queryText;
    }

    private static QueryStatement copyQueryStatement( final QueryStatement queryStmt )
    {
        QueryStatement queryStmtCopy = (QueryStatement) EcoreUtil.copy( queryStmt );
        
        // non EObject are not yet copied; make a shallow copy of top-level source info
        queryStmtCopy.setSourceInfo( queryStmt.getSourceInfo());

        return queryStmtCopy;
    }

    /**
     * Convert any named parameter variables to a '?' parameter marker
     * @param queryStmt a query statement; its contents may get modified by this method
     */
    private static void convertNamedVariablesToMarkers( QueryStatement queryStmt )
    {
        // get all the parameters defined in query
        List paramVars = StatementHelper.getAllVariablesInQueryStatement( queryStmt, false, null ); 
        if( paramVars.isEmpty() )
            return;     // done
        
        Iterator paramVarsIter = paramVars.iterator(); 
        while( paramVarsIter.hasNext() ) 
        { 
            ValueExpressionVariable var = (ValueExpressionVariable) paramVarsIter.next();
            
            // setting the variable's name field to null turns it into a parameter marker-type variable
            if( var.getName() != null )
                var.setName( null ); 
        } 
    }
    
    /**
     * Indicates whether the two specified SQL fragments are syntactically equivalent.
     */
    static boolean isEquivalentSQL( String sql1, String sql2 )
    {
        if( sql1 == null || sql2 == null )
        {
            return( sql1 == sql2 );
        }
        // compareSQL does not handle null arguments
        return ( StatementHelper.compareSQL( sql1, sql2 ) == 0 );
    }

    /**
     * Updates the specified data set design's result set definition based on the
     * specified runtime metadata.
     * @param md    runtime result set metadata instance
     * @param dataSetDesign     data set design instance to update
     * @throws OdaException
     */
    private static void updateResultSetDesign( IResultSetMetaData md,
            DataSetDesign dataSetDesign ) 
        throws OdaException
    {
        ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign( md );

        ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
                .createResultSetDefinition();
        // resultSetDefn.setName( value );  // result set name
        resultSetDefn.setResultSetColumns( columns );

        // no exception in conversion; go ahead and assign to specified dataSetDesign
        dataSetDesign.setPrimaryResultSet( resultSetDefn );
        dataSetDesign.getResultSets().setDerivedMetaData( true );
    }
    
    private static void updateParameterDesign( DataSetDesign dataSetDesign, final QueryStatement queryStmt )
    {
        if( dataSetDesign == null || queryStmt == null )
            return;
        
        // get all parameters, both in named variable or position markers, if exist;
        // the list returned are sorted by their lexical order;
        // do not convert parameter marker(s) to named variables
        List paramVars = StatementHelper.getAllVariablesInQueryStatement( queryStmt, false ); 
        if( paramVars.isEmpty() )
        {
            dataSetDesign.setParameters( null );
            if ( dataSetDesign.getPrivateProperties( ) != null )
			{
				dataSetDesign.getPrivateProperties( )
						.setProperty( PROP_PRIVATE_PARAMETERMETADATA,
								EMPTY_STRING );
			}
            return;
        }
 
        DataSetParameters dataSetParams =
            DesignFactory.eINSTANCE.createDataSetParameters();
        dataSetDesign.setParameters( dataSetParams );        

        // iterate thru each parameter variable model and extract its metadata to corresponding ODA parameter design
        Iterator paramVarsIter = paramVars.iterator(); 
        int index = 0;
		StringBuffer paramMdPropBuf = new StringBuffer( );

        while ( paramVarsIter.hasNext( ) )
		{
			ValueExpressionVariable var = (ValueExpressionVariable) paramVarsIter.next( );
			index++;

			ParameterDefinition paramDefn = DesignFactory.eINSTANCE.createParameterDefinition( );

			paramDefn.setInOutMode( ParameterMode.IN_LITERAL );

			DataElementAttributes paramAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );
			paramDefn.setAttributes( paramAttrs );

			paramAttrs.setPosition( index );
			paramAttrs.setName( var.getName( ) );

			convertToDataElementAttributes( var, paramAttrs );

			adjustParameterDefinition( paramDefn );

			dataSetParams.getParameterDefinitions( ).add( paramDefn );
			
			// append parameter metadata info for private property value
			if ( paramAttrs.getName() != null && paramAttrs.getName().trim( ).length( ) > 0 )
			{
				if ( paramMdPropBuf.length() > 0 )
					paramMdPropBuf.append( CONST_PARAMS_DELIMITER );
				paramMdPropBuf.append( paramAttrs.getPosition() + CONST_PARAM_NAME_DELIMITER + paramAttrs.getName() );
			}
		}
        
        // set private property to data set design
		if ( dataSetDesign.getPrivateProperties( ) == null )
		{
		    // create a private property element in design if none already exists
			try
			{
				Properties props = new Properties( );
				props.setProperty( PROP_PRIVATE_PARAMETERMETADATA, EMPTY_STRING );
				dataSetDesign.setPrivateProperties( 
				        DesignSessionUtil.createDataSetNonPublicProperties( dataSetDesign.getOdaExtensionDataSourceId( ),
                        						dataSetDesign.getOdaExtensionDataSetId( ),
                        						props ) );
			}
			catch ( OdaException e )
			{
				return;
			}
		}
		dataSetDesign.getPrivateProperties( )
				.setProperty( PROP_PRIVATE_PARAMETERMETADATA, paramMdPropBuf.toString( ) );
	}

    private static void convertToDataElementAttributes(
            final ValueExpressionVariable var, DataElementAttributes elementAttrs )
    {
        DataType varDataType = var.getDataType();
        elementAttrs.setNativeDataTypeCode( getJDBCTypeCode( varDataType ));

        if( varDataType != null )
        {
            // get precision if applicable
            if( varDataType instanceof NumericalDataType )
            {
                int precision = ((NumericalDataType) varDataType).getPrecision();
                if( precision > 0 )
                    elementAttrs.setPrecision( precision );
            }
            else if( varDataType instanceof CharacterStringDataType )
            {
                elementAttrs.setPrecision( ((CharacterStringDataType) varDataType).getLength() );
            }
    
            // get scale if applicable
            if( varDataType instanceof ExactNumericDataType )
            {
                elementAttrs.setScale( ((ExactNumericDataType) varDataType).getScale() );
            }
        }
        
        if( var.getLabel() != null )
            elementAttrs.setUiDisplayName( var.getLabel() );
        if( var.getDescription() != null )
            elementAttrs.setUiDescription( var.getDescription() );
    }
    
    private static int getJDBCTypeCode( DataType varDataType )
    {
        if( varDataType == null )
            return Types.NULL;  // unknown value 

        int nativeTypeCode = DataTypeHelper.getJDBCTypeForNamedType( varDataType.getName() );
        if( nativeTypeCode != 0 )   // has valid value
            return nativeTypeCode;
            
        // native data type conversion is not handled by DataTypeHelper
        if( varDataType instanceof PredefinedDataType )
        {
          if( ((PredefinedDataType) varDataType).getPrimitiveType().getValue() == PrimitiveType.BOOLEAN )
              return java.sql.Types.BOOLEAN;
          // TODO - handling of additional primitive JDBC data types
        }
        
        return Types.NULL;  // unknown value 
    }

    /**
     * Process the parameter definition to handle special use, such as setting
     * a null native data type to a String by default.
     */
    private static void adjustParameterDefinition( ParameterDefinition paramDefn )
    {
        assert( paramDefn != null );
        DataElementAttributes paramAttributes = paramDefn.getAttributes();
        if( paramAttributes == null )
            return;     // no attributes to adjust

        if( paramAttributes.getNativeDataTypeCode( ) == Types.NULL )
            paramAttributes.setNativeDataTypeCode( Types.CHAR );  // default data type

        // a SQL Select Query parameter cannot be null
        if( paramAttributes.allowsNull() )
            paramAttributes.setNullability( ElementNullability.NOT_NULLABLE_LITERAL );
    }
   
//    private static ParameterDefinition findMatchingParameter( DataSetParameters existingParamsDesign, ParameterDefinition paramDefn )
//    {
//        if( existingParamsDesign == null )
//            return null;
//        
//        DataElementAttributes paramAttributes = paramDefn.getAttributes();
//        Iterator iter = existingParamsDesign.getParameterDefinitions().iterator();
//        while( iter.hasNext() )
//        {
//            ParameterDefinition origParamDefn = (ParameterDefinition) iter.next( );
//            DataElementAttributes origParamAttributes = origParamDefn.getAttributes();
//            
//            // match by name and native data type
//            if( origParamAttributes.getName() == null || paramAttributes.getName() == null )
//                continue;
//            if( ! origParamAttributes.getName().equals( paramAttributes.getName() ))
//                continue;
//            if( origParamAttributes.getNativeDataTypeCode() != paramAttributes.getNativeDataTypeCode() )
//                continue;
//           
//            return origParamDefn;
//        }
//        return null;    // no matching parameter definition
//    }

    /**
     * Attempts to close given ODA connection.
     */
    private static void closeConnection( IConnection conn )
    {
        try
        {
            if( conn != null && conn.isOpen() )
                conn.close();
        }
        catch ( OdaException e )
        {
            // ignore
            e.printStackTrace();
        }
    }

}
