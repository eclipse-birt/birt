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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * Represents an extended data set.
 */

public class OdaDataSet extends DataSet
{

	/**
	 * The property name of the query statement.
	 */

	public static final String QUERY_TEXT_PROP = "queryText"; //$NON-NLS-1$

	/**
	 * The property name of the query type.
	 */

	public static final String TYPE_PROP = "type"; //$NON-NLS-1$


	/**
	 * The property name of the result set name.
	 */

	public static final String RESULT_SET_NAME_PROP = "resultSetName"; //$NON-NLS-1$

	/**
	 * Name of the driver state property that gives the private driver design
	 * state from the extended driver. It is optional from the ODA data source
	 * builder. This is a CDATA section. If omitted or discarded, it will be
	 * recomputed when the report is next edited or run.
	 */

	public static final String PRIVATE_DRIVER_DESIGN_STATE_PROP = "privateDriverDesignState"; //$NON-NLS-1$

	/**
	 * The property name of the script which provides the query.
	 */

	public static final String QUERY_SCRIPT_METHOD = "queryScript"; //$NON-NLS-1$

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

	public OdaDataSet( )
	{
		super( );
	}

	/**
	 * Constructs an extended data set with name.
	 * 
	 * @param theName
	 *            the name of this extended data set
	 */

	public OdaDataSet( String theName )
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
		visitor.visitOdaDataSet( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.ODA_DATA_SET;
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

	public OdaDataSetHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new OdaDataSetHandle( design, this );
		}
		return (OdaDataSetHandle) handle;
	}
}