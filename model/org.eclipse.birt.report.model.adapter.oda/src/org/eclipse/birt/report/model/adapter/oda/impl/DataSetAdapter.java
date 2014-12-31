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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IConstants;
import org.eclipse.birt.report.model.adapter.oda.impl.ResultSetsAdapter.ResultSetColumnInfo;
import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.DynamicList;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.util.SchemaConversionUtil;
import org.eclipse.birt.report.model.adapter.oda.model.util.SerializerImpl;
import org.eclipse.birt.report.model.adapter.oda.util.IdentifierUtility;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.OdaDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 *
 */

class DataSetAdapter extends AbstractDataAdapter
{

	private String defaultDataSourceName = null;
	private boolean isLinkedParameter = false;
	/**
	 * 
	 */

	DataSetAdapter( )
	{
		super( );
	}

	DataSetAdapter( DataSourceHandle defaultDataSource )
	{
		super( );
		if ( defaultDataSource != null )
		{
			defaultDataSourceName = defaultDataSource.getName( );
			isLinkedParameter = true;
		}
	}

	/**
	 * @param setHandle
	 * @return
	 */

	DataSetDesign createDataSetDesign( OdaDataSetHandle setHandle )
	{
		if ( setHandle == null )
			return null;

		DataSetDesign setDesign = designFactory.createDataSetDesign( );
		updateDataSetDesign( setHandle, setDesign );
		return setDesign;
	}

	/**
	 * @param setHandle
	 * @param setDesign
	 */

