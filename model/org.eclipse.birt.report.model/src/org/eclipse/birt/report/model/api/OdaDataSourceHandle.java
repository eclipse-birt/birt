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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents a extended data source.
 * 
 * @see org.eclipse.birt.report.model.elements.OdaDataSource
 */

public class OdaDataSourceHandle extends DataSourceHandle
{

	/**
	 * Constructs an extended data source handle with the given design and the
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public OdaDataSourceHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Sets the driver name.
	 * 
	 * @param driverName
	 *            the name to set
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setDriverName( String driverName ) throws SemanticException
	{
		setProperty( OdaDataSource.DRIVER_NAME_PROP, driverName );
	}

	/**
	 * Returns the driver name.
	 * 
	 * @return the driver name
	 */

	public String getDriverName( )
	{
		return getStringProperty( OdaDataSource.DRIVER_NAME_PROP );
	}

	/**
	 * Returns the iterator for the public driver property list. The item over
	 * the iterator is the instance of <code>ExtendedPropertyHandle</code>.
	 * 
	 * @return the iterator over public driver property list defined on this
	 *         data source.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty
	 */

	public Iterator publicDriverPropertiesIterator( )
	{
		PropertyHandle propertyHandle = getPropertyHandle( OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP );
		assert propertyHandle != null;

		return propertyHandle.iterator( );
	}

	/**
	 * Returns the iterator for the private driver property list. The item over
	 * the iterator is the instance of <code>ExtendedPropertyHandle</code>.
	 * 
	 * @return the iterator over private driver property list defined on this
	 *         data source.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty
	 */

	public Iterator privateDriverPropertiesIterator( )
	{
		PropertyHandle propertyHandle = getPropertyHandle( OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP );
		assert propertyHandle != null;

		return propertyHandle.iterator( );
	}

	/**
	 * Returns a public driver property value with the given property name.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * 
	 * @return a public driver property value
	 */

	public String getPublicDriverProperty( String name )
	{
		return ExtendedPropertyHelper.getExtendedProperty( this,
				OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP, name );
	}

	/**
	 * Sets a public driver property value with the given name and value. If the
	 * property does not exist, it will be added into the property list. If the
	 * property already exists, the value will be overwritten.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * @param value
	 *            the value of a public driver property
	 * 
	 * @throws SemanticException
	 *             if <code>name</code> is <code>null</code> or an empty
	 *             string after trimming.
	 */

	public void setPublicDriverProperty( String name, String value )
			throws SemanticException
	{
		ExtendedPropertyHelper.setExtendedProperty( this,
				OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP, name, value );
	}

	/**
	 * Returns a private driver property value with the given property name.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * 
	 * @return a public driver property value
	 */

	public String getPrivateDriverProperty( String name )
	{
		return ExtendedPropertyHelper.getExtendedProperty( this,
				OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP, name );
	}

	/**
	 * Sets a private driver property value with the given name and value. If
	 * the property does not exist, it will be added into the property list. If
	 * the property already exists, the value will be overwritten.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * @param value
	 *            the value of a public driver property
	 * 
	 * @throws SemanticException
	 *             if <code>name</code> is <code>null</code> or an empty
	 *             string after trimming.
	 */

	public void setPrivateDriverProperty( String name, String value )
			throws SemanticException
	{
		ExtendedPropertyHelper.setExtendedProperty( this,
				OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP, name, value );
	}
}