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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.PropertyAttributes;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
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
		adaptDataSourceHandle( sourceHandle, sourceDesign );
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
		adaptDataSetHandle( setHandle, setDesign );
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

		// udpate property bindings, report parameters and so on.

		// updateROMPropertyBindings( setDesign.getPublicProperties( ),
		// setHandle );

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
		List dataSetParams = newROMSetParams( setDesign.getParameters( ) );
		PropertyValueValidationUtil.validateProperty( setHandle,
				OdaDataSetHandle.PARAMETERS_PROP, dataSetParams );
		setHandle.getElement( ).setProperty( OdaDataSetHandle.PARAMETERS_PROP,
				dataSetParams );

		// set the result sets

		List resultRetColumns = newROMResultSets( setDesign
				.getPrimaryResultSet( ) );
		if ( resultRetColumns == null )
		{
			ResultSets sets = setDesign.getResultSets( );
			if ( sets != null && !sets.getResultSetDefinitions( ).isEmpty( ) )
				resultRetColumns = newROMResultSets( (ResultSetDefinition) sets
						.getResultSetDefinitions( ).get( 0 ) );

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
	}

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param sourceHandle
	 *            the Model handle
	 * @param sourceDesign
	 *            the ODA data source design
	 */

	private void adaptDataSetHandle( OdaDataSetHandle setHandle,
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

		setDesign.setParameters( newOdaDataSetParams( setHandle
				.parametersIterator( ) ) );

		setDesign.setPrimaryResultSet( newOdaResultSetDefinition( setHandle ) );
	}

	/**
	 * Creates ODA data set parameters with given ROM data set parameters.
	 * 
	 * @param romSetParams
	 *            ROM defined data set parameters.
	 * @return the created ODA data set parameters.
	 * 
	 */

	private DataSetParameters newOdaDataSetParams( Iterator romSetParams )
	{
		if ( !romSetParams.hasNext( ) )
			return null;

		DataSetParameters odaSetParams = DesignFactory.eINSTANCE
				.createDataSetParameters( );

		DataSetParameterAdapter setParamAdapter = new DataSetParameterAdapter( );
		while ( romSetParams.hasNext( ) )
		{
			DataSetParameterHandle paramDefn = (DataSetParameterHandle) romSetParams
					.next( );
			ParameterDefinition odaParamDefn = setParamAdapter
					.newParameterDefinition( paramDefn );

			odaSetParams.getParameterDefinitions( ).add( odaParamDefn );
		}

		return odaSetParams;
	}

	/**
	 * Copies values of <code>sourceHandle</code> to <code>sourceDesign</code>.
	 * 
	 * @param sourceHandle
	 *            the Model handle
	 * @param sourceDesign
	 *            the ODA data source design
	 */

	private void adaptDataSourceHandle( OdaDataSourceHandle sourceHandle,
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
	 * Creates a ResultSetDefinition with the given ROM ResultSet columns.
	 * 
	 * @param romResultSet
	 *            the ROM result set columns.
	 * @return the created ResultSetDefinition
	 */

	private ResultSetDefinition newOdaResultSetDefinition(
			OdaDataSetHandle setHandle )
	{
		Iterator romSets = setHandle.resultSetIterator( );
		String name = setHandle.getResultSetName( );

		if ( !romSets.hasNext( ) && StringUtil.isBlank( name ) )
			return null;

		ResultSetDefinition odaSetDefn = null;
		ResultSetColumns odaSetColumns = null;

		if ( !StringUtil.isBlank( name ) )
		{
			odaSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition( );
			odaSetDefn.setName( name );
		}

		while ( romSets.hasNext( ) )
		{
			if ( odaSetDefn == null )
				odaSetDefn = DesignFactory.eINSTANCE
						.createResultSetDefinition( );

			if ( odaSetColumns == null )
				odaSetColumns = DesignFactory.eINSTANCE
						.createResultSetColumns( );

			ResultSetColumnHandle setColumn = (ResultSetColumnHandle) romSets
					.next( );

			ColumnDefinition columnDefn = DesignFactory.eINSTANCE
					.createColumnDefinition( );

			DataElementAttributes dataAttrs = DesignFactory.eINSTANCE
					.createDataElementAttributes( );
			dataAttrs.setName( setColumn.getColumnName( ) );
			dataAttrs.setPosition( setColumn.getPosition( ).intValue( ) );

			// TODO set the data type

			columnDefn.setAttributes( dataAttrs );
			odaSetColumns.getResultColumnDefinitions( ).add( columnDefn );
		}

		odaSetDefn.setResultSetColumns( odaSetColumns );
		return odaSetDefn;
	}

	/**
	 * Creates a list containing ROM ResultSetColumn according to given ODA
	 * ResultSets.
	 * 
	 * @param setDefn
	 *            the ODA result set.
	 * @return a list containing ROM ResultSetColumn.
	 */

	private List newROMResultSets( ResultSetDefinition setDefn )
	{
		if ( setDefn == null )
			return null;

		List retList = new ArrayList( );

		ResultSetColumns setColumns = setDefn.getResultSetColumns( );
		retList.addAll( newROMResultSetColumns( setColumns ) );

		return retList;
	}

	/**
	 * Creates a list containing ROM ResultSetColumn according to given ODA
	 * ResultSetColumns.
	 * 
	 * @param setColumns
	 *            the ODA result set columns
	 * @return a list containing ROM ResultSetColumn.
	 */

	private List newROMResultSetColumns( ResultSetColumns setColumns )
	{
		if ( setColumns == null )
			return null;

		EList odaSetColumns = setColumns.getResultColumnDefinitions( );
		if ( odaSetColumns.isEmpty( ) )
			return null;

		List retList = new ArrayList( );

		for ( int i = 0; i < odaSetColumns.size( ); i++ )
		{

			ColumnDefinition columnDefn = (ColumnDefinition) odaSetColumns
					.get( i );

			// what's this?

			DataElementAttributes dataAttrs = columnDefn.getAttributes( );
			ResultSetColumn newColumn = StructureFactory
					.createResultSetColumn( );

			newColumn.setColumnName( dataAttrs.getName( ) );
			newColumn.setPosition( new Integer( dataAttrs.getPosition( ) ) );

			retList.add( newColumn );

			// TODO data type code.

			// maps the native type code to model type string.
			// newColumn.setDataType( dataAttrs.getNativeDataTypeCode( ) );
		}

		return retList;
	}

	/**
	 * Creates a list containing <code>DataSetParameter</code> with the given
	 * ODA data set parameter definition.
	 * 
	 * @param odaSetParams
	 *            ODA data set parameter definition
	 * @return a list containing <code>DataSetParameter</code>.
	 */

	private List newROMSetParams( DataSetParameters odaSetParams )
	{
		if ( odaSetParams == null )
			return null;

		EList odaParams = odaSetParams.getParameterDefinitions( );
		if ( odaParams == null || odaParams.isEmpty( ) )
			return null;

		List retList = new ArrayList( );
		DataSetParameterAdapter setParamAdapter = new DataSetParameterAdapter( );
		for ( int i = 0; i < odaParams.size( ); i++ )
		{
			ParameterDefinition odaParamDefn = (ParameterDefinition) odaParams
					.get( i );
			DataSetParameter setParam = setParamAdapter
					.newROMDataSetParameter( odaParamDefn );
			retList.add( setParam );
		}
		return retList;
	}

	/**
	 * Updates values of <code>sourceHandle</code> with the given
	 * <code>sourceDesign</code>.
	 * 
	 * @param setDesign
	 *            the ODA data source design
	 * @param setHandle
	 *            the Model handle
	 * @throws SemanticException
	 *             if any of <code>sourceDesign</code> property values is not
	 *             valid.
	 */

	public void updateDataSetHandle( DataSetDesign setDesign,
			OdaDataSetHandle setHandle ) throws SemanticException
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

			updateROMStructureList( setHandle
					.getPropertyHandle( OdaDataSetHandle.PARAMETERS_PROP ),
					newROMSetParams( setDesign.getParameters( ) ) );

			ResultSetDefinition resultDefn = setDesign.getPrimaryResultSet( );
			if ( resultDefn == null )
			{
				ResultSets resultSets = setDesign.getResultSets( );
				if ( resultSets != null
						&& !resultSets.getResultSetDefinitions( ).isEmpty( ) )
					resultDefn = (ResultSetDefinition) resultSets
							.getResultSetDefinitions( ).get( 0 );
			}

			updateROMStructureList( setHandle
					.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP ),
					newROMResultSets( resultDefn ) );

			setHandle.setResultSetName( setDesign.getPrimaryResultSetName( ) );

			setHandle.setQueryText( setDesign.getQueryText( ) );

			DataSourceDesign sourceDesign = setDesign.getDataSourceDesign( );
			if ( sourceDesign != null )
			{
				setHandle.setDataSource( sourceDesign.getName( ) );
				updateDataSourceHandle( sourceDesign,
						(OdaDataSourceHandle) setHandle.getDataSource( ) );
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

		propHandle.clearValue( );

		if ( structList == null || structList.isEmpty( ) )
			return;

		for ( int i = 0; i < structList.size( ); i++ )
			propHandle.addItem( structList.get( i ) );
	}
}
