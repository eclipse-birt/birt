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
