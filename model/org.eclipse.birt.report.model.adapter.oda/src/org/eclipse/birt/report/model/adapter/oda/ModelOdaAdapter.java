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

package org.eclipse.birt.report.model.adapter.oda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.util.SerializerImpl;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.PropertyAttributes;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.common.util.EList;

/**
 * An adapter class that converts between ROM OdaDataSourceHandle and ODA
 * DataSourceDesign.
 * 
 * @see OdaDataSourceHandle
 * @see DataSourceDesign
 */

public class ModelOdaAdapter
{

	/**
	 * Adapts the specified Model OdaDataSourceHandle to a Data Engine API
	 * DataSourceDesign object.
	 * 
	 * @param sourceHandle
	 *            the Model handle
	 * @return a new <code>DataSourceDesign</code>
	 */

	public DataSourceDesign createDataSourceDesign(
			OdaDataSourceHandle sourceHandle )
	{
		if ( sourceHandle == null )
			return null;

		DataSourceDesign sourceDesign = DesignFactory.eINSTANCE
				.createDataSourceDesign( );
		updateDataSourceDesign( sourceHandle, sourceDesign );
		return sourceDesign;
	}

	/**
	 * Adapts the specified Model OdaDataSetHandle to a Data Engine API
	 * DataSetDesign object.
	 * 
	 * @param setHandle
	 *            the Model handle
	 * @return a new <code>DataSetDesign</code>
	 */

	public DataSetDesign createDataSetDesign( OdaDataSetHandle setHandle )
	{
		if ( setHandle == null )
			return null;

		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign( );
		updateDataSetDesign( setHandle, setDesign );
		return setDesign;
	}

	/**
	 * Adapts the Data Engine API DataSetDesign object to the specified Model
	 * OdaDataSetHandle.
	 * 
	 * @param setDesign
	 *            the ODA dataSet design.
	 * @param module
	 *            the module where the Model handle resides.
	 * @return a new <code>OdaDataSourceHandle</code>
	 * @throws SemanticException
	 *             if any value in <code>sourceDesign</code> is invalid
	 *             according ROM.
	 * @throws IllegalStateException
	 *             if <code>setDesign</code> is not valid.
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
		String dataSourceName = sourceDesign.getName( );

		if ( sourceDesign != null )
			setHandle.getElement( )
					.setProperty(
							OdaDataSetHandle.DATA_SOURCE_PROP,
							PropertyValueValidationUtil.validateProperty(
									setHandle,
									OdaDataSetHandle.DATA_SOURCE_PROP,
									dataSourceName ) );
		else
			setHandle.getElement( ).clearProperty(
					OdaDataSetHandle.DATA_SOURCE_PROP );

		// set the data set parameter list.

		setHandle.getElement( )
				.clearProperty( OdaDataSetHandle.PARAMETERS_PROP );

		List dataSetParams = new DataSetParameterAdapter( ).newROMSetParams(
				setDesign, setHandle, null );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.PARAMETERS_PROP, dataSetParams );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.PARAMETERS_PROP,
				dataSetParams );

		// set the result sets

		List resultRetColumns = ResultSetsAdapter.newROMResultSets( setDesign
				.getPrimaryResultSet( ), null, setDesign
				.getOdaExtensionDataSourceId( ), setDesign
				.getOdaExtensionDataSetId( ) );
		if ( resultRetColumns == null )
		{
			ResultSets sets = setDesign.getResultSets( );
			if ( sets != null && !sets.getResultSetDefinitions( ).isEmpty( ) )
				resultRetColumns = ResultSetsAdapter.newROMResultSets(
						(ResultSetDefinition) sets.getResultSetDefinitions( )
								.get( 0 ), null, setDesign
								.getOdaExtensionDataSourceId( ), setDesign
								.getOdaExtensionDataSetId( ) );
		}

		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.RESULT_SET_PROP, resultRetColumns );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.RESULT_SET_PROP,
				resultRetColumns );

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

	private String serializeOdaValues( DataSetDesign setDesign )
	{
		DataSetParameters params = setDesign.getParameters( );
		ResultSets resultSets = setDesign.getResultSets( );

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setVersion( IConstants.DESINGER_VALUES_VERSION );
		boolean hasData = false;
		if ( params != null )
		{
			values.setDataSetParameters( params );
			hasData = true;
		}

		if ( resultSets != null )
		{
			values.setResultSets( resultSets );
			hasData = true;
		}

		if ( !hasData )
			return IConstants.EMPTY_STRING;

		ByteArrayOutputStream bos = new ByteArrayOutputStream( );

		String retString = IConstants.EMPTY_STRING;
		try
		{
			SerializerImpl.instance( ).write( values, bos );
			retString = bos.toString( IConstants.CHAR_ENCODING );
			bos.close( );
		}
		catch ( UnsupportedEncodingException e )
		{
		}
		catch ( IOException e )
		{
		}

		return retString;
	}

	/**
	 * Return DesignValues that is de-serialized from the given string.
	 * 
	 * @param value
	 *            the input string
	 * @return the DesignValues instance
	 */

