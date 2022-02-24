/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
