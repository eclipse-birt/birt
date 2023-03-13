/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.resource;

import java.net.URL;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.swt.graphics.Image;

/**
 * ResourceEntryWrapper
 */
public class ResourceEntryWrapper implements ResourceEntry {

	public static final int LIBRARY = 0;
	public static final int CSS_STYLE_SHEET = 1;
	public static final int RPTDESIGN = 2;

	private ResourceEntry proxy;
	private int type;

	public ResourceEntryWrapper(int type, ResourceEntry entry) {
		this.type = type;
		proxy = entry;
	}

	public int getType() {
		return type;
	}

	@Override
	public void dispose() {
		proxy.dispose();
	}

	@Override
	public ResourceEntry[] getChildren() {
		return proxy.getChildren();
	}

	@Override
	public ResourceEntry[] getChildren(Filter filter) {
		return proxy.getChildren(filter);
	}

	@Override
	public String getDisplayName() {
		return proxy.getDisplayName();
	}

	@Override
	public Image getImage() {
		return proxy.getImage();
	}

	@Override
	public String getName() {
		return proxy.getName();
	}

	@Override
	public boolean hasChildren() {
		return proxy.hasChildren();
	}

	@Override
	public ResourceEntry getParent() {
		return proxy.getParent();
	}

	@Override
	public URL getURL() {
		return proxy.getURL();
	}

	@Override
	public boolean isFile() {
		return proxy.isFile();
	}

	@Override
	public boolean isRoot() {
		return proxy.isRoot();
	}

	@Override
	public Object getAdapter(Class adapter) {
		return proxy.getAdapter(adapter);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ResourceEntryWrapper)) {
			return false;
		}

		if (object == this) {
			return true;
		} else {
			ResourceEntryWrapper temp = (ResourceEntryWrapper) object;

			if (temp.proxy.equals(this.proxy) && temp.type == this.type) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return proxy.hashCode() * 7 + type;
	}

	public ResourceEntry getEntry() {
		return proxy;
	}

}
