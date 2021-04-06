/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers.ISchematicMenuListener;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

/**
 * 
 */

public class ImageMenuAdapterFactory implements IAdapterFactory {

	public Object getAdapter(final Object adaptableObject, Class adapterType) {
		if ((adaptableObject instanceof ImageHandle) && adapterType == IMenuListener.class) {

			return new ISchematicMenuListener() {

				public void menuAboutToShow(IMenuManager manager) {
					manager.appendToGroup(GEFActionConstants.GROUP_EDIT, new ReloadImageAction(adaptableObject));
				}

				public void setActionRegistry(ActionRegistry actionRegistry) {
				}
			};
		}
		return null;
	}

	public Class[] getAdapterList() {
		return null;
	}

}
