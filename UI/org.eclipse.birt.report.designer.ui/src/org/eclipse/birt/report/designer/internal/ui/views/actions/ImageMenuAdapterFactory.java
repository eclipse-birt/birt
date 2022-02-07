/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
