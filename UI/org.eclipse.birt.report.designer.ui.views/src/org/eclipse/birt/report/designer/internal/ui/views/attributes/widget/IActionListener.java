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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

/**
 * A Listener interface for receiving events produced by Render. The Descriptor
 * can act as a deputy of Render(Not support now).
 * 
 * 
 */
public interface IActionListener {

	// TODO: use IActionListener instead of IPropertyRenderListener.
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param newValue The new value saved to DE model.
	 */
	void performAction(String newValue) throws Exception;
}