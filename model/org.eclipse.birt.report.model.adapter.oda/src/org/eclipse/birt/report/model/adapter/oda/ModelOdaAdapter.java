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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
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

		sourceDesign
				.setPublicProperties( newOdaPublicProperties( sourceHandle ) );
	}

	/**
	 * Converts ROM public properties to ODA <code>Properties</code> instance.
	 * 
	 * @param sourceHandle
	 *            the data source handle
	 * @return <code>Properties</code> containing ROM public property values.
	 */

	private Properties newOdaPublicProperties( OdaDataSourceHandle sourceHandle )
	{
		Properties retProps = DesignFactory.eINSTANCE.createProperties( );
		List propDefns = sourceHandle.getExtensionPropertyDefinitionList( );
		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			IPropertyDefn propDefn = (IPropertyDefn) propDefns.get( i );
			String propName = propDefn.getName( );
			String propValue = sourceHandle.getStringProperty( propName );
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

			List props = sourceDesign.getPublicProperties( ).getProperties( );
			for ( int i = 0; i < props.size( ); i++ )
			{
				Property prop = (Property) props.get( i );
				sourceHandle.setProperty( prop.getName( ), prop.getValue( ) );
			}

			// set private properties.

			props = sourceDesign.getPrivateProperties( ).getProperties( );

			for ( int i = 0; i < props.size( ); i++ )
			{
				Property prop = (Property) props.get( i );
				sourceHandle.setPrivateDriverProperty( prop.getName( ), prop
						.getValue( ) );
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
	}

	/**
	 * Converts ODA <code>Properties</code> to ROM public properties.
	 * 
	 * @param sourceHandle
	 *            the data source handle
	 */

	private void updateROMPublicProperties( Properties designProps,
			OdaDataSourceHandle sourceHandle ) throws SemanticException
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
}
