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

import org.eclipse.swt.widgets.Composite;

/**
 * The interface used to define the detail of a tab in the Property Editor
 */

public interface IPropertyTabUI
{

	/**
	 * Creates the widgets to be shown in the tab, note that this must used the
	 * PropertyDescriptors framework to link them to the model.
	 * 
	 * @param parent
	 *            the composite which the widgets are built on
	 */
	public void buildUI( Composite parent );

	/**
	 * Gets the list of all property descriptors that have been used to build
	 * the UI. This method is called after buildUI has been called at least
	 * once. Existing PropertyDescriptors should be reused if possible.
	 * 
	 * @return Returns the property descriptors used in the Quick Edit View
	 */
	public IPropertyDescriptor[] getPropertyDescriptors( );

}