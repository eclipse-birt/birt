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

package org.eclipse.birt.report.model.plugin;

import java.net.URL;

import org.eclipse.birt.core.util.EclipseUtil;
import org.eclipse.birt.report.model.api.IBundleFactory;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * The internal factory to get resources in the bundle.
 * 
 */

public class PlatformBundleFactory implements IBundleFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.IBundleFactory#getBundleResource(java.lang.
	 * String, java.lang.String)
	 */

	public URL getBundleResource(String bundleName, String resourceName) {
		Bundle bundle = EclipseUtil.getBundle(bundleName);
		if (bundle != null)
			return bundle.getResource(resourceName);

		return null;
	}
}
