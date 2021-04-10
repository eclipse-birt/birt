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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;

/**
 * This listener is notified of changes to resources in the resource path. The
 * resources under resource path is suppose to be one of following:
 * <ul>
 * <li>libraries
 * </ul>
 * 
 * <p>
 * Clients may implement this interface.
 * </p>
 * 
 */

public interface IResourceChangeListener {

	/**
	 * Notifies this listener that some resource changes happened. The supplied
	 * event gives details.
	 * 
	 * @param module the module
	 * 
	 * @param event  the resource change event
	 */
	public void resourceChanged(ModuleHandle module, ResourceChangeEvent event);
}
