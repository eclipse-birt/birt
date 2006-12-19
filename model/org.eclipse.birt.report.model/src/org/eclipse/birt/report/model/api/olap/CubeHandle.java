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

package org.eclipse.birt.report.model.api.olap;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

/**
 * Represents a cube.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Cube
 */

public class CubeHandle extends ReportElementHandle implements ICubeModel
{

	/**
	 * Constructs a handle for the given design and design element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public CubeHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the data set of this cube.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		return (DataSetHandle) getElementProperty( DATA_SET_PROP );
	}

	/**
	 * Sets the data set of this cube.
	 * 
	 * @param handle
	 *            the handle of the data set
	 * 
	 * @throws SemanticException
	 *             if the property is locked, or the data-set is invalid.
	 */

	public void setDataSet( DataSetHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( DATA_SET_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						handle.getName( ) );
			}
			setStringProperty( DATA_SET_PROP, valueToSet );
		}
	}

	/**
	 * Gets the slot handle of all the dimension elements.
	 * 
	 * @return the slot handle of all dimension elements
	 */

	public SlotHandle getDimensions( )
	{
		return getSlot( DIMENSION_SLOT );
	}

	/**
	 * Gets the slot handle of all measure elements.
	 * 
	 * @return the slot handle of all measure elements
	 */

	public SlotHandle getMeasures( )
	{
		return getSlot( MEASURE_SLOT );
	}

	/**
	 * Gets the iterator of the join conditions. Each one in the iterator is
	 * instance of <code>StructureHandle</code>.
	 * 
	 * @return iterator of the join conditions in this cube
	 */

	public Iterator joinConditionsIterator( )
	{
		PropertyHandle propertyHandle = getPropertyHandle( DIMENSION_CONDITIONS_PROP );
		return propertyHandle.iterator( );
	}
}
