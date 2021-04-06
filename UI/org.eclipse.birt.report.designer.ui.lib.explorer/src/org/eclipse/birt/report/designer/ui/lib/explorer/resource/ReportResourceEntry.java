/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;

public abstract class ReportResourceEntry implements ResourceEntry {

	public abstract Object getReportElement();

	public void dispose() {

	}

	public boolean hasChildren() {
		return false;
	}

	public ResourceEntry[] getChildren() {
		return new ResourceEntry[0];
	}

	public ResourceEntry[] getChildren(Filter filter) {
		ResourceEntry[] children = getChildren();
		List childrenFiltered = new ArrayList();
		for (int i = 0; i < children.length; i++) {
			if (filter.accept(children[i]))
				childrenFiltered.add(children[i]);
		}
		return (ResourceEntry[]) childrenFiltered.toArray(new ResourceEntry[childrenFiltered.size()]);
	}

	public URL getURL() {
		return null;
	}

	public boolean isFile() {
		return true;
	}

	public boolean isRoot() {
		return false;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

}
