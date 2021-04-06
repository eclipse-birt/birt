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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import java.net.URL;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public interface ResourceEntry extends IAdaptable {

	public static interface Filter {

		public boolean accept(ResourceEntry entity);
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
