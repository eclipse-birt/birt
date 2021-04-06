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

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.draw2d.IFigure;

/**
 * The interface defines all the UI dynamic behaviour for the extended element
 * 
 * The IExtendedElementUI allows the user to customize the appearance of the
 * element inside the designer. The user can implement an quick edit page to be
 * shown in the Quick Edit View and a builder when a new element is created.
 */

public interface IReportItemFigureProvider {

	/**
	 * Gets the figure to be rendered for the extended element in the
	 * designer.Cannot return null.
	 * 
	 * @param handle the handle of the element
	 * 
	 * @return Returns the figure
	 */
	public IFigure createFigure(ExtendedItemHandle handle);

	/**
	 * Updates the figure based on the handle properties. The figure passed is the
	 * same as the one returned by getFigure. This function should make sure to
	 * update all necessary properties of the figure using the relevant information
	 * in the handle. It should not invalidate the figure, or try to access its
	 * parent layout manager.
	 */
	public void updateFigure(ExtendedItemHandle handle, IFigure figure);

	/**
	 * Frees resources when the item is no longer part of the editor. Implementors
	 * are responsible to dispose any resource allocated for the figure
	 * 
	 * @param handle the handle of the element
	 * @param figure the figure
	 */
	public void disposeFigure(ExtendedItemHandle handle, IFigure figure);

}