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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDerivedDataSetModel;

/**
 * Class for derived data set.
 * 
 */

public class DerivedDataSet extends DataSet implements IDerivedDataSetModel
{

	/**
	 * Default constructor.
	 */

	public DerivedDataSet( )
	{
	}

	/**
	 * Constructs this data set by name.
	 * 
	 * @param name
	 *            of the data set.
	 */

	public DerivedDataSet( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitDerivedDataSet( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.DERIVED_DATA_SET_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.core.Module)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design of the derived data set
	 * 
	 * @return an API handle for this element
	 */

	public DerivedDataSetHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new DerivedDataSetHandle( module, this );
		}
		return (DerivedDataSetHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.DataSet#validate(org.eclipse.birt
	 * .report.model.core.Module)
	 */

	public List<SemanticException> validate( Module module )
	{
		return Collections.emptyList( );
	}
}
