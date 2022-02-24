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
