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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 *
 */

public abstract class BaseResourceEntity implements ResourceEntry {

	@Override
	public Image getImage() {
		Object adapter = getAdapter(LibraryHandle.class);
		if (adapter != null) {
			return ProviderFactory.createProvider(adapter).getNodeIcon(adapter);
		}
		adapter = getAdapter(CssStyleSheetHandle.class);
		if (adapter != null) {
			return ProviderFactory.createProvider(adapter).getNodeIcon(adapter);
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
	}

	@Override
	public String getName() {
		return getURL().getFile();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ResourceEntry.class) {
			return this;
		}
		return null;
	}

	public boolean hasChildren(Filter filter) {
		return getChildren(filter).length > 0;
	}

	@Override
	public ResourceEntry[] getChildren(Filter filter) {
		ResourceEntry[] children = getChildren();
		List childrenFiltered = new ArrayList();
		for (int i = 0; i < children.length; i++) {
			if (filter.accept(children[i])) {
				childrenFiltered.add(children[i]);
			}
		}
		return (ResourceEntry[]) childrenFiltered.toArray(new ResourceEntry[childrenFiltered.size()]);
	}

}
