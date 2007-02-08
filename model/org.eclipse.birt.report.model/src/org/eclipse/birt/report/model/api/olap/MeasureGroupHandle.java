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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureGroupModel;

/**
 * Handle class for MeasureGroup. It holds a list of MeasureHandle.
 */
public class MeasureGroupHandle extends ReportElementHandle
		implements
			IMeasureGroupModel
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

	public MeasureGroupHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Indicates whether this measure group is default in the cube.
	 * 
	 * @return true if this measure group is default in the cube, otherwise
	 *         false
	 */
	public boolean isDefault( )
	{
		return getBooleanProperty( IS_DEFAULT_PROP );
	}

	/**
	 * Sets the status to indicate whether this measure group is default in the
	 * cube.
	 * 
	 * @param isDefault
	 *            status whether this measure group is default in the cube
	 * @throws SemanticException
	 */
	public void setDefault( boolean isDefault ) throws SemanticException
	{
		setProperty( IS_DEFAULT_PROP, Boolean.valueOf( isDefault ) );
	}
}