	void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign )
	{
		// properties on ReportElement, like name, displayNames, etc.

		setDesign.setName( setHandle.getName( ) );

		String displayName = setHandle.getDisplayName( );
		String displayNameKey = setHandle.getDisplayNameKey( );

		if ( displayName != null || displayNameKey != null )
		{
			setDesign.setDisplayName( displayName );
			setDesign.setDisplayNameKey( displayNameKey );
		}

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		setDesign.setOdaExtensionDataSetId( setHandle.getExtensionID( ) );

		setDesign.setPublicProperties( newOdaPublicProperties(
				setHandle.getExtensionPropertyDefinitionList( ), setHandle ) );

		setDesign.setPrivateProperties( newOdaPrivateProperties( setHandle
				.privateDriverPropertiesIterator( ) ) );

		setDesign.setPrimaryResultSetName( setHandle.getResultSetName( ) );

		setDesign.setQueryText( setHandle.getQueryText( ) );

		// create a new data source design for this set design.

		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle
				.getDataSource( );

		if ( sourceHandle != null )
			setDesign.setDataSourceDesign( new DataSourceAdapter( )
					.createDataSourceDesign( sourceHandle ) );

		// when converts the ODA to BIRT, some information for parameter
		// definitions may be lost. So, must get the cached values first. Then
		// try to restore to latest value.

		String strDesignValues = setHandle.getDesignerValues( );

		DesignValues designerValues = null;

		try
		{
			if ( strDesignValues != null )
				designerValues = SerializerImpl.instance( ).read(
						strDesignValues );
			
		}
		catch ( IOException e )
		{
		}

		// the driver defined parameters are in the designer values

		DataSetParameters cachedParams = null;
		if ( designerValues != null )
		{
			cachedParams = designerValues.getDataSetParameters( );
		}

		DataSetParameterAdapter dataParamAdapter = new DataSetParameterAdapter(
				setHandle, setDesign );

		org.eclipse.datatools.connectivity.oda.design.DataSetParameters designDefinedParams = null;
		if ( cachedParams != null )
		{
			// cached are driver-defined parameters
			designDefinedParams = SchemaConversionUtil
					.convertToDesignParameters( EcoreUtil.copy( cachedParams ) );

			setDesign.setParameters( designDefinedParams );

			dataParamAdapter.updateDriverDefinedParameter( designDefinedParams,
					SchemaConversionUtil.getCachedDynamicList( cachedParams ) );
		}

		// if there is no driver defined parameters, update parameters with set
		// handle defined with cached in the last request

		if ( designDefinedParams == null )
		{
			designDefinedParams = dataParamAdapter
					.newOdaDataSetParams( designDefinedParams );
			setDesign.setParameters( designDefinedParams );
		}

		// handle those parameters defined by user

		dataParamAdapter.updateUserDefinedParameter( designDefinedParams );

		org.eclipse.datatools.connectivity.oda.design.DataSetParameters userDefinedParams = dataParamAdapter
				.newOdaDataSetParams( dataParamAdapter.getUserDefinedParams( ) );

		if ( designDefinedParams == null )
		{
			designDefinedParams = userDefinedParams;
			setDesign.setParameters( designDefinedParams );
		}
		else if ( userDefinedParams != null )
			designDefinedParams.getParameterDefinitions( ).addAll(
					userDefinedParams.getParameterDefinitions( ) );

		new ResultSetsAdapter( setHandle, setDesign )
				.updateOdaResultSetDefinition( );

		updateODAMessageFile( setDesign.getDataSourceDesign( ),
				setHandle.getModuleHandle( ) );
	}

	/**
	 * @param setHandle
	 * @param setDesign
	 * @param propertyName
	 */

	void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign, String propertyName )
	{
		if ( setHandle == null || setDesign == null || propertyName == null )
			return;

		// properties on ReportElement, like name, displayNames, etc.

		if ( OdaDataSetHandle.NAME_PROP.equalsIgnoreCase( propertyName ) )
			setDesign.setName( setHandle.getName( ) );
		else if ( OdaDataSetHandle.DISPLAY_NAME_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setDisplayName( setHandle.getDisplayName( ) );
		else if ( OdaDataSetHandle.DISPLAY_NAME_ID_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setDisplayNameKey( setHandle.getDisplayNameKey( ) );

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		else if ( OdaDataSourceHandle.EXTENSION_ID_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setOdaExtensionDataSetId( setHandle.getExtensionID( ) );

		else if ( OdaDataSetHandle.PRIVATE_DRIVER_PROPERTIES_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setPrivateProperties( newOdaPrivateProperties( setHandle
					.privateDriverPropertiesIterator( ) ) );

		else if ( OdaDataSetHandle.RESULT_SET_NAME_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setPrimaryResultSetName( setHandle.getResultSetName( ) );

		else if ( OdaDataSetHandle.QUERY_TEXT_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setQueryText( setHandle.getQueryText( ) );

		// create a new data source design for this set design.

		else if ( OdaDataSetHandle.DATA_SOURCE_PROP
				.equalsIgnoreCase( propertyName ) )
		{
			OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle
					.getDataSource( );

			if ( sourceHandle != null )
				setDesign.setDataSourceDesign( new DataSourceAdapter( )
						.createDataSourceDesign( sourceHandle ) );
		}

		else if ( OdaDataSetHandle.PARAMETERS_PROP
				.equalsIgnoreCase( propertyName ) )
		{
			org.eclipse.datatools.connectivity.oda.design.DataSetParameters dsParams = new DataSetParameterAdapter(
					setHandle, setDesign )
					.newOdaDataSetParams( SchemaConversionUtil
							.convertToDesignParameters( getCachedParameters( setHandle ) ) );

			if ( dsParams != null )
				setDesign.setParameters( dsParams );
			else
				setDesign.setParameters( null );
		}

		else if ( OdaDataSetHandle.RESULT_SET_PROP
				.equalsIgnoreCase( propertyName ) )
			new ResultSetsAdapter( setHandle, setDesign )
					.updateOdaResultSetDefinition( );

		else if ( OdaDataSetHandle.COLUMN_HINTS_PROP
				.equalsIgnoreCase( propertyName ) )
			new ResultSetsAdapter( setHandle, setDesign )
					.updateOdaColumnHints( );

		else if ( OdaDataSetHandle.FILTER_PROP.equalsIgnoreCase( propertyName ) )
			new ResultSetCriteriaAdapter( setHandle, setDesign )
					.updateODAResultSetCriteria( );

		updateODAMessageFile( setDesign.getDataSourceDesign( ),
				setHandle.getModuleHandle( ) );
	}

	/**
	 * @param setHandle
	 * @return
	 */

	private DataSetParameters getCachedParameters( OdaDataSetHandle setHandle )
	{
		String strDesignValues = setHandle.getDesignerValues( );

		DesignValues designerValues = null;

		try
		{
			if ( strDesignValues != null )
				designerValues = SerializerImpl.instance( ).read(
						strDesignValues );
		}
		catch ( IOException e )
		{
		}

		// the driver defined parameters are in the designer values

		DataSetParameters cachedParams = null;
		if ( designerValues != null )
			cachedParams = designerValues.getDataSetParameters( );

		return cachedParams;
	}

	/**
	 * @param dataSetHandle
	 * @return
	 */

	public OdaDesignSession createOdaDesignSession(
			OdaDataSetHandle dataSetHandle )
	{
		OdaDesignSession session = designFactory.createOdaDesignSession( );
		DataSetDesign setDesign = createDataSetDesign( dataSetHandle );
		DesignSessionRequest request = designFactory
				.createDesignSessionRequest( );
		request.setNewDataAccessDesign( setDesign );
		request.setDesignerState( DesignerStateAdapter
				.createOdaDesignState( dataSetHandle.getDesignerState( ) ) );

		session.setRequest( request );
		return session;
	}

	OdaDataSetHandle createDataSetHandle( DataSetDesign setDesign,
			ModuleHandle module ) throws SemanticException,
			IllegalStateException
	{
		if ( setDesign == null )
			return null;

		// validate the source design to make sure it is valid

		DesignUtil.validateObject( setDesign );

		OdaDataSetHandle setHandle = module.getElementFactory( ).newOdaDataSet(
				setDesign.getName( ), setDesign.getOdaExtensionDataSetId( ) );

		if ( setHandle == null )
			return null;

		adaptDataSetDesign( setDesign, setHandle );
		return setHandle;
	}

	public void updateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, boolean isSourceChanged )
			throws SemanticException
	{
		if ( setDesign == null || setHandle == null )
			return;

		// serialize and get the designer values
		DesignValues designerValues = null;
		try
		{
			designerValues = SerializerImpl.instance( ).read(
					setHandle.getDesignerValues( ) );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}

		DataSetParameters requestParameters = null;
		ResultSets requestResultSets = null;
		if ( designerValues != null )
		{
			//Do not consider datasetparameters in designervalues.
			//Parameters read from old version report is not null.
			//requestParameters = designerValues.getDataSetParameters( );
			requestResultSets = designerValues.getResultSets( );
		}

		updateDataSetHandle( setDesign, setHandle, isSourceChanged,
				requestParameters, requestResultSets );
	}

	/**
	 * @param dataSetHandle
	 * @param completedSession
	 * @throws SemanticException
	 */

	public void updateDataSetHandle( OdaDataSetHandle dataSetHandle,
			OdaDesignSession completedSession ) throws SemanticException
	{
		if ( completedSession == null || dataSetHandle == null )
			return;

		DataSetDesign responseDesign = completedSession
				.getResponseDataSetDesign( );
		DataSetDesign requestDesign = completedSession
				.getRequestDataSetDesign( );

		updateDataSetHandle( responseDesign, dataSetHandle, false,
				SchemaConversionUtil.convertToAdapterParameters( requestDesign
						.getParameters( ) ), requestDesign.getResultSets( ) );

		DesignerStateAdapter.updateROMDesignerState( completedSession
				.getResponse( ).getDesignerState( ), dataSetHandle );;
	}

	/**
	 * Copies values of <code>setDesign</code> to <code>setHandle</code>. Values
	 * in <code>setDesign</code> are validated before maps to values in
	 * OdaDataSetHandle.
	 * 
	 * @param setDesign
	 *            the ODA data set design
	 * @param setHandle
	 *            the Model handle
	 * @throws SemanticException
	 *             if any value is invalid.
	 * 
	 */

	private void adaptDataSetDesign( DataSetDesign setDesign,
			OdaDataSetHandle setHandle ) throws SemanticException
	{

		Object value = null;

		// properties on ReportElement, like name, displayNames, etc.

		value = setDesign.getName( );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.NAME_PROP, value );
		setHandle.getElement( ).setName( setDesign.getName( ) );

		// properties on ReportElement, like name, displayNames, etc.

		value = setDesign.getDisplayName( );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.DISPLAY_NAME_PROP, value );
		setHandle.getElement( ).setProperty(
				OdaDataSetHandle.DISPLAY_NAME_PROP, value );

		value = setDesign.getDisplayNameKey( );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.DISPLAY_NAME_ID_PROP, value );
		setHandle.getElement( ).setProperty(
				OdaDataSetHandle.DISPLAY_NAME_ID_PROP, value );

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		// set null or empty list if the return list is empty.

		value = newROMPrivateProperties( setDesign.getPrivateProperties( ) );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.PRIVATE_DRIVER_PROPERTIES_PROP, value );
		setHandle.getElement( ).setProperty(
				OdaDataSetHandle.PRIVATE_DRIVER_PROPERTIES_PROP, value );

		updateROMPublicProperties( setDesign.getPublicProperties( ), setHandle );

		DataSourceDesign sourceDesign = setDesign.getDataSourceDesign( );
		String dataSourceName = null;
		if ( sourceDesign != null )
		{
			dataSourceName = sourceDesign.getName( );
			ModuleHandle moduleHandle = setHandle.getModuleHandle( );
			DataSourceHandle sourceHandle = null;			
			if ( isLinkedParameter )
			{
				sourceHandle = moduleHandle.getElementFactory( )
						.newOdaDataSource( dataSourceName,
						sourceDesign.getOdaExtensionDataSourceId( ) );
				moduleHandle.getDataSources( ).add( sourceHandle );
			}
			else
				sourceHandle = moduleHandle.findDataSource( dataSourceName );
			if ( sourceHandle != null
					&& sourceHandle instanceof OdaDataSourceHandle )
			{
				new DataSourceAdapter( ).updateDataSourceHandle( sourceDesign,
						(OdaDataSourceHandle) sourceHandle );
				dataSourceName = sourceHandle.getName( );
			}			
		}
		else 
			dataSourceName = defaultDataSourceName;
		
		setHandle.getElement( ).setProperty(
					OdaDataSetHandle.DATA_SOURCE_PROP,
					PropertyValueValidationUtil.validateProperty(
							setHandle,
							OdaDataSetHandle.DATA_SOURCE_PROP,
							dataSourceName ) );

			

		// set the data set parameter list.

		setHandle.getElement( )
				.clearProperty( OdaDataSetHandle.PARAMETERS_PROP );

		List dataSetParams = new DataSetParameterAdapter( setHandle, setDesign )
				.newROMSetParams( null );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.PARAMETERS_PROP, dataSetParams );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.PARAMETERS_PROP,
				dataSetParams );

		// set the result sets

		ResultSetsAdapter tmpAdapter = new ResultSetsAdapter( setHandle,
				setDesign );
		List resultRetColumns = tmpAdapter.newROMResultSets( null );
		// add filter condition for the result set
		tmpAdapter.updateROMFilterCondition( );

		List columns = null;
		List hints = null;

		// if the return value is null, do not create an empty list.

		if ( resultRetColumns != null )
		{
			columns = new ArrayList( );
			hints = new ArrayList( );

			ResultSetColumnInfo.updateResultSetColumnList( resultRetColumns,
					columns, hints );
			if ( hints.isEmpty( ) )
				hints = null;

			PropertyValueValidationUtil.validateProperty( setHandle,
					OdaDataSetHandle.RESULT_SET_PROP, columns );
			PropertyValueValidationUtil.validateProperty( setHandle,
					OdaDataSetHandle.COLUMN_HINTS_PROP, hints );
		}
		setHandle.getElement( ).setProperty(
				OdaDataSetHandle.COLUMN_HINTS_PROP, hints );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.RESULT_SET_PROP,
				columns );

		// set the query text.

		String queryText = setDesign.getQueryText( );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.QUERY_TEXT_PROP, queryText );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.QUERY_TEXT_PROP,
				queryText );

		// set the result name

		String resultSetName = setDesign.getPrimaryResultSetName( );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.RESULT_SET_NAME_PROP, queryText );
		setHandle.getElement( ).setProperty(
				OdaDataSetHandle.RESULT_SET_NAME_PROP, resultSetName );

		// convert data set paramters and result set columns first. Then update
		// designer values.

		String odaValues = serializeOdaValues( setDesign );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.DESIGNER_VALUES_PROP, odaValues );
		setHandle.getElement( ).setProperty(
				OdaDataSetHandle.DESIGNER_VALUES_PROP, odaValues );

		updateROMMessageFile( setDesign.getDataSourceDesign( ),
				setHandle.getModuleHandle( ) );
	}

	/**
	 * Gets the serializable string for design values. Design values include
	 * data set parameter definitions and result set definitions.
	 * 
	 * @param setDesign
	 *            the data set desgin
	 * @return the serializable string for design values
	 */

	private String serializeOdaValues( DataSetDesign setDesign )
	{
		DataSetParameters params = SchemaConversionUtil
				.convertToAdapterParameters( setDesign.getParameters( ) );
		ResultSets resultSets = setDesign.getResultSets( );

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setVersion( IConstants.DESINGER_VALUES_VERSION );
		boolean hasData = false;

		if ( params != null )
		{
			values.setDataSetParameters( (DataSetParameters) EcoreUtil
					.copy( params ) );
			hasData = true;
		}

		if ( resultSets != null )
		{
			values.setResultSets( (ResultSets) EcoreUtil.copy( resultSets ) );
			hasData = true;
		}

		if ( !hasData )
			return IConstants.EMPTY_STRING;

		try
		{
			return SerializerImpl.instance( ).write( values );
		}
		catch ( IOException e )
		{
			return null;
		}
	}

	/**
	 * Updates the data set handle with specified values.
	 * 
	 * @param setDesign
	 *            the data set design
	 * @param setHandle
	 *            the data set handle
	 * @param isSourceChanged
	 * @param requestParameters
	 * @param requestResultSets
	 * @throws SemanticException
	 */

	protected void processUpdateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, boolean isSourceChanged,
			DataSetParameters requestParameters, ResultSets requestResultSets )
			throws SemanticException
	{
		if ( setDesign == null || setHandle == null )
			return;

		DesignUtil.validateObject( setDesign );

		CommandStack stack = setHandle.getModuleHandle( ).getCommandStack( );

		stack.startTrans( null );
		try
		{
			// extension id is set without undo/redo support.

			setHandle.getElement( ).setProperty(
					OdaDataSourceHandle.EXTENSION_ID_PROP,
					setDesign.getOdaExtensionDataSetId( ) );

			setHandle.setName( setDesign.getName( ) );
			setHandle.setDisplayName( setDesign.getDisplayName( ) );
			setHandle.setDisplayNameKey( setDesign.getDisplayNameKey( ) );

			// set public properties.

			Properties props = setDesign.getPublicProperties( );
			if ( props != null )
			{
				EList propList = props.getProperties( );
				for ( int i = 0; i < propList.size( ); i++ )
				{
					Property prop = (Property) propList.get( i );
					setHandle.setProperty( prop.getName( ), prop.getValue( ) );
				}
			}

			// set private properties.

			props = setDesign.getPrivateProperties( );
			if ( props != null )
			{
				EList propList = props.getProperties( );
				for ( int i = 0; i < propList.size( ); i++ )
				{
					Property prop = (Property) propList.get( i );
					setHandle.setPrivateDriverProperty( prop.getName( ),
							prop.getValue( ) );
				}
			}

			ResultSetDefinition cachedResultDefn = null;

			if ( requestResultSets != null
					&& !requestResultSets.getResultSetDefinitions( ).isEmpty( ) )
				cachedResultDefn = (ResultSetDefinition) requestResultSets
						.getResultSetDefinitions( ).get( 0 );

			ResultSetsAdapter tmpAdapter = new ResultSetsAdapter( setHandle,
					setDesign );
			updateROMResultSets( setHandle, tmpAdapter, cachedResultDefn );

			setHandle.setResultSetName( setDesign.getPrimaryResultSetName( ) );

			setHandle.setQueryText( setDesign.getQueryText( ) );

			// designer values must be saved after convert data set parameters
			// and result set columns.

			// Set Parameter

			// Get user-defined parameters

			DataSetParameterAdapter dataParamAdapter = new DataSetParameterAdapter(
					setHandle, setDesign );
			dataParamAdapter.updateUserDefinedParameter( SchemaConversionUtil
					.convertToDesignParameters( requestParameters ) );

			// Update parameters of dataset handle.

			updateROMDataSetParams( dataParamAdapter, requestParameters );

			DataSourceDesign sourceDesign = setDesign.getDataSourceDesign( );
			if ( sourceDesign != null )
			{
				OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle
						.getDataSource( );

				DataSourceAdapter dataSourceAdapter = new DataSourceAdapter( !isLinkedParameter );

				// only the local data source can be used.

				if ( isSourceChanged && sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( ) )
				{
					setHandle.setDataSource( sourceDesign.getName( ) );
					dataSourceAdapter.updateDataSourceHandle( sourceDesign,
							sourceHandle );
				}

				// if the source is not changed, and it is not in the included
				// library, then we can update it.

				if ( !isSourceChanged
						&& sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( )
						&& !( dataSourceAdapter
								.isEqualDataSourceDesign(
										dataSourceAdapter
												.createDataSourceDesign( sourceHandle ),
										sourceDesign ) ) )
				{
					dataSourceAdapter.updateDataSourceHandle( sourceDesign,
							sourceHandle );
				}
			}
			else
				setHandle.setDataSource( defaultDataSourceName );

			updateDesignerValue( setDesign, setHandle, requestParameters,
					dataParamAdapter.getUserDefinedParams( ), requestResultSets );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Updates the data set handle with specified values.
	 * 
	 * @param setDesign
	 *            the data set design
	 * @param setHandle
	 *            the data set handle
	 * @param isSourceChanged
	 * @param requestParameters
	 * @param requestResultSets
	 * @throws SemanticException
	 */

	private void updateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, boolean isSourceChanged,
			DataSetParameters requestParameters, ResultSets requestResultSets )
			throws SemanticException
	{
		if ( setDesign == null || setHandle == null )
			return;

		DesignUtil.validateObject( setDesign );

		CommandStack stack = setHandle.getModuleHandle( ).getCommandStack( );

		stack.startTrans( null );
		try
		{
			// extension id is set without undo/redo support.

			setHandle.getElement( ).setProperty(
					OdaDataSourceHandle.EXTENSION_ID_PROP,
					setDesign.getOdaExtensionDataSetId( ) );

			setHandle.setName( setDesign.getName( ) );
			setHandle.setDisplayName( setDesign.getDisplayName( ) );
			setHandle.setDisplayNameKey( setDesign.getDisplayNameKey( ) );

			// set public properties.

			Properties props = setDesign.getPublicProperties( );
			if ( props != null )
			{
				EList propList = props.getProperties( );
				for ( int i = 0; i < propList.size( ); i++ )
				{
					Property prop = (Property) propList.get( i );
					setHandle.setProperty( prop.getName( ), prop.getValue( ) );
				}
			}

			// set private properties.

			props = setDesign.getPrivateProperties( );
			if ( props != null )
			{
				EList propList = props.getProperties( );
				for ( int i = 0; i < propList.size( ); i++ )
				{
					Property prop = (Property) propList.get( i );
					setHandle.setPrivateDriverProperty( prop.getName( ),
							prop.getValue( ) );
				}
			}

			ResultSetDefinition cachedResultDefn = null;

			if ( requestResultSets != null
					&& !requestResultSets.getResultSetDefinitions( ).isEmpty( ) )
				cachedResultDefn = (ResultSetDefinition) requestResultSets
						.getResultSetDefinitions( ).get( 0 );

			ResultSetsAdapter tmpAdapter = new ResultSetsAdapter( setHandle,
					setDesign );
			updateROMResultSets( setHandle, tmpAdapter, cachedResultDefn );

			setHandle.setResultSetName( setDesign.getPrimaryResultSetName( ) );

			setHandle.setQueryText( setDesign.getQueryText( ) );

			// designer values must be saved after convert data set parameters
			// and result set columns.

			// Set Parameter

			// Get user-defined parameters

			DataSetParameterAdapter dataParamAdapter = new DataSetParameterAdapter(
					setHandle, setDesign );
			dataParamAdapter.updateUserDefinedParameter( SchemaConversionUtil
					.convertToDesignParameters( requestParameters ) );

			// Update parameters of dataset handle.

			updateROMDataSetParams( dataParamAdapter, requestParameters );

			DataSourceDesign sourceDesign = setDesign.getDataSourceDesign( );
			if ( sourceDesign != null )
			{
				OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle
						.getDataSource( );

				DataSourceAdapter dataSourceAdapter = new DataSourceAdapter( );

				// only the local data source can be used.

				if ( isSourceChanged && sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( ) )
				{
					setHandle.setDataSource( sourceDesign.getName( ) );
					dataSourceAdapter.updateDataSourceHandle( sourceDesign,
							sourceHandle );
				}

				// if the source is not changed, and it is not in the included
				// library, then we can update it.

				if ( !isSourceChanged
						&& sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( )
						&& !( dataSourceAdapter
								.isEqualDataSourceDesign(
										dataSourceAdapter
												.createDataSourceDesign( sourceHandle ),
										sourceDesign ) ) )
				{
					dataSourceAdapter.updateDataSourceHandle( sourceDesign,
							sourceHandle );
				}
			}
			else
				setHandle.setDataSource( defaultDataSourceName );

			updateDesignerValue( setDesign, setHandle, requestParameters,
					dataParamAdapter.getUserDefinedParams( ), requestResultSets );

			updateROMMessageFile( sourceDesign, setHandle.getModuleHandle( ) );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Updates the designer value. Designer values only contain Driver-defined
	 * parameters.
	 * 
	 * @param setDesign
	 *            the data set design
	 * @param setHandle
	 *            the data set handle
	 * @param designerValues
	 *            the designer values
	 * @param userDefinedList
	 *            the user defined parameters
	 * @throws SemanticException
	 */

	private void updateDesignerValue( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, DataSetParameters requestParameters,
			List userDefinedList, ResultSets requestResultSets )
			throws SemanticException
	{

		DesignValues designerValues = null;

		// update design values.

		org.eclipse.datatools.connectivity.oda.design.DataSetParameters setDefinedParams = setDesign
				.getParameters( );

		if ( setDefinedParams == null )
		{
			if ( requestParameters != null )
			{
				designerValues = ModelFactory.eINSTANCE.createDesignValues( );
				designerValues.setDataSetParameters( null );
			}
		}
		else
		{
			org.eclipse.datatools.connectivity.oda.design.DataSetParameters driverParams = DataSetParameterAdapter
					.getDriverDefinedParameters(
							setDefinedParams.getParameterDefinitions( ),
							userDefinedList );
			
			if ( driverParams.getParameterDefinitions( ).size( ) > 0 )
			{
				designerValues = ModelFactory.eINSTANCE.createDesignValues( );

				DataSetParameters adapterParams = SchemaConversionUtil
						.convertToAdapterParameters( driverParams );
				designerValues.setDataSetParameters( adapterParams );

				clearReportParameterRelatedValues(
						adapterParams.getParameters( ),
						setHandle.getModuleHandle( ) );
			}
		}

		if ( designerValues == null && requestResultSets == null )
		{
			setHandle.setDesignerValues( null );
			return;
		}

		if ( requestResultSets != null )
		{
			if ( designerValues == null )
			{
				designerValues = ModelFactory.eINSTANCE.createDesignValues( );
				designerValues.setDataSetParameters( null );
			}

			designerValues.setResultSets( requestResultSets );
		}
		else
			designerValues.setResultSets( null );

		// Set DesignerValues

		try
		{
			String dValue = SerializerImpl.instance( ).write( designerValues );
			setHandle.setDesignerValues( dValue );
		}
		catch ( IOException e )
		{
		}
	}

	/**
	 * Removes ODA data set parameter information that relates to the report
	 * parameter. In the design value, do not need to save data for the report
	 * parameter.
	 * 
	 * @param dsParams
	 */

	private static void clearReportParameterRelatedValues(
			EList<DataSetParameter> params, ModuleHandle module )
	{
		if ( params == null )
			return;

		for ( int i = 0; i < params.size( ); i++ )
		{
			DataSetParameter adapterParam = params.get( i );
			ParameterDefinition param = adapterParam.getParameterDefinition( );

			InputParameterAttributes paramAttrs = param.getInputAttributes( );
			if ( paramAttrs == null )
				continue;

			InputElementAttributes elementAttrs = paramAttrs
					.getElementAttributes( );
			if ( elementAttrs == null )
				continue;

			DynamicValuesQuery query = elementAttrs.getDynamicValueChoices( );
			if ( query == null )
				continue;

			DataSetDesign setDesign = query.getDataSetDesign( );
			String setName = setDesign.getName( );

			if ( module.findDataSet( setName ) != null )
			{
				// need to cache dynamic value query here. If the user breaks
				// the relationship between data set parameter and report
				// parameter. This cached value is used to update the dynamic
				// value in the new data set design
				
				DynamicList cachedDynamic = ModelFactory.eINSTANCE
						.createDynamicList( );
				cachedDynamic.setDataSetName( setName );
				cachedDynamic.setValueColumn( query.getValueColumn( ) );
				cachedDynamic.setLabelColumn( query.getDisplayNameColumn( ) );

				adapterParam.setDynamicList( cachedDynamic );

				elementAttrs.setDynamicValueChoices( null );
			}
		}
	}

	/**
	 * Updates a structure list with the corresponding property handle.
	 * 
	 * @param propHandle
	 *            the property handle
	 * @param structList
	 *            the structure list
	 * @throws SemanticException
	 *             if any structure has invalid value.
	 */

	private void updateROMResultSets( OdaDataSetHandle setHandle,
			ResultSetsAdapter tmpAdapter, ResultSetDefinition cachedResultDefn )
			throws SemanticException
	{
		List structList = tmpAdapter.newROMResultSets( cachedResultDefn );

		List columns = new ArrayList( );
		List hints = new ArrayList( );

		ResultSetColumnInfo.updateResultSetColumnList( structList, columns,
				hints );

		setHandle.setProperty( OdaDataSetHandle.RESULT_SET_PROP, new ArrayList( ) );
		PropertyHandle propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP );

		if ( !columns.isEmpty( ) )
		{
			for ( int i = 0; i < columns.size( ); i++ )
				propHandle.addItem( columns.get( i ) );
		}

		setHandle.setProperty( OdaDataSetHandle.COLUMN_HINTS_PROP, new ArrayList( ) );
		propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.COLUMN_HINTS_PROP );

		if ( !hints.isEmpty( ) )
		{			
			for ( int i = 0; i < hints.size( ); i++ )
			{
				ColumnHint hint = (ColumnHint) hints.get( i );
				ColumnHintHandle oldHint = AdapterUtil.findColumnHint(
						(String) hint.getProperty( null,
								ColumnHint.COLUMN_NAME_MEMBER ), setHandle
								.columnHintsIterator( ) );

				if ( oldHint == null )
					propHandle.addItem( hints.get( i ) );
				else
				{
					oldHint.setDisplayName( (String) hint.getProperty( null,
							ColumnHint.DISPLAY_NAME_MEMBER ) );
					oldHint.setDisplayNameKey( (String) hint.getProperty( null,
							ColumnHint.DISPLAY_NAME_ID_MEMBER ) );
					oldHint.setHelpText( (String) hint.getProperty( null,
							ColumnHint.HELP_TEXT_MEMBER ) );
					oldHint.setHelpTextKey( (String) hint.getProperty( null,
							ColumnHint.HELP_TEXT_ID_MEMBER ) );
					oldHint.setFormat( (String) hint.getProperty( null,
							ColumnHint.FORMAT_MEMBER ) );
				}
			}
		}		

		// add column hints for the computed column

		List hints4ComputedColumn = tmpAdapter.getHintsForComputedColumn( );
		for ( int i = 0; i < hints4ComputedColumn.size( ); i++ )
		{
			propHandle.addItem( (ColumnHint) hints4ComputedColumn.get( i ) );
		}

		// add filter condition for the result set
		tmpAdapter.updateROMFilterCondition( );
	}

	/**
	 * Update parameters in DataSetHandle with DataSetDesign's.
	 * 
	 * @param setDesign
	 *            data set design contains driver-defined parameters
	 * @param setHandle
	 *            data set handle
	 * @param cachedParameters
	 *            the cached data set parameters in the designer values
	 * @param userDefinedList
	 *            a list contains user-defined parameters. Each item is
	 *            <code>OdaDataSetParameter</code>.
	 * 
	 * @throws SemanticException
	 */

	private void updateROMDataSetParams(
			DataSetParameterAdapter setParamAdapter,
			DataSetParameters cachedParameters ) throws SemanticException
	{
		List newParams = setParamAdapter.newROMSetParams( SchemaConversionUtil
				.convertToDesignParameters( cachedParameters ) );

		// Merge all parameter list with data set handle.

		// If the name is the same , should rename it.
		// when you have three driver-defined parameter in DataSetDesign,but you
		// remove two of them
		// in handle, then when you back to 'parameter' page, you can get three
		// parameter and in this
		// time it's easy to duplicate name.

		IdentifierUtility.updateParams2UniqueName( newParams );

		// if one parameter in newParams has the corresponding data set
		// parameter in data set handle, use the new parameter to update the one
		// on set handle.

		setParamAdapter.updateRomDataSetParamsWithNewParams( newParams );
	}
}
