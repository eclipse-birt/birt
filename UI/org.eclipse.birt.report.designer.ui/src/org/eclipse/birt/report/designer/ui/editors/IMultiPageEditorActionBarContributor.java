/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * Interface for managing the installation/deinstallation of global actions for
 * multi-page editors.
 */

public interface IMultiPageEditorActionBarContributor extends IEditorActionBarContributor {

	/**
	 * Sets the active page of the the multi-page editor to be the given editor.
	 * Redirect actions to the given editor if actions are not already being sent to
	 * it.
	 * <p>
	 * This method is called whenever the page changes. Subclasses must implement
	 * this method to redirect actions to the given editor (if not already directed
	 * to it).
	 * </p>
	 * 
	 * @param page the new active page
	 */

	public void setActivePage(IFormPage page);

}
