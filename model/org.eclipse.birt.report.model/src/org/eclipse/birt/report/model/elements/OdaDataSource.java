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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Represents an extended data source.
 */

public class OdaDataSource extends DataSource
{

	/**
	 * The property name of the name of a driver.
	 */

	public static final String DRIVER_NAME_PROP = "driverName"; //$NON-NLS-1$

	/**
	 * The property name of public driver properties.
	 */

	public static final String PUBLIC_DRIVER_PROPERTIES_PROP = "publicDriverProperties"; //$NON-NLS-1$

	/**
	 * The property name of private driver properties.
	 */

	public static final String PRIVATE_DRIVER_PROPERTIES_PROP = "privateDriverProperties"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public OdaDataSource( )
	{
		super( );
	}

	/**
	 * Constructs an extended data source with name.
	 * 
	 * @param theName
	 *            the name of this extended data source
	 */

	public OdaDataSource( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */
	public void apply( ElementVisitor visitor )
	{
		visitor.visitOdaDataSource( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.ODA_DATA_SOURCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */
	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element
	 */

	public OdaDataSourceHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new OdaDataSourceHandle( design, this );
		}
		return (OdaDataSourceHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		String driverName = getStringProperty( design, DRIVER_NAME_PROP );
		if ( StringUtil.isBlank( driverName ) )
		{
			list.add( new PropertyValueException( this, DRIVER_NAME_PROP,
					driverName, PropertyValueException.VALUE_REQUIRED ) );
		}

		list.addAll( validateStructureList( design,
				PUBLIC_DRIVER_PROPERTIES_PROP ) );
		list.addAll( validateStructureList( design,
				PRIVATE_DRIVER_PROPERTIES_PROP ) );

		return list;
	}
}