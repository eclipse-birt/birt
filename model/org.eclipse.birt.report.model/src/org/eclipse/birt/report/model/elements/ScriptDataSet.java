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
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;

/**
 * This class represents script data set. The script data set provides the
 * ability to implement a data set in code.
 *  
 */

public class ScriptDataSet extends DataSet
{

	/**
	 * Name of script property for opening the data set.
	 */

	public static final String OPEN_METHOD = "open"; //$NON-NLS-1$

	/**
	 * Name of script property for describing the result set dynamically.
	 */

	public static final String DESCRIBE_METHOD = "describe"; //$NON-NLS-1$

	/**
	 * Name of script property for providing the data for the next row from the
	 * result set.
	 */

	public static final String FETCH_METHOD = "fetch"; //$NON-NLS-1$

	/**
	 * Name of script property for closing the data set.
	 */

	public static final String CLOSE_METHOD = "close"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public ScriptDataSet( )
	{
		super( );
	}

	/**
	 * Constructs a script data set with name.
	 * 
	 * @param theName
	 *            the name of this script data set
	 */

	public ScriptDataSet( String theName )
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
		visitor.visitScriptDataSet( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.SCRIPT_DATA_SET;
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

	public ScriptDataSetHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new ScriptDataSetHandle( design, this );
		}
		return (ScriptDataSetHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#valdiate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		list.addAll( ValueRequiredValidator.getInstance( ).validate( design,
				this, FETCH_METHOD ) );

		return list;
	}
}