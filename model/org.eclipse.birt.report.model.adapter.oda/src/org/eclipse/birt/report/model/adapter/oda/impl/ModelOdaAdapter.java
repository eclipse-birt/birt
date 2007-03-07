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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IConstants;
import org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.impl.ResultSetsAdapter.ResultSetColumnInfo;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.util.SerializerImpl;
import org.eclipse.birt.report.model.adapter.oda.util.IdentifierUtility;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * An adapter class that converts between ROM OdaDataSourceHandle and ODA
 * DataSourceDesign.
 * 
 * @see OdaDataSourceHandle
 * @see DataSourceDesign
 */

public class ModelOdaAdapter implements IModelOdaAdapter
{
	/**
	 * Constructs a DesignEngine with the given platform config.
	 * 
	 * @param config
	 *            the platform config.
	 */

	public ModelOdaAdapter( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSourceDesign(org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DataSourceDesign createDataSourceDesign(
			OdaDataSourceHandle sourceHandle )
	{
		if ( sourceHandle == null )
			return null;

		DataSourceDesign sourceDesign = ODADesignFactory.getFactory( )
				.createDataSourceDesign( );
		updateDataSourceDesign( sourceHandle, sourceDesign );
		return sourceDesign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DataSetDesign createDataSetDesign( OdaDataSetHandle setHandle )
	{
		if ( setHandle == null )
			return null;

		DataSetDesign setDesign = ODADesignFactory.getFactory( )
				.createDataSetDesign( );
		updateDataSetDesign( setHandle, setDesign );
		return setDesign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSetHandle(org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 *      org.eclipse.birt.report.model.api.ModuleHandle)
	 */

	public OdaDataSetHandle createDataSetHandle( DataSetDesign setDesign,
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

	/**
	 * Copies values of <code>setDesign</code> to <code>setHandle</code>.
	 * Values in <code>setDesign</code> are validated before maps to values in
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
		setHandle.getElement( )
				.setProperty( OdaDataSetHandle.DISPLAY_NAME_PROP,
						setDesign.getDisplayName( ) );

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

		if ( sourceDesign != null )
		{
			String dataSourceName = sourceDesign.getName( );
			setHandle.getElement( )
					.setProperty(
							OdaDataSetHandle.DATA_SOURCE_PROP,
							PropertyValueValidationUtil.validateProperty(
									setHandle,
									OdaDataSetHandle.DATA_SOURCE_PROP,
									dataSourceName ) );
		}
		else
			setHandle.getElement( ).clearProperty(
					OdaDataSetHandle.DATA_SOURCE_PROP );

		// set the data set parameter list.

		setHandle.getElement( )
				.clearProperty( OdaDataSetHandle.PARAMETERS_PROP );

		List dataSetParams = new DataSetParameterAdapter( ).newROMSetParams(
				setDesign, setHandle, null, null );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.PARAMETERS_PROP, dataSetParams );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.PARAMETERS_PROP,
				dataSetParams );

		// set the result sets

		List resultRetColumns = new ResultSetsAdapter( ).newROMResultSets(
				setDesign, setHandle, null );

		List columns = null;
		List hints = null;

		// if the return value is null, do not create an empty list.

		if ( resultRetColumns != null )
		{
			columns = new ArrayList( );
			hints = new ArrayList( );

			ResultSetColumnInfo.updateResultSetColumnList( resultRetColumns,
					columns, hints );

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
		DataSetParameters params = setDesign.getParameters( );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */

	public void updateDataSetDesign( OdaDataSetHandle setHandle,
			DataSetDesign setDesign )
	{
		// properties on ReportElement, like name, displayNames, etc.

		setDesign.setName( setHandle.getName( ) );
		setDesign.setDisplayName( setHandle.getDisplayName( ) );

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		setDesign.setOdaExtensionDataSetId( setHandle.getExtensionID( ) );

		setDesign.setPublicProperties( newOdaPublicProperties( setHandle
				.getExtensionPropertyDefinitionList( ), setHandle ) );

		setDesign.setPrivateProperties( newOdaPrivateProperties( setHandle
				.privateDriverPropertiesIterator( ) ) );

		setDesign.setPrimaryResultSetName( setHandle.getResultSetName( ) );

		setDesign.setQueryText( setHandle.getQueryText( ) );

		// create a new data source design for this set design.

		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle
				.getDataSource( );

		if ( sourceHandle != null )
			setDesign
					.setDataSourceDesign( createDataSourceDesign( sourceHandle ) );

		String strDesignValues = setHandle.getDesignerValues( );

		DesignValues designerValues = null;

		try
		{
			designerValues = SerializerImpl.instance( ).read( strDesignValues );
		}
		catch ( IOException e )
		{
		}

		if ( designerValues != null
				&& designerValues.getDataSetParameters( ) != null )
		{
			setDesign.setParameters( (DataSetParameters) EcoreUtil
					.copy( designerValues.getDataSetParameters( ) ) );
		}

		if ( setDesign.getParameters( ) == null )
		{
			setDesign.setParameters( new DataSetParameterAdapter( )
					.newOdaDataSetParams( setHandle.parametersIterator( ),
							designerValues == null ? null : designerValues
									.getDataSetParameters( ), setDesign ) );
		}

		setDesign.setPrimaryResultSet( new ResultSetsAdapter( )
				.newOdaResultSetDefinition( setHandle ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 *      java.lang.String)
	 */

	public void updateDataSetDesign( OdaDataSetHandle setHandle,
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
				setDesign
						.setDataSourceDesign( createDataSourceDesign( sourceHandle ) );
		}

		else if ( OdaDataSetHandle.PARAMETERS_PROP
				.equalsIgnoreCase( propertyName ) )
		{
			updateDataSetDesignParams( setDesign, setHandle );
		}

		else if ( OdaDataSetHandle.RESULT_SET_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setPrimaryResultSet( new ResultSetsAdapter( )
					.newOdaResultSetDefinition( setHandle ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSourceDesign(org.eclipse.birt.report.model.api.OdaDataSourceHandle,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSourceDesign)
	 */

	public void updateDataSourceDesign( OdaDataSourceHandle sourceHandle,
			DataSourceDesign sourceDesign )
	{
		// properties on ReportElement, like name, displayNames, etc.

		sourceDesign.setName( sourceHandle.getName( ) );
		sourceDesign.setDisplayName( sourceHandle.getDisplayName( ) );

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		sourceDesign.setOdaExtensionId( sourceHandle.getExtensionID( ) );

		sourceDesign
				.setPrivateProperties( newOdaPrivateProperties( sourceHandle
						.privateDriverPropertiesIterator( ) ) );

		sourceDesign.setPublicProperties( newOdaPublicProperties( sourceHandle
				.getExtensionPropertyDefinitionList( ), sourceHandle ) );

		// updateOdaPublicProperties( sourceDesign.getPublicProperties( ),
		// sourceHandle );
	}

	/**
	 * Converts ROM public properties to ODA <code>Properties</code> instance.
	 * 
	 * @param sourceHandle
	 *            the data source handle
	 * @return <code>Properties</code> containing ROM public property values.
	 */

	private Properties newOdaPublicProperties( List propDefns,
			ReportElementHandle element )
	{
		if ( propDefns == null )
			return null;

		Properties retProps = null;

		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			if ( retProps == null )
				retProps = ODADesignFactory.getFactory( ).createProperties( );
			IPropertyDefn propDefn = (IPropertyDefn) propDefns.get( i );
			String propName = propDefn.getName( );
			String propValue = element.getStringProperty( propName );
			retProps.setProperty( propName, propValue );
		}

		return retProps;
	}

	/**
	 * Conversts <code>props</code> from Iterator to ODA
	 * <code>Properties</code>.
	 * 
	 * @param props
	 *            the iterator for extended property
	 * @return a new <code>Properties</code> object.
	 */

	private Properties newOdaPrivateProperties( Iterator props )
	{
		if ( props == null || !props.hasNext( ) )
			return null;

		Properties retProps = ODADesignFactory.getFactory( ).createProperties( );
		for ( ; props.hasNext( ); )
		{
			ExtendedPropertyHandle propHandle = (ExtendedPropertyHandle) props
					.next( );
			retProps
					.setProperty( propHandle.getName( ), propHandle.getValue( ) );
		}

		return retProps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#createDataSourceHandle(org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 *      org.eclipse.birt.report.model.api.ModuleHandle)
	 */

	public OdaDataSourceHandle createDataSourceHandle(
			DataSourceDesign sourceDesign, ModuleHandle module )
			throws SemanticException, IllegalStateException
	{
		if ( sourceDesign == null )
			return null;

		// validate the source design to make sure it is valid

		DesignUtil.validateObject( sourceDesign );
		OdaDataSourceHandle sourceHandle = module.getElementFactory( )
				.newOdaDataSource( sourceDesign.getName( ),
						sourceDesign.getOdaExtensionId( ) );

		if ( sourceHandle == null )
			return null;

		adaptDataSourceDesign( sourceDesign, sourceHandle );
		return sourceHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSourceHandle(org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 *      org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public void updateDataSourceHandle( DataSourceDesign sourceDesign,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
	{
		if ( sourceDesign == null || sourceHandle == null )
			return;

		DesignUtil.validateObject( sourceDesign );
		CommandStack stack = sourceHandle.getModuleHandle( ).getCommandStack( );

		stack.startTrans( null );
		try
		{
			// extension id is set without undo/redo support.

			sourceHandle.getElement( ).setProperty(
					OdaDataSourceHandle.EXTENSION_ID_PROP,
					sourceDesign.getOdaExtensionId( ) );

			sourceHandle.setName( sourceDesign.getName( ) );
			sourceHandle.setDisplayName( sourceDesign.getDisplayName( ) );

			// set public properties.

			Properties props = sourceDesign.getPublicProperties( );
			if ( props != null )
			{
				EList propList = props.getProperties( );
				for ( int i = 0; i < propList.size( ); i++ )
				{
					Property prop = (Property) propList.get( i );
					sourceHandle
							.setProperty( prop.getName( ), prop.getValue( ) );
				}
			}

			// updateROMPropertyBindings( props, sourceHandle );

			// set private properties.

			props = sourceDesign.getPrivateProperties( );
			if ( props != null )
			{
				EList propList = props.getProperties( );
				for ( int i = 0; i < propList.size( ); i++ )
				{
					Property prop = (Property) propList.get( i );
					sourceHandle.setPrivateDriverProperty( prop.getName( ),
							prop.getValue( ) );
				}
			}

		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Copies values of <code>sourceDesign</code> to <code>sourceHandle</code>.
	 * Values in <code>sourceDesign</code> are validated before maps to values
	 * in OdaDataSourceHandle.
	 * 
	 * @param sourceDesign
	 *            the ODA data source design
	 * @param sourceHandle
	 *            the Model handle
	 * @throws SemanticException
	 *             if any value is invalid.
	 * 
	 */

	private void adaptDataSourceDesign( DataSourceDesign sourceDesign,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
	{

		Object value = null;

		// properties on ReportElement, like name, displayNames, etc.

		value = sourceDesign.getName( );
		PropertyValueValidationUtil.validateProperty( sourceHandle,
				OdaDataSourceHandle.NAME_PROP, value );
		sourceHandle.getElement( ).setName( sourceDesign.getName( ) );

		// properties on ReportElement, like name, displayNames, etc.

		value = sourceDesign.getDisplayName( );
		PropertyValueValidationUtil.validateProperty( sourceHandle,
				OdaDataSourceHandle.DISPLAY_NAME_PROP, value );
		sourceHandle.getElement( ).setProperty(
				OdaDataSourceHandle.DISPLAY_NAME_PROP,
				sourceDesign.getDisplayName( ) );

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		// set null or empty list if the return list is empty.

		value = newROMPrivateProperties( sourceDesign.getPrivateProperties( ) );
		PropertyValueValidationUtil.validateProperty( sourceHandle,
				OdaDataSourceHandle.PRIVATE_DRIVER_PROPERTIES_PROP, value );
		sourceHandle.getElement( ).setProperty(
				OdaDataSourceHandle.PRIVATE_DRIVER_PROPERTIES_PROP, value );

		updateROMPublicProperties( sourceDesign.getPublicProperties( ),
				sourceHandle );

		// udpate property bindings, report parameters and so on.

		// updateROMPropertyBindings( sourceDesign.getPublicProperties( ),
		// sourceHandle );
	}

	/**
	 * Converts ODA <code>Properties</code> to ROM public properties.
	 * 
	 * @param sourceHandle
	 *            the data source handle
	 */

	private void updateROMPublicProperties( Properties designProps,
			ReportElementHandle sourceHandle ) throws SemanticException
	{
		if ( designProps == null )
			return;

		EList publicProps = designProps.getProperties( );
		for ( int i = 0; i < publicProps.size( ); i++ )
		{
			Property prop = (Property) publicProps.get( i );

			String propName = prop.getName( );
			String propValue = prop.getValue( );

			PropertyValueValidationUtil.validateProperty( sourceHandle,
					propName, propValue );

			sourceHandle.getElement( ).setProperty( propName, propValue );
		}
	}

	/**
	 * Conversts <code>props</code> from ODA <code>Properties</code> to
	 * List.
	 * 
	 * @param props
	 *            ODA property values.
	 * @return a new <code>List</code> object.
	 */

	private List newROMPrivateProperties( Properties props )
	{
		if ( props == null )
			return null;

		List list = new ArrayList( );
		EList designProps = props.getProperties( );
		for ( int i = 0; i < designProps.size( ); i++ )
		{
			Property prop = (Property) designProps.get( i );
			ExtendedProperty extendedProperty = StructureFactory
					.createExtendedProperty( );
			extendedProperty.setName( prop.getName( ) );
			extendedProperty.setValue( prop.getValue( ) );

			list.add( extendedProperty );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateDataSetHandle(org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 *      org.eclipse.birt.report.model.api.OdaDataSetHandle, boolean)
	 */

	public void updateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, boolean isSourceChanged )
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
					setHandle.setPrivateDriverProperty( prop.getName( ), prop
							.getValue( ) );
				}
			}

			DesignValues designerValues = null;

			try
			{
				designerValues = SerializerImpl.instance( ).read(
						setHandle.getDesignerValues( ) );
			}
			catch ( IOException e )
			{
			}

			ResultSets cachedResultSets = designerValues == null
					? null
					: designerValues.getResultSets( );

			ResultSetDefinition cachedResultDefn = null;

			if ( cachedResultSets != null
					&& !cachedResultSets.getResultSetDefinitions( ).isEmpty( ) )
				cachedResultDefn = (ResultSetDefinition) cachedResultSets
						.getResultSetDefinitions( ).get( 0 );

			updateROMResultSets( setHandle, new ResultSetsAdapter( )
					.newROMResultSets( setDesign, setHandle, cachedResultDefn ) );

			setHandle.setResultSetName( setDesign.getPrimaryResultSetName( ) );

			setHandle.setQueryText( setDesign.getQueryText( ) );

			// designer values must be saved after convert data set parameters
			// and result set columns.

			// Set Parameter

			// Get user-defined parameters

			List userDefinedList = new DataSetParameterAdapter( )
					.getUserDefinedParameter( designerValues, setDesign,
							setHandle );

			// Update parameters of dataset handle.

			updateROMDataSetParams( setDesign, setHandle,
					designerValues == null ? null : designerValues
							.getDataSetParameters( ), userDefinedList );

			DataSourceDesign sourceDesign = setDesign.getDataSourceDesign( );
			if ( sourceDesign != null )
			{
				OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle
						.getDataSource( );

				// only the local data source can be used.

				if ( isSourceChanged && sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( ) )
				{
					setHandle.setDataSource( sourceDesign.getName( ) );
					updateDataSourceHandle( sourceDesign, sourceHandle );
				}

				// if the source is not changed, and it is not in the included
				// library, then we can update it.

				if ( !isSourceChanged
						&& sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( )
						&& !( isEqualDataSourceDesign(
								createDataSourceDesign( sourceHandle ),
								sourceDesign ) ) )
				{
					updateDataSourceHandle( sourceDesign, sourceHandle );
				}
			}
			else
				setHandle.setDataSource( null );

			updateDesignerValue( setDesign, setHandle, designerValues,
					userDefinedList );
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
			OdaDataSetHandle setHandle, DesignValues designerValues,
			List userDefinedList ) throws SemanticException
	{

		// update designvalues.

		if ( setDesign.getParameters( ) == null )
		{
			if ( designerValues != null )
				designerValues.setDataSetParameters( null );
		}
		else
		{
			EList designDefns = setDesign.getParameters( )
					.getParameterDefinitions( );

			List resultList = DataSetParameterAdapter
					.getDriverDefinedParameters( designDefns, userDefinedList );

			if ( resultList.size( ) > 0 )
			{
				if ( designerValues == null )
				{
					designerValues = ModelFactory.eINSTANCE
							.createDesignValues( );
				}
				DataSetParameters dsParams = designerValues
						.getDataSetParameters( );
				if ( dsParams == null )
				{
					dsParams = ODADesignFactory.getFactory( )
							.createDataSetParameters( );
					designerValues.setDataSetParameters( dsParams );
				}
				dsParams = designerValues.getDataSetParameters( );
				dsParams.getParameterDefinitions( ).clear( );
				dsParams.getParameterDefinitions( ).addAll( resultList );
			}
		}

		// Set DesignerValues

		try
		{
			if ( designerValues != null )
			{
				String dValue = SerializerImpl.instance( ).write(
						designerValues );
				setHandle.setDesignerValues( dValue );
			}
		}
		catch ( IOException e )
		{
		}
	}

	/**
	 * Overrides data set design's parameters with data set handle's. Totally
	 * override.
	 * 
	 * @param setDesign
	 *            data set design.
	 * @param setHandle
	 *            data set handle.
	 * @throws SemanticException
	 */

	void updateDataSetDesignParams( DataSetDesign setDesign,
			OdaDataSetHandle setHandle )
	{
		DataSetParameters dsParams = new DataSetParameterAdapter( )
				.newOdaDataSetParams( setHandle.parametersIterator( ), null,
						setDesign );

		if ( dsParams != null )
		{
			setDesign.setParameters( dsParams );
		}
		else
		{
			setDesign.setParameters( null );
		}

	}

	/**
	 * Updates a strucutre list with the corresponding property handle.
	 * 
	 * @param propHandle
	 *            the property handle
	 * @param structList
	 *            the structure list
	 * @throws SemanticException
	 *             if any strucutre has invalid value.
	 */

	private void updateROMResultSets( OdaDataSetHandle setHandle,
			List structList ) throws SemanticException
	{
		List columns = new ArrayList( );
		List hints = new ArrayList( );

		ResultSetColumnInfo.updateResultSetColumnList( structList, columns,
				hints );

		PropertyHandle propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP );

		propHandle.setValue( new ArrayList( ) );

		if ( !columns.isEmpty( ) )
		{
			for ( int i = 0; i < columns.size( ); i++ )
				propHandle.addItem( columns.get( i ) );
		}

		propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.COLUMN_HINTS_PROP );
		propHandle.setValue( new ArrayList( ) );
		if ( !hints.isEmpty( ) )
		{
			for ( int i = 0; i < hints.size( ); i++ )
			{
				ColumnHint hint = (ColumnHint) hints.get( i );
				ColumnHintHandle oldHint = ResultSetsAdapter.findColumnHint(
						(String) hint.getProperty( null,
								ColumnHint.COLUMN_NAME_MEMBER ), setHandle
								.columnHintsIterator( ) );

				if ( oldHint == null )
					propHandle.addItem( hints.get( i ) );
				else
				{
					oldHint.setDisplayName( (String) hint.getProperty( null,
							ColumnHint.DISPLAY_NAME_MEMBER ) );
					oldHint.setHelpText( (String) hint.getProperty( null,
							ColumnHint.HELP_TEXT_MEMBER ) );
					oldHint.setFormat( (String) hint.getProperty( null,
							ColumnHint.FORMAT_MEMBER ) );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#newOdaDesignerState(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DesignerState newOdaDesignerState( OdaDataSetHandle setHandle )
	{
		OdaDesignerStateHandle designerState = setHandle.getDesignerState( );

		return DesignerStateAdapter.createOdaDesignState( designerState );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateROMDesignerState(org.eclipse.datatools.connectivity.oda.design.DesignerState,
	 *      org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public void updateROMDesignerState( DesignerState designerState,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( designerState == null || setHandle == null )
			return;

		DesignerStateAdapter.updateROMDesignerState( designerState, setHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#newOdaDesignerState(org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DesignerState newOdaDesignerState( OdaDataSourceHandle sourceHandle )
	{
		OdaDesignerStateHandle designerState = sourceHandle.getDesignerState( );

		return DesignerStateAdapter.createOdaDesignState( designerState );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#updateROMDesignerState(org.eclipse.datatools.connectivity.oda.design.DesignerState,
	 *      org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public void updateROMDesignerState( DesignerState designerState,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
	{
		if ( designerState == null || sourceHandle == null )
			return;

		DesignerStateAdapter.updateROMDesignerState( designerState,
				sourceHandle );
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

	private void updateROMDataSetParams( DataSetDesign setDesign,
			OdaDataSetHandle setHandle, DataSetParameters cachedParameters,
			List userDefinedList ) throws SemanticException
	{
		List resultList = new DataSetParameterAdapter( ).newROMSetParams(
				setDesign, setHandle, cachedParameters, userDefinedList );

		// Merge all parameter list with data set handle.

		PropertyHandle propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.PARAMETERS_PROP );

		// If the name is the same , should rename it.
		// when you have three driver-defined parameter in DataSetDesign,but you
		// remove two of them
		// in handle, then when you back to 'parameter' page, you can get three
		// parameter and in this
		// time it's easy to duplicate name.

		List nameList = new ArrayList( );
		List retList = new ArrayList( );

		propHandle.clearValue( );
		Iterator iterator = resultList.iterator( );
		while ( iterator.hasNext( ) )
		{
			OdaDataSetParameter parameter = (OdaDataSetParameter) iterator
					.next( );
			String paramName = parameter.getName( );
			if ( nameList.contains( paramName ) )
			{
				paramName = IdentifierUtility.getParamUniqueName( setHandle
						.parametersIterator( ), retList, parameter
						.getPosition( ).intValue( ) );
				parameter.setName( paramName );
			}
			nameList.add( paramName );
			retList.add( parameter );
			propHandle.addItem( parameter );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#isEqualDataSourceDesign(org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 *      org.eclipse.datatools.connectivity.oda.design.DataSourceDesign)
	 */

	public boolean isEqualDataSourceDesign( DataSourceDesign designFromHandle,
			DataSourceDesign design )
	{
		if ( designFromHandle != null )
		{
			EList publicProps = designFromHandle.getPublicProperties( )
					.getProperties( );
			for ( int i = 0; i < publicProps.size( ); i++ )
			{
				Property prop = (Property) publicProps.get( i );
				String propValue = prop.getValue( );
				String propName = prop.getName( );
				if ( propValue == null )
				{
					String value = design.getPublicProperties( ).getProperty(
							propName );
					if ( value != null && value.trim( ).equals( "" ) ) //$NON-NLS-1$
						prop.setNameValue( prop.getName( ), "" ); //$NON-NLS-1$
				}
			}
		}
		return new EcoreUtil.EqualityHelper( )
				.equals( designFromHandle, design );
	}
}
