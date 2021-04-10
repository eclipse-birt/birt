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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInReportDialog;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInThemeDialog;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.jface.dialogs.Dialog;

/**
 * 
 */

public class UseCssStyleAction extends AbstractViewAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.UseCssStyleAction"; //$NON-NLS-1$

	public static final String ACTION_TEXT = Messages.getString("UseCssStyleAction.text"); //$NON-NLS-1$

	public UseCssStyleAction(Object selectedObject) {
		this(selectedObject, ACTION_TEXT);
	}

	public UseCssStyleAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		Object selection = getSelection();
		if (selection == null) {
			return;
		}
		if ((selection instanceof SlotHandle)
				&& (((SlotHandle) selection).getElementHandle() instanceof ReportDesignHandle)) {
			useCssInReportDesign();
		} else if (selection instanceof AbstractThemeHandle) {
			useCssInTheme((AbstractThemeHandle) selection);
		}
	}

	private void useCssInReportDesign() {
		UseCssInReportDialog dialog = new UseCssInReportDialog();
		String relativeFileName = null;
		dialog.setFileName(relativeFileName);
		if (dialog.open() == Dialog.OK) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(ACTION_TEXT);

			try {
				ReportDesignHandle moduleHandle = (ReportDesignHandle) SessionHandleAdapter.getInstance()
						.getReportDesignHandle();

				{
					IncludedCssStyleSheet css = StructureFactory.createIncludedCssStyleSheet();
					css.setExternalCssURI(dialog.getURI());
					css.setUseExternalCss(dialog.isUseUri());
					css.setFileName(dialog.getFileName());
					moduleHandle.addCss(css);
				}
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				ExceptionHandler.handle(e);
				stack.rollback();
				return;
			}
			stack.commit();
		}
	}

	private void useCssInTheme(AbstractThemeHandle oldTheme) {
		UseCssInThemeDialog dialog = new UseCssInThemeDialog();
		String relativeFileName = null;
		dialog.setFileName(relativeFileName);
		dialog.setTheme(oldTheme);
		if (dialog.open() == Dialog.OK) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(ACTION_TEXT);
			AbstractThemeHandle themeHandle = dialog.getTheme();
			try {
				IncludedCssStyleSheet css = StructureFactory.createIncludedCssStyleSheet();
				css.setUseExternalCss(dialog.isUseUri());
				css.setExternalCssURI(dialog.getURI());
				css.setFileName(dialog.getFileName());
				themeHandle.addCss(css);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				stack.rollback();
				return;
			}
			stack.commit();
		}
	}

}
