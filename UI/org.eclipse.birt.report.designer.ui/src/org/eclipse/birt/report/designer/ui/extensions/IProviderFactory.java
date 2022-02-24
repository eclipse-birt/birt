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

import org.eclipse.birt.report.designer.ui.views.INodeProvider;

/**
 * The interface used to extend all the views.
 */

public interface IProviderFactory {

	/**
	 * Create a new node provider for the given object. Returns the node provider,
	 * or null if it is unnecessary.
	 * 
	 * @param selectedObject the object selected in the view
	 * 
	 * @return the node provider, or null if it is unnecessary.
	 */
	public INodeProvider createProvider(Object selectedObject);
}
