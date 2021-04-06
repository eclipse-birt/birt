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

/**
 * Property Listener. Listens to property changes in the Property Editor Tabs UI
 */
public interface IPropertyListener {
	/**
	 * This is called when a property changes in the UI
	 * 
	 * @param name  the name of the property that has changed
	 * @param value the value of the property (must include the unit if any)
	 */
	public void propertyChanged(String name, String value);
}
