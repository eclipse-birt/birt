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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * This class represents a Cube element. Cube is collection of dimensions and
 * measures. It specifies a dataset to refer to o outside data set element.Use
 * the {@link org.eclipse.birt.report.model.api.olap.CubeHandle}class to change
 * the properties.
 * 
 */

public class TabularCube extends Cube
{

	/**
	 * Default constructor.
	 */

	public TabularCube( )
	{
	}

	/**
	 * Constructs a cube element with the given name.
	 * 
	 * @param name
	 *            the name given for the element
	 */

	public TabularCube( String name )
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
		visitor.visitTabularCube( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.TABULAR_CUBE_ELEMENT;
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

	public TabularCubeHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new TabularCubeHandle( module, this );
		}
		return (TabularCubeHandle) handle;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.ReferenceableElement#doClone(org.eclipse
	 * .birt.report.model.elements.strategy.CopyPolicy)
	 */
	public Object doClone( CopyPolicy policy )
			throws CloneNotSupportedException
	{
		DesignElement element = (DesignElement) super.doClone( policy );

		handleDefaultMeasureGroup( element );
		return element;
	}

	/**
	 * The special case for the default measure group. If the cube extends a
	 * library cube, the default measure group is set to be local measure group.
	 * 
	 * @param cloned
	 */

	protected void handleDefaultMeasureGroup( DesignElement cloned )
	{
		Module module = getRoot( );
		DesignElement measureGroup = getDefaultMeasureGroup( module );
		if ( measureGroup != null )
		{
			int index = measureGroup.getIndex( module );
			DesignElement clonedMeasureGroup = new ContainerContext( cloned,
					MEASURE_GROUPS_PROP ).getContent( module, index );
			assert clonedMeasureGroup != null;
			cloned.setProperty( DEFAULT_MEASURE_GROUP_PROP,
					new ElementRefValue( null, clonedMeasureGroup ) );
		}
	}
	
	/**
	 * Sets the measure group at the specified position to be default.
	 * 
	 * @param index
	 */
	
	public void setDefaultMeasureGroup( int index )
	{
		List groups = getListProperty( getRoot( ), MEASURE_GROUPS_PROP );
		if ( groups == null || groups.isEmpty( ) )
			return;
		if ( index >= 0 && index < groups.size( ) )
			setProperty( Cube.DEFAULT_MEASURE_GROUP_PROP, new ElementRefValue(
					null, (DesignElement) groups.get( index ) ) );
	}
}
