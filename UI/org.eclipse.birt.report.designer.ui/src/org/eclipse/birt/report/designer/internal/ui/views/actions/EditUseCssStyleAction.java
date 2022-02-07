/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInReportDialog;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInThemeDialog;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.jface.dialogs.Dialog;

/**
 * 
 */

public class EditUseCssStyleAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.EditUseCssStyleAction"; //$NON-NLS-1$

	public static final String ACTION_TEXT = Messages.getString("EditUseCssStyleAction.text"); //$NON-NLS-1$

	public EditUseCssStyleAction(Object selectedObject) {
		this(selectedObject, ACTION_TEXT);
	}

	public EditUseCssStyleAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		Object selection = getSelection();
		if (selection == null) {
			return false;
		}
		if (selection instanceof CssStyleSheetHandle) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Object selection = getSelection();
		assert (selection instanceof CssStyleSheetHandle);
		CssStyleSheetHandle cssStyle = (CssStyleSheetHandle) selection;
		Object container = cssStyle.getContainerHandle();
		if (container instanceof ReportDesignHandle) {
			editCssInReportDesign(cssStyle, (ReportDesignHandle) container);
		} else if (container instanceof AbstractThemeHandle) {
			editCssInTheme(cssStyle, (AbstractThemeHandle) container);
		}
	}

	private void editCssInTheme(CssStyleSheetHandle cssStyle, AbstractThemeHandle theme) {
		UseCssInThemeDialog dialog = new UseCssInThemeDialog();
		dialog.setDialogTitle(Messages.getString("EditUseCssStyleAction.EditCssTitle"));
		dialog.setTitle(Messages.getString("EditUseCssStyleAction.EditCssAreaTitle.Libary"));
		IncludedCssStyleSheetHandle includedCss = theme.findIncludedCssStyleSheetHandleByProperties(
				cssStyle.getFileName(), cssStyle.getExternalCssURI(), cssStyle.isUseExternalCss());
		dialog.setIncludedCssStyleSheetHandle(includedCss);
		dialog.setTheme(theme);
		if (dialog.open() == Dialog.OK) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(ACTION_TEXT);

			AbstractThemeHandle themeHandle = dialog.getTheme();
			if (themeHandle == theme) {
				try {
					themeHandle.renameCssByProperties(includedCss, dialog.getFileName(), dialog.getURI(),
							dialog.isUseUri());
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
					stack.rollback();
					return;
				}
			} else {
				try {
					theme.dropCss(cssStyle);
					IncludedCssStyleSheet css = StructureFactory.createIncludedCssStyleSheet();
					css.setFileName(dialog.getFileName());
					css.setExternalCssURI(dialog.getURI());
					css.setUseExternalCss(dialog.isUseUri());
					themeHandle.addCss(css);
				} catch (SemanticException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stack.rollback();
					return;
				}
			}

			stack.commit();

		}
	}

	private void editCssInReportDesign(CssStyleSheetHandle cssStyle, ReportDesignHandle reportDesign) {
		UseCssInReportDialog dialog = new UseCssInReportDialog();
		dialog.setDialogTitle(Messages.getString("EditUseCssStyleAction.EditCssTitle"));
		dialog.setTitle(Messages.getString("EditUseCssStyleAction.EditCssAreaTitle.Report"));
		IncludedCssStyleSheetHandle includedCss = reportDesign.findIncludedCssStyleSheetHandleByProperties(
				cssStyle.getFileName(), cssStyle.getExternalCssURI(), cssStyle.isUseExternalCss());

		dialog.setIncludedCssStyleSheetHandle(includedCss);
		if (dialog.open() == Dialog.OK) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(ACTION_TEXT);

			try {
				reportDesign.renameCssByProperties(includedCss, dialog.getFileName(), dialog.getURI(),
						dialog.isUseUri());
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				ExceptionHandler.handle(e);
				stack.rollback();
				return;
			}
			stack.commit();
		}
	}

}
