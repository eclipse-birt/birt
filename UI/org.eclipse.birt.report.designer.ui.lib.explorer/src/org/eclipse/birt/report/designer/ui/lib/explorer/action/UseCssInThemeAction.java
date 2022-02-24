/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInThemeDialog;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * UseCssInThemeAction
 */
public class UseCssInThemeAction extends Action {

	private LibraryExplorerTreeViewPage viewer;

	private static final String ACTION_TEXT = Messages.getString("UseCssInThemeAction.Text"); //$NON-NLS-1$

	public UseCssInThemeAction(LibraryExplorerTreeViewPage page) {
		super(ACTION_TEXT);
		this.viewer = page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		Object obj = SessionHandleAdapter.getInstance().getReportDesignHandle();
		LibraryHandle moduleHandle;
		if ((obj == null) || (!(obj instanceof LibraryHandle))) {
			return false;
		}
		moduleHandle = (LibraryHandle) obj;
		CssStyleSheetHandle cssHandle = getSelectedCssStyleHandle();
		if (cssHandle == null) {
			return false;
		}
		SlotHandle slotHandle = moduleHandle.getThemes();
		for (Iterator iter = slotHandle.iterator(); iter.hasNext();) {
			AbstractThemeHandle theme = (AbstractThemeHandle) iter.next();
			if (theme.canAddCssStyleSheet(cssHandle)) {
				return true;
			}
		}
		return false;

	}

	private CssStyleSheetHandle getSelectedCssStyleHandle() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if (selection != null && selection.size() == 1) {
			Object selected = selection.getFirstElement();
			if (selected instanceof CssStyleSheetHandle) {
				return (CssStyleSheetHandle) selected;
			} else if (selected instanceof ReportResourceEntry
					&& ((ReportResourceEntry) selected).getReportElement() instanceof CssStyleSheetHandle) {
				return (CssStyleSheetHandle) ((ReportResourceEntry) selected).getReportElement();
			} else if (selected instanceof ResourceEntryWrapper
					&& ((ResourceEntryWrapper) selected).getType() == ResourceEntryWrapper.CSS_STYLE_SHEET) {
				return (CssStyleSheetHandle) ((ResourceEntryWrapper) selected).getAdapter(CssStyleSheetHandle.class);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		CssStyleSheetHandle cssHandle = getSelectedCssStyleHandle();
		UseCssInThemeDialog dialog = new UseCssInThemeDialog();
		String relativeFileName = cssHandle.getFileName();
		dialog.setFileName(relativeFileName);

		if (dialog.open() == Dialog.OK) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(ACTION_TEXT);

			AbstractThemeHandle themeHandle = dialog.getTheme();
			try {
				IncludedCssStyleSheet css = StructureFactory.createIncludedCssStyleSheet();
				css.setFileName(dialog.getFileName());
				css.setExternalCssURI(dialog.getURI());
				themeHandle.addCss(css);
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
				stack.rollback();
				return;
			}
			stack.commit();
		}
	}

}
