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

package org.eclipse.birt.report.model.plugin;

import java.net.URL;

import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.birt.report.model.api.IBundleFactory;
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

	@Override
	public URL getBundleResource(String bundleName, String resourceName) {
		Bundle bundle = EclipseUtil.getBundle(bundleName);
		if (bundle != null) {
			return bundle.getResource(resourceName);
		}

		return null;
	}
}
