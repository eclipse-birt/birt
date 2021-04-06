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
