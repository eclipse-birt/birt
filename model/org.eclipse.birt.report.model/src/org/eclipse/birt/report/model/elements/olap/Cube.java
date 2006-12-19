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
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

/**
 * This class represents a Cube element. Cube is collection of dimensions and
 * measures. It specifies a dataset to refer to o outside data set element.Use
 * the {@link org.eclipse.birt.report.model.api.olap.CubeHandle}class to change the
 * properties.
 * 
 */

public class Cube extends ReferenceableElement implements ICubeModel
{

	/**
	 * Default constructor.
	 */
	
	public Cube( )
	{
		initSlots( );
	}

	/**
	 * Constructs a cube element with the given name.
	 * 
	 * @param name
	 *            the name given for the element
	 */

	public Cube( String name )
	{
		super( name );
		initSlots( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitCube( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.CUBE_ELEMENT;
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
	 *            the module of the cube
	 * 
	 * @return an API handle for this element.
	 */

	public CubeHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new CubeHandle( module, this );
		}
		return (CubeHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */
	
	public ContainerSlot getSlot( int slot )
	{
		assert slot >= 0 && slot < SLOT_COUNT;
		return slots[slot];
	}
}
