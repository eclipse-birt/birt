/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
