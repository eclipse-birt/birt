/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry.Filter;

/**
 * 
 */

public abstract class ExtendedResourceFilter extends ResourceFilter implements Filter {

	public ExtendedResourceFilter() {
	};

	public ExtendedResourceFilter(String type, String displayName, boolean isEnabled) {
		setType(type);
		setDisplayName(displayName);
		setEnabled(isEnabled);
	};

	public ExtendedResourceFilter(String type, String displayName, boolean isEnabled, String helpContent) {
		setType(type);
		setDisplayName(displayName);
		setEnabled(isEnabled);
		setDescription(helpContent);
	};

}
