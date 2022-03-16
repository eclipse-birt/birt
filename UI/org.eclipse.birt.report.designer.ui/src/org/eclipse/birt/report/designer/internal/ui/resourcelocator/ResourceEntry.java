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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import java.net.URL;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public interface ResourceEntry extends IAdaptable {

	public interface Filter {

		boolean accept(ResourceEntry entity);
	}

	String getName();

	String getDisplayName();

	Image getImage();

	URL getURL();

	ResourceEntry getParent();

	ResourceEntry[] getChildren();

	boolean hasChildren();

	ResourceEntry[] getChildren(Filter filter);

	boolean isFile();

	boolean isRoot();

	void dispose();
}
