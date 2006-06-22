/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.core.model.views.property;


/**
 * Provide the vitual root of properties view.
 */
public class PropertySheetRootElement
{

	private Object model;
	private String displayName;

	/**
	 * Constructuor
	 * @param model, selected element
	 */
	public PropertySheetRootElement(Object model)
	{
		this.model = model;
	}
	
	/** Set model
	 * @return model
	 */
	public Object getModel()
	{
		return model;
	}
	
	/** Get display name
	 * @return display name of root element
	 */
	public String getDisplayName()
	{
		return displayName;
	}
	
	/**
	 * Set display name
	 * @param displayName
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
}
