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

/**
 * Retrieves resources in the Eclipse bundle.
 * 
 */

public interface IBundleFactory {

	/**
	 * Returns the url of resource which is in corresponding bundle.
	 * 
	 * @param bundleName   the Eclipse bundle name
	 * @param resourceName the relative file name
	 * @return the url of resource if found
	 */

	public URL getBundleResource(String bundleName, String resourceName);

}
