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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.interfaces.IFreeFormModel;

/**
 * Represents a free-form element. Free-form is the simplest form of report
 * container. A container item holds a collection of other report items. Every
 * item in the container is positioned at an (x, y) location relative to the top
 * left corner of the container. In Free-form elements can be positioned
 * anywhere.
 */

public class FreeFormHandle extends ReportItemHandle implements IFreeFormModel
{

	/**
	 * Constructs a free-form handle with the given design and the free-from.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public FreeFormHandle( Module module, FreeForm element )
	{
		super( module, element );
	}

	/**
	 * Returns a slot handle to work with the Report Items within the free-form.
	 * 
	 * @return a slot handle for the report items in the free-from.
	 * @see SlotHandle
	 */

	public SlotHandle getReportItems( )
	{
		return getSlot( IFreeFormModel.REPORT_ITEMS_SLOT );
	}
}