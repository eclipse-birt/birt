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

package org.eclipse.birt.report.model.elements.olap;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.strategy.TabularDimensionPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * This class represents a Dimension element. Dimension contains a list of
 * hierarchy elements and a foreign key. Use the
 * {@link org.eclipse.birt.report.model.api.olap.DimensionHandle}class to change
 * the properties.
 * 
 */

public class TabularDimension extends Dimension
		implements
			ITabularDimensionModel
{

	/**
	 * Default constructor.
	 * 
	 */

	public TabularDimension( )
	{
		this( null );
	}

	/**
	 * Constructs the dimension with the given name.
	 * 
	 * @param name
	 *            name given for this dimension
	 */

	public TabularDimension( String name )
	{
		super( name );
		cachedPropStrategy = TabularDimensionPropSearchStrategy.getInstance( );
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
		visitor.visitTabularDimension( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.TABULAR_DIMENSION_ELEMENT;
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
	 *            the module of the dimension
	 * 
	 * @return an API handle for this element.
	 */

	public TabularDimensionHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new TabularDimensionHandle( module, this );
		}
		return (TabularDimensionHandle) handle;
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module
	 *            the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */

	public DesignElement getSharedDimension( Module module )
	{
		ElementRefValue dataSetRef = (ElementRefValue) getProperty( module,
				INTERNAL_DIMENSION_RFF_TYPE_PROP );
		if ( dataSetRef == null )
			return null;
		return dataSetRef.getElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.olap.Dimension#isValidHierarchy
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected boolean isValidHierarchy( DesignElement hierarchy, Module module )
	{
		return ( hierarchy.getContainer( ) == this || getSharedDimension( module ) != null );
	}
}
