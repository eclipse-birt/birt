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

package org.eclipse.birt.report.model.plugin;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.ExtensibilityProvider;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.ODAExtensionElementDefn;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.Property;

/**
 * Provides ODA extensibility.
 */

public class OdaExtensibilityProvider extends ExtensibilityProvider
		implements
			ODAProvider
{

	/**
	 * ID of the extension which is used to extend the extendable element.
	 */

	String extensionID = null;

	/**
	 * Constructs ODA extensibility provider with the element to extend and
	 * extension ID.
	 * 
	 * @param element
	 *            the element to extend
	 * @param extensionID
	 *            the ID of the extension which provides property definition.
	 */

	public OdaExtensibilityProvider( DesignElement element, String extensionID )
	{
		super( element );

		this.extensionID = extensionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.ExtensibilityProvider#getPropertyDefns()
	 */

	public List getPropertyDefns( )
	{
		if ( getExtDefn( ) == null )
			return Collections.EMPTY_LIST;

		return getExtDefn( ).getProperties( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.ExtensibilityProvider#getPropertyDefn(java.lang.String)
	 */

	public IPropertyDefn getPropertyDefn( String propName )
	{
		if ( getExtDefn( ) == null )
			return null;

		return getExtDefn( ).getProperty( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.ExtensibilityProvider#checkExtends(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void checkExtends( DesignElement parent ) throws ExtendsException
	{
		String parentExt = (String) parent.getProperty( null,
				IOdaExtendableElementModel.EXTENSION_ID_PROP );

		assert extensionID != null;
		if ( !extensionID.equalsIgnoreCase( parentExt ) )
			throw new WrongTypeException( element, parent,
					WrongTypeException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IExtendableElement#getExtDefn()
	 */

	public ExtensionElementDefn getExtDefn( )
	{
		if ( extensionID == null )
			return null;

		if ( cachedExtDefn == null )
		{
			cachedExtDefn = new ODAExtensionElementDefn( element.getDefn( ) );

			try
			{
				Property[] properties = null;
				Properties visibilities = null;

				if ( element instanceof OdaDataSource )
				{
					ExtensionManifest manifest = ODAManifestUtil
							.getDataSourceExtension( extensionID );

					if ( manifest != null )
					{
						properties = manifest.getProperties( );
						visibilities = manifest.getPropertiesVisibility( );
					}

				}
				else if ( element instanceof OdaDataSet )
				{
					DataSetType dataSetType = ODAManifestUtil
							.getDataSetExtension( extensionID );

					if ( dataSetType != null )
					{
						properties = ODAManifestUtil.getDataSetExtension(
								extensionID ).getProperties( );
						visibilities = ODAManifestUtil.getDataSetExtension(
								extensionID ).getPropertiesVisibility( );
					}
				}

				if ( properties != null )
				{
					for ( int i = 0; i < properties.length; i++ )
					{
						ODAPropertyDefn propDefn = new ODAPropertyDefn(
								properties[i] );

						cachedExtDefn.addProperty( propDefn );
					}

					if ( visibilities != null )
					{
						for ( Iterator iter = visibilities.keySet( ).iterator( ); iter
								.hasNext( ); )
						{
							String key = (String) iter.next( );
							cachedExtDefn.addPropertyVisibility( key,
									visibilities.getProperty( key ) );
						}
					}
				}

				( (ODAExtensionElementDefn) cachedExtDefn ).buildDefinition( );
			}
			catch ( MetaDataException e )
			{
				return null;
			}
		}

		return cachedExtDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#convertDataSourceExtensionID(java.lang.String)
	 */

	public String convertDataSourceExtensionID( String extensionID )
	{
		ExtensionManifest manifest = ODAManifestUtil
				.getDataSourceExtension( extensionID );
		if ( manifest != null && manifest.isDeprecated( ) )
		{
			extensionID = manifest.getRelatedDataSourceId( );
		}
		return extensionID;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#convertDataSetExtensionID(java.lang.String)
	 */

	public String convertDataSetExtensionID( String extensionID )
	{
		DataSetType type = ODAManifestUtil.getDataSetExtension( extensionID );
		if ( type != null && type.isDeprecated( ) )
		{
			extensionID = type.getRelatedDataSetId( );
		}
		return extensionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidODADataSetExtensionID(java.lang.String)
	 */
	public boolean isValidODADataSetExtensionID( String extensionID )
	{
		if ( ODAManifestUtil.getDataSetExtension( extensionID ) != null )
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidODADataSourceExtensionID(java.lang.String)
	 */
	public boolean isValidODADataSourceExtensionID( String extensionID )
	{
		if ( ODAManifestUtil.getDataSourceExtension( extensionID ) != null )
			return true;

		return false;
	}

}