	private DesignValues deserializeOdaValues( String value )
	{
		if ( value == null )
			return null;

		byte[] rawData = null;

		try
		{
			rawData = value.getBytes( IConstants.CHAR_ENCODING );
		}
		catch ( UnsupportedEncodingException e )
		{
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream( rawData );

		try
		{
			return SerializerImpl.instance( ).fromXml( bis, false );
		}
		catch ( IOException e )
		{
			return null;
		}
	}

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param setHandle
	 *            the Model handle
	 * @param setDesign
	 *            the ODA data source design
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

		setDesign.setParameters( new DataSetParameterAdapter( )
				.newOdaDataSetParams( setHandle.parametersIterator( ) ) );

		setDesign.setPrimaryResultSet( ResultSetsAdapter
				.newOdaResultSetDefinition( setHandle ) );
	}

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param setHandle
	 *            the Model handle
	 * @param setDesign
	 *            the ODA data source design
	 * @param propertyName
	 *            the property name
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
			setDesign.setParameters( new DataSetParameterAdapter( )
					.newOdaDataSetParams( setHandle.parametersIterator( ) ) );

		else if ( OdaDataSetHandle.RESULT_SET_PROP
				.equalsIgnoreCase( propertyName ) )
			setDesign.setPrimaryResultSet( ResultSetsAdapter
					.newOdaResultSetDefinition( setHandle ) );
	}

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param sourceHandle
	 *            the Model handle
	 * @param sourceDesign
	 *            the ODA data source design
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
				retProps = DesignFactory.eINSTANCE.createProperties( );
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

		Properties retProps = DesignFactory.eINSTANCE.createProperties( );
		for ( ; props.hasNext( ); )
		{
			ExtendedPropertyHandle propHandle = (ExtendedPropertyHandle) props
					.next( );
			retProps
					.setProperty( propHandle.getName( ), propHandle.getValue( ) );
		}

		return retProps;
	}

	/**
	 * Adapts the Data Engine API DataSourceDesign object to the specified Model
	 * OdaDataSourceHandle.
	 * 
	 * @param sourceDesign
	 *            the ODA dataSource design.
	 * @param module
	 *            the module where the Model handle resides.
	 * @return a new <code>OdaDataSourceHandle</code>
	 * @throws SemanticException
	 *             if any value in <code>sourceDesign</code> is invalid
	 *             according ROM.
	 * @throws IllegalStateException
	 *             if <code>sourceDesign</code> is not valid.
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

	/**
	 * Updates values of <code>sourceHandle</code> with the given
	 * <code>sourceDesign</code>.
	 * 
	 * @param sourceDesign
	 *            the ODA data source design
	 * @param sourceHandle
	 *            the Model handle
	 * @throws SemanticException
	 *             if any of <code>sourceDesign</code> property values is not
	 *             valid.
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

	/**
	 * Copies values in <code>sourceHandle</code> to Oda properties.
	 * 
	 * @param props
	 *            the ODA public properties
	 * @param sourceHandle
	 *            the Model ODA DataSourceHandle
	 */

	protected void updateOdaPublicProperties( Properties props,
			OdaDataSourceHandle sourceHandle )
	{
		List propDefns = sourceHandle.getExtensionPropertyDefinitionList( );

		// finds out the property bindings for this data source. So that
		// values of report parameters can be copied to PropertyAttributes of
		// Properties.

		List propBindings = getPropertyBindings( sourceHandle.getID( ),
				sourceHandle.getModuleHandle( ) );

		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			IPropertyDefn propDefn = (IPropertyDefn) propDefns.get( i );
			String propName = propDefn.getName( );

			Property property = props.findProperty( propName );
			PropertyBinding propBinding = findPropertyBinding( propName,
					propBindings );

			if ( propBinding == null )
				continue;

			// synchronize data from propBinding to ODA property

			// String paramName = propBinding.getValue( );
			//
			// if ( StringUtil.isBlank( paramName ) )
			// continue;

			// finds out the parameter with the given paramName

			// ScalarParameterHandle paramHandle = (ScalarParameterHandle)
			// sourceHandle
			// .getModuleHandle( ).findParameter( paramName );
			//
			// property.getDesignAttributes( ).setElementAttributes(
			// new ReportParameterAdapter( )
			// .newInputElementAttributes( paramHandle ) );
		}
	}

	/**
	 * Finds the PropertyBinding with the given property name and binding list.
	 * 
	 * @param propName
	 *            the property name
	 * @param propBindings
	 *            a list containing property bindings.
	 * @return the found PropertyBinding
	 */

	private PropertyBinding findPropertyBinding( String propName,
			List propBindings )
	{
		if ( propBindings == null || propBindings.isEmpty( ) )
			return null;

		for ( int i = 0; i < propBindings.size( ); i++ )
		{
			PropertyBinding propBinding = (PropertyBinding) propBindings
					.get( i );
			String tmpPropName = propBinding.getName( );
			if ( tmpPropName != null && tmpPropName.equals( propName ) )
				return propBinding;
		}

		return null;
	}

	/**
	 * Returns a list containing property bindings that are bound to a specified
	 * element id.
	 * 
	 * @param id
	 *            the element id
	 * @param module
	 *            the ROM module
	 * @return a list containing property bindings
	 */

	private List getPropertyBindings( long id, ModuleHandle module )
	{
		List bindingList = module
				.getListProperty( ModuleHandle.PROPERTY_BINDINGS_PROP );
		if ( bindingList == null || bindingList.isEmpty( ) )
			return Collections.EMPTY_LIST;

		List retList = new ArrayList( );
		for ( int i = 0; i < bindingList.size( ); i++ )
		{
			PropertyBinding propBinding = (PropertyBinding) bindingList.get( i );
			long elementId = propBinding.getID( ).longValue( );
			if ( id == elementId )
				retList.add( propBinding );
		}

		return bindingList;
	}

	/**
	 * Updates property bindings and report parameters in the report design with
	 * the given ODA properties.
	 * 
	 * @param props
	 *            the ODA public properties.
	 * @param sourceHandle
	 *            the ROM element
	 * @throws SemanticException
	 *             if any value are not valid.
	 */

	protected void updateROMPropertyBindings( Properties props,
			ReportElementHandle sourceHandle ) throws SemanticException
	{
		// clear all property bindings for the OdaDataSource

		clearPropertyBindings( sourceHandle );

		EList propList = props.getProperties( );
		for ( int i = 0; i < propList.size( ); i++ )
		{
			Property prop = (Property) propList.get( i );
			updateROMODAProperty( prop, sourceHandle );
		}
	}

	/**
	 * Clears property bindings for the given <code>sourceHandle</code>.
	 * 
	 * @param sourceHandle
	 *            the DataSource element
	 */

	private void clearPropertyBindings( ReportElementHandle sourceHandle )
			throws PropertyValueException
	{
		List bindings = getPropertyBindings( sourceHandle.getID( ),
				sourceHandle.getModuleHandle( ) );

		PropertyHandle propHandle = sourceHandle.getModuleHandle( )
				.getPropertyHandle( ModuleHandle.PROPERTY_BINDINGS_PROP );
		propHandle.removeItems( bindings );
	}

	/**
	 * Updates property bindings and report paramters with the given ODA
	 * property.
	 * 
	 * @param prop
	 *            the ODA property
	 * @param sourceHandle
	 *            the ROM data source
	 * @throws SemanticException
	 *             if values in <code>prop</code> are invalid in ROM.
	 */

	private void updateROMODAProperty( Property prop,
			ReportElementHandle sourceHandle ) throws SemanticException
	{
		String name = prop.getName( );

		// add the property binding first.

		PropertyAttributes propAttrs = prop.getDesignAttributes( );
		if ( propAttrs == null )
			return;

		InputElementAttributes inputAttrs = propAttrs.getElementAttributes( );

		// TODO do we find property binding in the design?

		List propBindings = getPropertyBindings( sourceHandle.getID( ),
				sourceHandle.getModuleHandle( ) );

		PropertyBinding propBinding = findPropertyBinding( name, propBindings );
		String paramName = propBinding.getValue( );

		if ( paramName == null )
			return;

		ScalarParameterHandle param = (ScalarParameterHandle) sourceHandle
				.getModuleHandle( ).findParameter( paramName );

		// convert Oda input element attributes to property bindings and
		// scalar parameter.

		// new ReportParameterAdapter( ).updateReportParameter( param,
		// inputAttrs );

	}

	/**
	 * Updates values of <code>sourceHandle</code> with the given
	 * <code>sourceDesign</code>.
	 * 
	 * @param setDesign
	 *            the ODA data source design
	 * @param setHandle
	 *            the Model handle
	 * @param isSourceChanged
	 *            <code>true</code> if the data source of the given design has
	 *            been changed. Otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if any of <code>sourceDesign</code> property values is not
	 *             valid.
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

			// updateROMPropertyBindings( props, setHandle );

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
				e.printStackTrace( );
			}

			updateROMDataSetParamList( setHandle, new DataSetParameterAdapter( )
					.newROMSetParams( setDesign, setHandle,
							designerValues == null ? null : designerValues
									.getDataSetParameters( ) ) );

			ResultSetDefinition resultDefn = setDesign.getPrimaryResultSet( );
			if ( resultDefn == null )
			{
				ResultSets resultSets = setDesign.getResultSets( );
				if ( resultSets != null
						&& !resultSets.getResultSetDefinitions( ).isEmpty( ) )
					resultDefn = (ResultSetDefinition) resultSets
							.getResultSetDefinitions( ).get( 0 );
			}

			DesignValues values = null;
			try
			{
				values = SerializerImpl.instance( ).read(
						setHandle.getDesignerValues( ) );
			}
			catch ( IOException e )
			{

			}

			ResultSets cachedResultSets = values == null ? null : values
					.getResultSets( );

			ResultSetDefinition cachedResultDefn = null;
			
			if ( cachedResultSets != null
					&& !cachedResultSets.getResultSetDefinitions( ).isEmpty( ) )
				cachedResultDefn = (ResultSetDefinition) cachedResultSets
						.getResultSetDefinitions( ).get( 0 );

			updateROMStructureList( setHandle
					.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP ),
					ResultSetsAdapter.newROMResultSets( resultDefn,
							cachedResultDefn, setDesign
									.getOdaExtensionDataSourceId( ), setDesign
									.getOdaExtensionDataSetId( ) ) );

			setHandle.setResultSetName( setDesign.getPrimaryResultSetName( ) );

			setHandle.setQueryText( setDesign.getQueryText( ) );

			// designer values must be saved after convert data set parameters
			// and result set columns.

			String odaValues = serializeOdaValues( setDesign );
			setHandle.setDesignerValues( odaValues );

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

				if ( !isSourceChanged && sourceHandle != null
						&& !sourceHandle.getModuleHandle( ).isReadOnly( ) )
				{
					updateDataSourceHandle( sourceDesign, sourceHandle );
				}
			}
			else
				setHandle.setDataSource( null );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
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

	private void updateROMStructureList( PropertyHandle propHandle,
			List structList ) throws SemanticException
	{
		assert propHandle != null;

		propHandle.setValue( null );

		if ( structList == null || structList.isEmpty( ) )
			return;

		for ( int i = 0; i < structList.size( ); i++ )
			propHandle.addItem( structList.get( i ) );
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

	private void updateROMDataSetParamList( OdaDataSetHandle setHandle,
			List structList ) throws SemanticException
	{
		setHandle.setProperty( OdaDataSetHandle.PARAMETERS_PROP, null );

		if ( structList == null || structList.isEmpty( ) )
			return;

		PropertyHandle propHandle = setHandle
				.getPropertyHandle( OdaDataSetHandle.PARAMETERS_PROP );

		for ( int i = 0; i < structList.size( ); i++ )
			propHandle.addItem( structList.get( i ) );
	}

	/**
	 * Creates a ODA DesignerState object with the given OdaDataSet.
	 * 
	 * @param setHandle
	 *            the ODA DataSet.
	 * @return the oda DesignerState object.
	 */

	public DesignerState newOdaDesignerState( OdaDataSetHandle setHandle )
	{
		OdaDesignerStateHandle designerState = setHandle.getDesignerState( );

		return DesignerStateAdapter.createOdaDesignState( designerState );
	}

	/**
	 * Creates a ROM DesignerState object with the given ODA DataSet design.
	 * 
	 * @param designerState
	 *            the ODA designer state.
	 * @param setHandle
	 *            the ODA DataSet.
	 * @throws SemanticException
	 *             if ROM Designer state value is locked.
	 */

	public void updateROMDesignerState( DesignerState designerState,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( designerState == null || setHandle == null )
			return;

		DesignerStateAdapter.updateROMDesignerState( designerState, setHandle );
	}

	/**
	 * Creates a ODA DesignerState object with the given OdaDataSource.
	 * 
	 * @param sourceHandle
	 *            the ODA DataSource.
	 * @return the oda DesignerState object.
	 */

	public DesignerState newOdaDesignerState( OdaDataSourceHandle sourceHandle )
	{
		OdaDesignerStateHandle designerState = sourceHandle.getDesignerState( );

		return DesignerStateAdapter.createOdaDesignState( designerState );
	}

	/**
	 * Creates a ROM DesignerState object with the given ODA DataSet design.
	 * 
	 * @param designerState
	 *            the ODA designer state.
	 * @param sourceHandle
	 *            the ODA DataSource.
	 * @throws SemanticException
	 *             if ROM Designer state value is locked.
	 */

	public void updateROMDesignerState( DesignerState designerState,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
	{
		if ( designerState == null || sourceHandle == null )
			return;

		DesignerStateAdapter.updateROMDesignerState( designerState,
				sourceHandle );
	}
}
