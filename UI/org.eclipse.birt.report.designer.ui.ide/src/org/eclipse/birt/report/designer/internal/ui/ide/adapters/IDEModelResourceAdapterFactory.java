/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Path;

/**
 * This class adapts the BIRT model to IResource in workspace, basically it is
 * to resolve the current IResoruce selection when focus on non-editor views,
 * e.g. outline view, data explorer view.
 */
public class IDEModelResourceAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IResource.class) {
			if (adaptableObject instanceof SlotHandle) {
				String file = ((SlotHandle) adaptableObject).getModule().getFileName();
				if (file != null) {
					IResource[] res = ResourcesPlugin.getWorkspace().getRoot()
							.findFilesForLocation(Path.fromOSString(file));

					if (res != null && res.length > 0) {
						return res[0];
					}
				}
			}
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IResource.class };
	}

}
