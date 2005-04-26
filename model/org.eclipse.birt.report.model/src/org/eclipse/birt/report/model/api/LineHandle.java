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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.LineItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.ILineItemModel;

/**
 * Represents the line item. The user can set the line orientation.
 * 
 * @see org.eclipse.birt.report.model.elements.LineItem
 */

public class LineHandle extends ReportItemHandle implements ILineItemModel
{

	/**
	 * Constructs a line handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public LineHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the orientation of the line. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>LINE_ORIENTATION_HORIZONTAL</code>
	 * <li><code>LINE_ORIENTATION_VERTICAL</code>
	 * </ul>
	 * The default is <code>LINE_ORIENTATION_HORIZONTAL</code>.
	 * 
	 * @return the orientation of the line
	 */

	public String getOrientation( )
	{
		return getStringProperty( LineItem.ORIENTATION_PROP );
	}

	/**
	 * Sets the orientation of the line. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>LINE_ORIENTATION_HORIZONTAL</code>
	 * <li><code>LINE_ORIENTATION_VERTICAL</code>
	 * </ul>
	 * 
	 * @param orientation
	 *            the orientation of the line
	 * @throws SemanticException
	 *             if the input orientation is not one of the above.
	 */

	public void setOrientation( String orientation ) throws SemanticException
	{
		setStringProperty( LineItem.ORIENTATION_PROP, orientation );
	}
}