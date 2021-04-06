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

package org.eclipse.birt.report.model.api;

import java.net.URL;
import java.util.Map;

/**
 * The default implementation for interface {@link IResourceLocator}. This
 * implementation is empty and does nothing to search the resource.
 * </ul>
 * 
 * @see IResourceLocator
 * @see SessionHandle
 */

public class DefaultResourceLocator implements IResourceLocator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.IResourceLocator#findResource(org.eclipse
	 * .birt.report.model.api.ModuleHandle, java.lang.String, int)
	 */

	public URL findResource(ModuleHandle moduleHandle, String fileName, int type) {
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.IResourceLocator#findResource(org.eclipse
	 * .birt.report.model.api.ModuleHandle, java.lang.String, int, java.util.Map)
	 */
	public URL findResource(ModuleHandle moduleHandle, String fileName, int type, Map appContext) {
		return null;
	}

}