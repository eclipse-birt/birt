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
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * This class represents a Dimension element. Dimension contains a list of
 * hierarchy elements and a foreign key. Use the
 * {@link org.eclipse.birt.report.model.api.olap.DimensionHandle}class to
 * change the properties.
 * 
 */

public class Dimension extends ReferenceableElement implements IDimensionModel
{

	/**
	 * Default constructor.
	 * 
	 */

	public Dimension( )
	{
	}

	/**
	 * Constructs the dimension with the given name.
	 * 
	 * @param name
	 *            name given for this dimension
	 */

	public Dimension( String name )
	{
		super( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */
	public void apply( ElementVisitor visitor )
	{
		visitor.visitDimension( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName( )
	{
		return ReportDesignConstants.DIMENSION_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.birt.report.model.core.Module)
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

	public DimensionHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new DimensionHandle( module, this );
		}
		return (DimensionHandle) handle;
	}

	/**
	 * Gets the default hierarchy in this dimension.
	 * 
	 * @param module
	 * @return
	 */
	public DesignElement getDefaultHierarchy( Module module )
	{
		DesignElement hierarchy = getReferenceProperty( module,
				DEFAULT_HIERARCHY_PROP );
		// if hierarchy is not set or resolved, or the hierarchy does not reside
		// in this dimension, then return null
		if ( hierarchy == null || hierarchy.getContainer( ) != this )
			return null;
		return hierarchy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#doClone(org.eclipse.birt.report.model.elements.strategy.CopyPolicy)
	 */
	public Object doClone( CopyPolicy policy )
			throws CloneNotSupportedException
	{
		DesignElement element = (DesignElement) super.doClone( policy );

		Module module = getRoot( );		
		DesignElement hierarchy = getDefaultHierarchy( module );
		if ( hierarchy != null )
		{
			int index = hierarchy.getIndex( module );
			DesignElement clonedHierarchy = new ContainerContext( element,
					HIERARCHIES_PROP ).getContent( module, index );
			assert clonedHierarchy != null;
			element.setProperty( DEFAULT_HIERARCHY_PROP, new ElementRefValue(
					null, clonedHierarchy ) );
		}
		return element;
	}
}
