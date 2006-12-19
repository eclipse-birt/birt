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

import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;

/**
 * Represents a dimension element in the cube element.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Dimension
 */

public class DimensionHandle extends ReportElementHandle
		implements
			IDimensionModel
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

	public DimensionHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Indicates whether this dimension is a special type of Time.
	 * 
	 * @return true if this dimension is of Time type, otherwise false
	 */

	public boolean isTimeType( )
	{
		return getBooleanProperty( IS_TIME_TYPE_PROP );
	}

	/**
	 * Sets the status to indicate whether this dimension is a special type of
	 * Time.
	 * 
	 * @param isTimeType
	 *            status whether this dimension is of Time type
	 * @throws SemanticException
	 *             the property is locked
	 */

	public void setTimeType( boolean isTimeType ) throws SemanticException
	{
		setProperty( IS_TIME_TYPE_PROP, Boolean.valueOf( isTimeType ) );
	}

	/**
	 * Gets the slot handle that holds all the hierarchy elements.
	 * 
	 * @return slot handle of all hierarchy elements
	 */

	public SlotHandle getHierarchies( )
	{
		return getSlot( HIERARCHY_SLOT );
	}
}
