/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

	void setActivePage(IFormPage page);

}
