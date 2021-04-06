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

package org.eclipse.birt.report.designer.ui;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * Extends MenuManager to allow populating the menu directly from the manager
 * itself. Using this class is no different than using a standalone
 * <code>MenuManager</code>, and adding a menuAboutToShow listener.
 * 
 * 
 */

public abstract class ContextMenuProvider extends MenuManager implements IMenuListener {

	private ISelectionProvider viewer;

	/**
	 * Constructs a context menu for the specified EditPartViewer.
	 * 
	 * @param viewer the editpart viewer
	 */
	public ContextMenuProvider(ISelectionProvider viewer) {
		setViewer(viewer);
		addMenuListener(this);
		setRemoveAllWhenShown(true);

	}

	/**
	 * Called when the menu is about to show. Subclasses must implement this method
	 * to populate the menu each time it is shown.
	 * 
	 * @param menuManager this parameter is actually <code>this</code> object
	 */
	public abstract void buildContextMenu(IMenuManager menuManager);

	protected void registMenu(IMenuManager menuManager) {
	}

	/**
	 * Returns the EditPartViewer
	 * 
	 * @return the viewer
	 */
	protected ISelectionProvider getViewer() {
		return viewer;
	}

	/**
	 * Called when the menu is about to show.
	 * 
	 * @see IMenuListener#menuAboutToShow(IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager menu) {
		buildContextMenu(menu);
	}

	/**
	 * Sets the editpart viewer. Called during construction.
	 * 
	 * @param viewer the viewer
	 */
	protected void setViewer(ISelectionProvider viewer) {
		this.viewer = viewer;
	}

}