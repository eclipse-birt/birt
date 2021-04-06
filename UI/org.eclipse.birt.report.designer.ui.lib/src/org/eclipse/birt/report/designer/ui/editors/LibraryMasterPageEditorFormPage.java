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

import org.eclipse.birt.report.designer.internal.lib.editparts.LibraryMasterPageGraphicalPartFactory;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportMasterPageEditorFormPage;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;

/**
 * 
 */

public class LibraryMasterPageEditorFormPage extends ReportMasterPageEditorFormPage {
	protected EditPartFactory getEditPartFactory() {
		return new LibraryMasterPageGraphicalPartFactory();
	}

	public boolean onBroughtToTop(IReportEditorPage prePage) {
		if (getEditorInput() != prePage.getEditorInput()) {
			setInput(prePage.getEditorInput());
		}

		ModuleHandle newModel = getProvider().queryReportModuleHandle();
		boolean reload = false;
		if (getStaleType() == IPageStaleType.MODEL_RELOAD) {
			setModel(null);
			doSave(null);
			reload = true;
		}
		if ((newModel != null && getModel() != newModel) || reload) {
			ModuleHandle oldModel = getModel();

			getProvider().connect(newModel);
			setModel(newModel);

			rebuildReportDesign(oldModel);
			if (getModel() != null) {
				setViewContentsAsMasterPage();
				markPageStale(IPageStaleType.NONE);
			}
			updateStackActions();

		}
		// reselect the selection
		GraphicalViewer view = getGraphicalViewer();

		UIUtil.resetViewSelection(view, true);
		return true;
	}
}
