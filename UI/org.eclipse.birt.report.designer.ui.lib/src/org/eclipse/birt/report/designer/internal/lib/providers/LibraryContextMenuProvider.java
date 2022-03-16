/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.designer.internal.lib.providers;

import java.util.List;

import org.eclipse.birt.report.designer.internal.lib.editors.actions.ExportAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers.SchematicContextMenuProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IMenuManager;

/**
 * Context menu provider for the library editor.
 *
 */
public class LibraryContextMenuProvider extends SchematicContextMenuProvider {

	/**
	 * @param viewer
	 * @param actionRegistry
	 */
	public LibraryContextMenuProvider(EditPartViewer viewer, ActionRegistry actionRegistry) {
		super(viewer, actionRegistry);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers.
	 * SchematicContextMenuProvider#buildContextMenu(org.eclipse.jface.action.
	 * IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager menuManager) {
		super.buildContextMenu(menuManager);
		List list = getElements();
		if (list.size() == 1 && list.get(0) instanceof DesignElementHandle) {
			appendToGroup(GEFActionConstants.GROUP_REST, getAction(ExportAction.ID));
		}
	}
}
