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

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * 
 */

public abstract class AbstractMultiPageEditor extends FormEditor {

	protected void pageChange(int newPageIndex) {
		// TODO Auto-generated method stub
		super.pageChange(newPageIndex);
		updateActionBarContributor(newPageIndex);
	}

	/**
	 * Notifies action bar contributor about page change.
	 * 
	 * @param pageIndex the index of the new page
	 */
	protected void updateActionBarContributor(int pageIndex) {
		super.updateActionBarContributor(pageIndex);
		// Overwrite this method to implement multi-editor action bar
		// contributor
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof IMultiPageEditorActionBarContributor && pageIndex >= 0 && pageIndex < pages.size()) {
			Object page = pages.get(pageIndex);
			if (page instanceof IFormPage) {
				((IMultiPageEditorActionBarContributor) contributor).setActivePage((IFormPage) page);
			}
		}

	}

	/**
	 * Refresh resource markser
	 * 
	 * temporary for WTP XML editor
	 * 
	 * @param input
	 * @throws CoreException
	 */
	public void refreshMarkers(IEditorInput input) throws CoreException {

	}
}
