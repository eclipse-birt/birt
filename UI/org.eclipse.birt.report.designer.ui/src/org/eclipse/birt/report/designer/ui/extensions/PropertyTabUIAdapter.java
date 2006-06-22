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

package org.eclipse.birt.report.designer.ui.extensions;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Adapter class for IPropertyTabUI, default doing nothing.
 */
abstract public class PropertyTabUIAdapter implements IPropertyTabUI
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#buildUI(org.eclipse.swt.widgets.Composite)
	 */
	public void buildUI( Composite composite )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#getTabDisplayName()
	 */
	public String getTabDisplayName( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#setInput(java.util.List)
	 */
	public void setInput( List elements )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#elementChanged(org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged( NotificationEvent ev )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#dispose()
	 */
	public void dispose( )
	{
	}

}
