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

	@Override
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
	@Override
	public URL findResource(ModuleHandle moduleHandle, String fileName, int type, Map appContext) {
		return null;
	}

}
